package com.ampnet.core.jwt.provider

import com.ampnet.core.jwt.JwtAuthToken
import com.ampnet.core.jwt.JwtTokenUtils
import com.ampnet.core.jwt.exception.TokenException
import mu.KotlinLogging
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UsernameNotFoundException
import reactor.core.publisher.Mono

private val logger = KotlinLogging.logger {}
private const val missingJwtToken = "Authentication is missing JWT token!"

class JwtAuthenticationProvider(private val signingKey: String) : AuthenticationProvider {

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
        throw UsernameNotFoundException(missingJwtToken)
    }

    override fun supports(authentication: Class<*>): Boolean = authentication == JwtAuthToken::class.java
}

class JwtReactiveAuthenticationManager(private val publicKey: String) : ReactiveAuthenticationManager {
    override fun authenticate(authentication: Authentication): Mono<Authentication> {
        return Mono.just(authentication)
            .map { it.credentials as String }
            .switchIfEmpty(Mono.error(UsernameNotFoundException(missingJwtToken)))
            .map { token ->
                val userPrincipal = JwtTokenUtils.decodeToken(token, publicKey)
                JwtAuthToken(token, userPrincipal)
            }
    }
}
