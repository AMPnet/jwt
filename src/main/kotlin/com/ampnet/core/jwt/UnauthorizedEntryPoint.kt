package com.ampnet.core.jwt

import java.io.Serializable
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint

/**
 * Entry point to set all unauthenticated requests as unauthorized.
 */
class UnauthorizedEntryPoint : AuthenticationEntryPoint, Serializable {

    override fun commence(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        authException: AuthenticationException?
    ) {
        response?.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized entry point")
    }
}
