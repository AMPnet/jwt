package com.ampnet.core.jwt.filter

import com.ampnet.core.jwt.UserPrincipal
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import mu.KLogging
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter

/**
 * Filter class for eliminating users with disabled account.
 * User disabled data is contained in UserPrincipal filed enabled.
 */
class DisabledProfileFilter : OncePerRequestFilter() {

    companion object : KLogging()

    private val disabledProfileMessage = "Disabled user profile"

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
