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

/**
 * Class for authenticating user via JWT.
 *
 * @property publicKey public key as String in DER format for verifying JWT signature.
 */
class JwtAuthenticationProvider(private val publicKey: String) : AuthenticationProvider {

    override fun authenticate(authentication: Authentication): Authentication {
        val token = authentication.credentials
        if (token is String) {
            try {
                val address = JwtTokenUtils.decodeToken(token, publicKey)
                return JwtAuthToken(token, address)
            } catch (ex: TokenException) {
                logger.info("Invalid JWT", ex)
                SecurityContextHolder.clearContext()
                throw BadCredentialsException("Invalid JWT")
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
                val address = JwtTokenUtils.decodeToken(token, publicKey)
                JwtAuthToken(token, address)
            }
    }
}
