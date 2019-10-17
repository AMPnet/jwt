package com.ampnet.core.jwt

import com.ampnet.core.jwt.exception.SigningKeyException
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
import io.jsonwebtoken.security.Keys
import io.jsonwebtoken.security.WeakKeyException
import java.io.Serializable
import java.util.Date
import javax.crypto.SecretKey

object JwtTokenUtils : Serializable {

    private const val userKey = "user"
    private val objectMapper: ObjectMapper by lazy {
        val mapper = ObjectMapper()
        mapper.propertyNamingStrategy = PropertyNamingStrategy.SNAKE_CASE
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        mapper.registerModule(KotlinModule())
    }

    @Throws(TokenException::class)
    fun decodeToken(token: String, signingKey: String): UserPrincipal {
        try {
            val key: SecretKey = Keys.hmacShaKeyFor(signingKey.toByteArray())
            val jwtParser = Jwts.parser()
                    .deserializeJsonWith(JacksonDeserializer(objectMapper))
                    .setSigningKey(key)
            val claimsJws = jwtParser.parseClaimsJws(token)
            val claims = claimsJws.body
            validateExpiration(claims)
            return getUserPrincipal(claims)
        } catch (ex: JwtException) {
            throw TokenException("Could not validate JWT token", ex)
        }
    }

    @Throws(SigningKeyException::class)
    fun encodeToken(userPrincipal: UserPrincipal, signingKey: String, validityInMillis: Long): String {
        try {
            val key: SecretKey = Keys.hmacShaKeyFor(signingKey.toByteArray())
            return Jwts.builder()
                .serializeToJsonWith(JacksonSerializer(objectMapper))
                .setSubject(userPrincipal.email)
                .claim(userKey, objectMapper.writeValueAsString(userPrincipal))
                .signWith(key, SignatureAlgorithm.HS256)
                .setIssuedAt(Date())
                .setExpiration(Date(System.currentTimeMillis() + validityInMillis))
                .compact()
        } catch (ex: WeakKeyException) {
            throw SigningKeyException("Signing key is too weak", ex)
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
