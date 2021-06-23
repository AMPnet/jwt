package com.ampnet.core.jwt

import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority

class JwtAuthToken(private val token: String, private val address: String? = null) : Authentication {

    override fun setAuthenticated(isAuthenticated: Boolean) {
        // not needed
    }

    override fun getName(): String? = address

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        TODO("Not yet implemented")
    }

    override fun getCredentials(): Any = token

    override fun getDetails(): Any {
        TODO("Not yet implemented")
    }

    override fun getPrincipal(): Any? = address

    override fun isAuthenticated(): Boolean = address != null
}
