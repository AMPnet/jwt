package com.ampnet.core.jwt.filter

import com.ampnet.core.jwt.UserPrincipal
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono

private val logger = KotlinLogging.logger {}
private const val disabledProfileMessage = "Disabled user profile"

/**
 * Filter class for eliminating users with disabled account.
 * User disabled data is contained in UserPrincipal filed enabled.
 */
class DisabledProfileFilter : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authentication = SecurityContextHolder.getContext().authentication
        if (authentication != null && authentication.isAuthenticated) {
            val principal = authentication.principal
            if (principal is UserPrincipal) {
                val path = request.requestURI
                if (principal.enabled.not()) {
                    logger.warn("User: ${principal.uuid} with disabled profile try to reach $path")
                    response.sendError(HttpServletResponse.SC_CONFLICT, disabledProfileMessage)
                    return
                }
            }
        }
        filterChain.doFilter(request, response)
    }
}

class DisabledProfileWebFilter : WebFilter {

    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        return ReactiveSecurityContextHolder.getContext()
            .map {
                logger.debug { "Authentication principal = ${it.authentication.principal}" }
                it.authentication.principal as UserPrincipal
            }
            .handle { userPrincipal, sink ->
                if (userPrincipal.enabled.not()) {
                    logger.warn("User: ${userPrincipal.uuid} with disabled profile try to reach " +
                        "${exchange.request.uri}")
                    sink.error(ResponseStatusException(HttpStatus.CONFLICT, disabledProfileMessage))
                } else {
                    chain.filter(exchange)
                }
            }
    }
}
