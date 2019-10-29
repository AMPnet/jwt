package com.ampnet.core.jwt.filter

import com.ampnet.core.jwt.UserPrincipal
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import mu.KLogging
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter

class UnverifiedProfileFilter : OncePerRequestFilter() {

    companion object : KLogging()

    private val unVerifiedUserMessage = "User did not verified his profile."

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
