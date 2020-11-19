package com.ampnet.core.jwt

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.web.AuthenticationEntryPoint
import java.io.Serializable
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Entry point to set all unauthenticated requests as unauthorized.
 */
class AuthenticationEntryPointExceptionHandler(private val objectMapper: ObjectMapper) :
    AuthenticationEntryPoint, Serializable {

    override fun commence(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        exception: AuthenticationException?
    ) {
        val errorCode = when (exception) {
            is BadCredentialsException -> ErrorCode.JWT_INVALID
            is UsernameNotFoundException -> ErrorCode.JWT_MISSING
            else -> ErrorCode.JWT_FAILED
        }
        val errorResponse = ErrorResponse(
            errorCode.message,
            errorCode.categoryCode + errorCode.specificCode,
            exception?.message.orEmpty()
        )
        response?.status = HttpStatus.BAD_REQUEST.value()
        response?.contentType = MediaType.APPLICATION_JSON_VALUE
        response?.outputStream?.print(objectMapper.writeValueAsString(errorResponse))
    }

    private data class ErrorResponse(val description: String, val errCode: String, val message: String)
    private enum class ErrorCode(val categoryCode: String, var specificCode: String, var message: String) {
        // Authentication: 02
        JWT_INVALID("02", "04", "Invalid JWT"),
        JWT_MISSING("02", "05", "Missing JWT"),
        JWT_FAILED("02", "06", "Failed to register JWT")
    }
}
