package com.ampnet.core.jwt.filter

import com.ampnet.core.jwt.UserPrincipal
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import mu.KLogging
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
private const val unVerifiedUserMessage = "User profile not verified. Please go to User Profile and verify your identity."

/**
 * Filter class for eliminating users with unverified account.
 * User verification data is contained in UserPrincipal filed verified.
 */
class UnverifiedProfileFilter : OncePerRequestFilter() {

    companion object : KLogging()

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
                if (principal.verified.not() && path.contains("/public/").not()) {
                    logger.warn("User: ${principal.uuid} did not verified his profile")
                    response.sendError(HttpServletResponse.SC_CONFLICT, unVerifiedUserMessage)
                    return
                }
            }
        }
        filterChain.doFilter(request, response)
    }
}

class UnverifiedProfileWebFilter : WebFilter {

    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        return ReactiveSecurityContextHolder.getContext()
            .map {
                logger.debug { "Authentication principal = ${it.authentication.principal}" }
                it.authentication.principal as UserPrincipal
            }
            .handle { userPrincipal, sink ->
                if (userPrincipal.verified.not()) {
                    logger.warn("User: ${userPrincipal.uuid} did not verified his profile")
                    sink.error(ResponseStatusException(HttpStatus.CONFLICT, unVerifiedUserMessage))
                } else {
                    chain.filter(exchange)
                }
            }
    }
}
