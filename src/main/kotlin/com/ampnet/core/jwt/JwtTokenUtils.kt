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
import io.jsonwebtoken.io.JacksonDeserializer
import io.jsonwebtoken.io.JacksonSerializer
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
    private val rsaKeyDecoder = RsaKeyDecoder()
    private val objectMapper: ObjectMapper by lazy {
        val mapper = ObjectMapper()
        mapper.propertyNamingStrategy = PropertyNamingStrategy.SNAKE_CASE
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        mapper.registerModule(KotlinModule())
    }

    /**
     * Decode UserPrincipal data from JWT.
     *
     * @param token encoded JWT.
     * @param publicKey public key as String in DER format for verifying JWT signature.
     * @return decoded UserPrincipal data from JWT.
     * @exception KeyException if the [publicKey] is invalid format.
     * @exception TokenException if the [token] is not valid.
     */
    @Throws(KeyException::class, TokenException::class)
    fun decodeToken(token: String, publicKey: String): UserPrincipal {
        val decodedPublicKey = rsaKeyDecoder.getPublicKey(publicKey)
        try {
            val jwtParser = Jwts.parser()
                .deserializeJsonWith(JacksonDeserializer(objectMapper))
                .setSigningKey(decodedPublicKey)
            val claimsJws = jwtParser.parseClaimsJws(token)
            val claims = claimsJws.body
            validateExpiration(claims)
            return getUserPrincipal(claims)
        } catch (ex: JwtException) {
            throw TokenException("Could not validate JWT token", ex)
        }
    }

    /**
     * Encode UserPrincipal data to JWT.
     *
     * @param userPrincipal user principal data to encode to JWT.
     * @param privateKey private key as String in DER format  to sign JWT.
     * @param validityInMillis validity of JWT in milliseconds.
     * @return encoded JWT token.
     * @exception KeyException if the [privateKey] is invalid format.
     * @exception TokenException if the [privateKey] is too weak.
     */
    @Throws(KeyException::class, TokenException::class)
    fun encodeToken(userPrincipal: UserPrincipal, privateKey: String, validityInMillis: Long): String {
        val decodedPrivateKey = rsaKeyDecoder.getPrivateKey(privateKey)
        try {
            return Jwts.builder()
                .serializeToJsonWith(JacksonSerializer(objectMapper))
                .setSubject(userPrincipal.email)
                .claim(userKey, objectMapper.writeValueAsString(userPrincipal))
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

    private fun getUserPrincipal(claims: Claims): UserPrincipal {
        val principalClaims = claims[userKey] as? String
                ?: throw TokenException("Token principal claims in invalid format")
        try {
            return objectMapper.readValue(principalClaims)
        } catch (ex: MissingKotlinParameterException) {
            throw TokenException("Could not extract user principal from JWT token for key: $userKey", ex)
        }
    }
}
