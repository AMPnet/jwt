package com.ampnet.core.jwt

import com.ampnet.core.jwt.exception.SigningKeyException
import com.ampnet.core.jwt.exception.TokenException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class JwtTokenUtilsTest : BaseTest() {

    @Test
    fun mustBeAbleToEncodeAndDecodeJwtToken() {
        val jwtToken = JwtTokenUtils.encodeToken(userPrincipal, signingKey, validityInMillis)

        val decodedUserPrincipal = JwtTokenUtils.decodeToken(jwtToken, signingKey)
        assertUserPrincipal(decodedUserPrincipal)
    }

    @Test
    fun mustThrowExceptionForEncodingWithExpiredToken() {
        val jwtToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlQG1haWwuY29tIiwidXNlciI6IntcInV1aWRcIjpcIjM3MGEyND" +
            "hlLWNiZDAtNGFmOC1iMWQ1LTA4NTFhM2Q2NTRkYlwiLFwiZW1haWxcIjpcImVAbWFpbC5jb21cIixcIm5hbWVcIjpcIk5hbWVcIix" +
            "cImF1dGhvcml0aWVzXCI6W1wiQXV0aFwiXSxcImVuYWJsZWRcIjp0cnVlfSIsImlhdCI6MTU3MTA2MTc5MSwiZXhwIjoxNTcxMDYx" +
            "Nzk0fQ.QUgu4helNb_L70F9Iw5u_qwwd6waQpOjxHjW9qPXadY"
        assertThrows<TokenException> {
            JwtTokenUtils.decodeToken(jwtToken, signingKey)
        }
    }

    @Test
    fun mustThrowExceptionForDecodingWithInvalidSigningKey() {
        val jwtToken = JwtTokenUtils.encodeToken(userPrincipal, signingKey, validityInMillis)
        assertThrows<TokenException> {
            JwtTokenUtils.decodeToken(jwtToken, "invalid-afds89saydf98aysd9fhas9fhs9a8dhf89sadhf89ahf8a9shfasf")
        }
    }

    @Test
    fun mustThrowExceptionForEncodingWithWeakSigningKey() {
        assertThrows<SigningKeyException> {
            JwtTokenUtils.encodeToken(userPrincipal, "", validityInMillis)
        }
    }
}
