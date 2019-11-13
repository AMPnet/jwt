package com.ampnet.core.jwt.filter

import com.ampnet.core.jwt.JwtAuthToken
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter

/**
 * Filter class for extracting JWT from Authorization header
 * and setting it to Spring security context authentication.
 */
class JwtAuthenticationFilter : OncePerRequestFilter() {

    private val headerName = "Authorization"
    private val tokenPrefix = "Bearer "

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        val header = request.getHeader(headerName)
        if (header != null && header.startsWith(tokenPrefix)) {
            val authToken = header.replace(tokenPrefix, "")
            SecurityContextHolder.getContext().authentication = JwtAuthToken(authToken)
        }
        chain.doFilter(request, response)
    }
}
