package com.ampnet.core.jwt

import com.ampnet.core.jwt.exception.KeyException
import com.ampnet.core.jwt.exception.TokenException
import io.jsonwebtoken.Claims
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.WeakKeyException
import java.io.Serializable
import java.util.Date

/**
 * This class provides static methods for encoding
 * and decoding of JWT.
 * For JWT specification see: <a href="http://www.ietf.org/rfc/rfc7519.txt">RCF 7519</a>
 */
object JwtTokenUtils : Serializable {

    private const val addressKey = "address"
    private const val jwtSubject = "AMPnet"
    private val rsaKeyDecoder = RsaKeyDecoder()

    /**
     * Decode address from JWT.
     *
     * @param token encoded JWT.
     * @param publicKey public key as String in DER format for verifying JWT signature.
     * @return decoded address from JWT.
     * @exception KeyException if the [publicKey] is invalid format.
     * @exception TokenException if the [token] is not valid.
     */
    @Throws(KeyException::class, TokenException::class)
    fun decodeToken(token: String, publicKey: String): String {
        val decodedPublicKey = rsaKeyDecoder.getPublicKey(publicKey)
        try {
            val jwtParser = Jwts.parserBuilder()
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
     * Encode address to JWT.
     *
     * @param address address to encode to JWT.
     * @param privateKey private key as String in DER format  to sign JWT.
     * @param validityInMillis validity of JWT in milliseconds.
     * @return encoded JWT token.
     * @exception KeyException if the [privateKey] is invalid format or too weak.
     */
    @Throws(KeyException::class, TokenException::class)
    fun encodeToken(address: String, privateKey: String, validityInMillis: Long): String {
        val decodedPrivateKey = rsaKeyDecoder.getPrivateKey(privateKey)
        try {
            return Jwts.builder()
                .setSubject(jwtSubject)
                .claim(addressKey, address)
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

    private fun getAddress(claims: Claims): String = claims[addressKey] as? String
        ?: throw TokenException("Token principal claims in invalid format")
}
