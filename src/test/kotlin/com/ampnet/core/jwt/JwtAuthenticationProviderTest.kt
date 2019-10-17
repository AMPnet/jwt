package com.ampnet.core.jwt

import com.ampnet.core.jwt.provider.JwtAuthenticationProvider
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Assertions.fail
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UsernameNotFoundException

class JwtAuthenticationProviderTest : BaseTest() {

    private val authenticationProvider = JwtAuthenticationProvider(signingKey)

    @Test
    fun mustReturnJwtAuthToken() {
        val jwtToken = JwtTokenUtils.encodeToken(userPrincipal, signingKey, validityInMillis)
        val incomingAuth = JwtAuthToken(jwtToken)
        assertFalse(incomingAuth.isAuthenticated)

        val authentication = authenticationProvider.authenticate(incomingAuth)
        assertTrue(authentication.isAuthenticated)

        val userPrincipal = authentication.principal as? UserPrincipal
            ?: fail("Missing principal in JwtAuthToken")
        assertUserPrincipal(userPrincipal)
    }

    @Test
    fun mustThrowExceptionIfJwtTokenIsMissing() {
        val incomingAuth = UsernamePasswordAuthenticationToken(null, null)
        assertThrows<UsernameNotFoundException> {
            authenticationProvider.authenticate(incomingAuth)
        }
    }

    @Test
    fun mustThrowExceptionForInvalidJwtToken() {
        val incomingAuth = UsernamePasswordAuthenticationToken("principal", "credentials")
        assertThrows<BadCredentialsException> {
            authenticationProvider.authenticate(incomingAuth)
        }
    }
}