package com.ampnet.core.jwt

import com.ampnet.core.jwt.exception.TokenException
import com.ampnet.core.jwt.provider.JwtReactiveAuthenticationManager
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import reactor.test.StepVerifier

class JwtReactiveAuthenticationManagerTest : BaseTest() {

    private val authenticationManager = JwtReactiveAuthenticationManager(publicKey)

    @Test
    fun mustReturnJwtAuthToken() {
        val jwtToken = JwtTokenUtils.encodeToken(address, privateKey, validityInMillis)
        val incomingAuth = JwtAuthToken(jwtToken)
        assertFalse(incomingAuth.isAuthenticated)

        val authentication = authenticationManager.authenticate(incomingAuth).block()
            ?: Assertions.fail("Missing authentication")
        val addressAuthenticated = authentication.principal as? String
            ?: Assertions.fail("Missing address in JwtAuthToken")
        assertEquals(addressAuthenticated, address)
    }

    @Test
    fun mustThrowExceptionForInvalidJwtToken() {
        val incomingAuth = UsernamePasswordAuthenticationToken("principal", "credentials")
        StepVerifier.create(authenticationManager.authenticate(incomingAuth))
            .expectError(TokenException::class.java)
            .verify()
    }
}
