package com.ampnet.core.jwt

import com.ampnet.core.jwt.exception.KeyException
import com.ampnet.core.jwt.exception.TokenException
import junit.framework.TestCase.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class JwtTokenUtilsTest : BaseTest() {

    @Test
    fun mustBeAbleToEncodeAndDecodeJwtToken() {
        val jwtToken = JwtTokenUtils.encodeToken(address, privateKey, validityInMillis)

        val decodedAddress = JwtTokenUtils.decodeToken(jwtToken, publicKey)
        assertEquals(decodedAddress, address)
    }

    @Test
    fun mustThrowExceptionForEncodingWithExpiredToken() {
        val jwtToken = "eyJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJlQG1haWwuY29tIiwidXNlciI6IntcInV1aWRcIjpcIjM3MGEyNDhlL" +
            "WNiZDAtNGFmOC1iMWQ1LTA4NTFhM2Q2NTRkYlwiLFwiZW1haWxcIjpcImVAbWFpbC5jb21cIixcIm5hbWVcIjpcIk5hbWVcIixcImF1d" +
            "Ghvcml0aWVzXCI6W1wiQXV0aFwiXSxcImVuYWJsZWRcIjp0cnVlLFwidmVyaWZpZWRcIjp0cnVlfSIsImlhdCI6MTU3MzIxNTAxNCwiZ" +
            "XhwIjoxNTczMjE1MDQ0fQ.hBKkMa7V_jkClLTpy4EByREirKCXfmOi3jWuIhzxOzgx2FpM5i57fjkH8dSHWRb9c2H7AUL-Pg59YQSyTC" +
            "8p1aG-E8V5diDv_2vxudYxZ9uTgc8brM6UJp38csLBGBDSYGMiXpcUvSuXq85weg00YMnBM5J2kF07uEbZuq7kjVbW9Dvue9uT2qacrn" +
            "ZJm5SDhdKoybs9NKvBbOTKCsF4u9TS4IuI7y6lmOPo9w9tUA3ngU46J98iraFOo6ZhASBMhDYRHtepGCHyUJlZPn2dm9TFO4Er8_QTEb" +
            "ZXtypRrmy6TxluCM_8SWm6HaULVM0KEBVOkkeu2gb_JURoQaIs9A"
        assertThrows<TokenException> {
            JwtTokenUtils.decodeToken(jwtToken, publicKey)
        }
    }

    @Test
    fun mustThrowExceptionForDecodingWithWrongPublicKey() {
        val jwtToken = JwtTokenUtils.encodeToken(address, privateKey, validityInMillis)
        val wrongPublicKey = "-----BEGIN PUBLIC KEY-----\n" +
            "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAs8VzstcVP4YYjfy3dJTr\n" +
            "0fH3L6gXVDrR1TP+3Vx3oUhm5LomHUEcDEKa3ilL0Xat/wjSdd2CHqLv+9uEesV+\n" +
            "cHzODqQNJM70DQb1BDDmEjKAjscvKxWfOUo8sMBvikQZMAKsiplxg47ZpkgPD9bF\n" +
            "QcOppEXzWI6GifoakjV6c4qoxCKQ56ytkjUMzXvXHdlgNa29FJIHuMeFTyFpCS0h\n" +
            "FDM6M5r+onl3rGWgGKQte0YHcxQ/NxxrRX3MMLXtQtb3/uwjaUVJfA+a3Aj+LJ4f\n" +
            "oJtLBwDr1QVXwgvMVjzSj8woWq8Q6FuSJdPp/Fuzp3g2b9ZBwamAuZuT9krutwoA\n" +
            "/wIDAQAB\n" +
            "-----END PUBLIC KEY-----"
        assertThrows<TokenException> {
            JwtTokenUtils.decodeToken(jwtToken, wrongPublicKey)
        }
    }

    @Test
    fun mustThrowExceptionForInvalidPublicKey() {
        val jwtToken = JwtTokenUtils.encodeToken(address, privateKey, validityInMillis)
        val invalidPublicKey = "asdfIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBC"
        assertThrows<KeyException> {
            JwtTokenUtils.decodeToken(jwtToken, invalidPublicKey)
        }
    }

    @Test
    fun mustThrowExceptionForInvalidPrivateKey() {
        val invalidPrivateKey = "asdfIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBC"
        assertThrows<KeyException> {
            JwtTokenUtils.encodeToken(address, invalidPrivateKey, validityInMillis)
        }
    }
}
