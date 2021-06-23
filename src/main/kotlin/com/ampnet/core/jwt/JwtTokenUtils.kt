package com.ampnet.core.jwt

import com.ampnet.core.jwt.exception.KeyException
import com.ampnet.core.jwt.exception.TokenException
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.MissingKotlinParameterException
import com.fasterxml.jackson.module.kotlin.readValue
import io.jsonwebtoken.Claims
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.jackson.io.JacksonDeserializer
import io.jsonwebtoken.jackson.io.JacksonSerializer
import io.jsonwebtoken.security.WeakKeyException
import java.io.Serializable
import java.util.Date

/**
 * This class provides static methods for encoding
 * and decoding of JWT.
 * For JWT specification see: <a href="http://www.ietf.org/rfc/rfc7519.txt">RCF 7519</a>
 */
object JwtTokenUtils : Serializable {

    private const val userKey = "user"
    private const val jwtSubject = "AMPnet"
    private val rsaKeyDecoder = RsaKeyDecoder()
    private val objectMapper: ObjectMapper by lazy {
        val mapper = ObjectMapper()
        mapper.propertyNamingStrategy = PropertyNamingStrategy.SNAKE_CASE
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        mapper.registerModule(KotlinModule())
    }

    /**
     * Decode Address from JWT.
     *
     * @param token encoded JWT.
     * @param publicKey public key as String in DER format for verifying JWT signature.
     * @return decoded Address data from JWT.
     * @exception KeyException if the [publicKey] is invalid format.
     * @exception TokenException if the [token] is not valid.
     */
    @Throws(KeyException::class, TokenException::class)
    fun decodeToken(token: String, publicKey: String): Address {
        val decodedPublicKey = rsaKeyDecoder.getPublicKey(publicKey)
        try {
            val jwtParser = Jwts.parserBuilder()
                .deserializeJsonWith(JacksonDeserializer(objectMapper))
                .setSigningKey(decodedPublicKey)
                .build()
            val claimsJws = jwtParser.parseClaimsJws(token)
            val claims = claimsJws.body
            validateExpiration(claims)
            return getAddress(claims)
        } catch (ex: JwtException) {
            throw TokenException("Could not validate JWT token", ex)
        }
    }

    /**
     * Encode Address data to JWT.
     *
     * @param address Address data to encode to JWT.
     * @param privateKey private key as String in DER format  to sign JWT.
     * @param validityInMillis validity of JWT in milliseconds.
     * @return encoded JWT token.
     * @exception KeyException if the [privateKey] is invalid format or too weak.
     */
    @Throws(KeyException::class, TokenException::class)
    fun encodeToken(address: Address, privateKey: String, validityInMillis: Long): String {
        val decodedPrivateKey = rsaKeyDecoder.getPrivateKey(privateKey)
        try {
            return Jwts.builder()
                .setSubject(jwtSubject)
                .serializeToJsonWith(JacksonSerializer(objectMapper))
                .claim(userKey, objectMapper.writeValueAsString(address))
                .signWith(decodedPrivateKey, SignatureAlgorithm.RS256)
                .setIssuedAt(Date())
                .setExpiration(Date(System.currentTimeMillis() + validityInMillis))
                .compact()
        } catch (ex: WeakKeyException) {
            throw KeyException("Signing key is too weak", ex)
        }
    }

    private fun validateExpiration(claims: Claims) {
        val expiration = claims.expiration
        if (Date().after(expiration)) {
            throw TokenException("Token expired. Expiration: $expiration")
        }
    }

    private fun getAddress(claims: Claims): Address {
        val address = claims[userKey] as? String
            ?: throw TokenException("Token principal claims in invalid format")
        try {
            return objectMapper.readValue(address)
        } catch (ex: MissingKotlinParameterException) {
            throw TokenException("Could not extract address from JWT token for key: $userKey", ex)
        }
    }
}
