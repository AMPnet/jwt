package com.ampnet.core.jwt

import com.ampnet.core.jwt.provider.JwtReactiveAuthenticationManager
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextImpl
import org.springframework.security.web.server.context.ServerSecurityContextRepository
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

class SecurityContextRepository(
    private val authManager: JwtReactiveAuthenticationManager
) : ServerSecurityContextRepository {

    override fun save(exchange: ServerWebExchange?, context: SecurityContext?): Mono<Void> {
        throw UnsupportedOperationException("Not supported yet.")
    }

    override fun load(exchange: ServerWebExchange?): Mono<SecurityContext> {
        exchange
            ?.request
            ?.headers
            ?.getFirst(HttpHeaders.AUTHORIZATION)
            ?.takeIf { it.startsWith("Bearer ") }
            ?.substring(7)
            ?.let { jwt ->
                return authManager.authenticate(JwtAuthToken(jwt))
                    .map { SecurityContextImpl(it) as SecurityContext }
                    .onErrorMap { error ->
                        ResponseStatusException(HttpStatus.UNAUTHORIZED, error.message, error)
                    }
            }
        return Mono.empty()
    }
}
