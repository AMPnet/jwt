package com.ampnet.core.jwt.provider

import com.ampnet.core.jwt.JwtAuthToken
import com.ampnet.core.jwt.JwtTokenUtils
import com.ampnet.core.jwt.exception.TokenException
import mu.KLogging
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UsernameNotFoundException

class JwtAuthenticationProvider(private val signingKey: String) : AuthenticationProvider {

    companion object : KLogging()

    override fun authenticate(authentication: Authentication): Authentication {
        val token = authentication.credentials
        if (token is String) {
            try {
                val userPrincipal = JwtTokenUtils.decodeToken(token, signingKey)
                return JwtAuthToken(token, userPrincipal)
            } catch (ex: TokenException) {
                logger.info("Invalid JWT token", ex)
                SecurityContextHolder.clearContext()
                throw BadCredentialsException("Invalid JWT token")
            }
        }
        logger.info { "Missing Authentication credentials" }
        throw UsernameNotFoundException("Authentication is missing JWT token!")
    }

    override fun supports(authentication: Class<*>): Boolean = authentication == JwtAuthToken::class.java
}
