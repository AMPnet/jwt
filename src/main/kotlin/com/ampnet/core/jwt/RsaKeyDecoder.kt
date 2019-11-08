package com.ampnet.core.jwt

import com.ampnet.core.jwt.exception.KeyException
import java.security.KeyFactory
import java.security.PrivateKey
import java.security.PublicKey
import java.security.spec.InvalidKeySpecException
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.Base64

internal class RsaKeyDecoder {

    private val beginPublicKey = "-----BEGIN PUBLIC KEY-----"
    private val endPublicKey = "-----END PUBLIC KEY-----"
    private val beginPrivateKey = "-----BEGIN PRIVATE KEY-----"
    private val endPrivateKey = "-----END PRIVATE KEY-----"
    private val emptyString = ""
    private val keyFactory = KeyFactory.getInstance("RSA")

    fun getPublicKey(publicKey: String): PublicKey {
        try {
            val pureKey = publicKey
                .replace(beginPublicKey, emptyString)
                .replace(endPublicKey, emptyString)
                .replace("\\s".toRegex(), emptyString)
            val decodedKey = Base64.getDecoder().decode(pureKey)
            return keyFactory.generatePublic(X509EncodedKeySpec(decodedKey))
        } catch (ex: IllegalArgumentException) {
            throw KeyException("Cannot decode public key", ex)
        } catch (ex: InvalidKeySpecException) {
            throw KeyException("Cannot generate public key", ex)
        }
    }

    fun getPrivateKey(privateKey: String): PrivateKey {
        try {
            val pureKey = privateKey
                .replace(beginPrivateKey, emptyString)
                .replace(endPrivateKey, emptyString)
                .replace("\\s".toRegex(), emptyString)
            val decodedKey = Base64.getDecoder().decode(pureKey)
            return keyFactory.generatePrivate(PKCS8EncodedKeySpec(decodedKey))
        } catch (ex: IllegalArgumentException) {
            throw KeyException("Cannot decode private key", ex)
        } catch (ex: InvalidKeySpecException) {
            throw KeyException("Cannot generate private key", ex)
        }
    }
}
