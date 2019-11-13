package com.ampnet.core.jwt

import java.util.Collections
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority

class JwtAuthToken(private val token: String, private val userPrincipal: UserPrincipal? = null) : Authentication {

    override fun getAuthorities(): MutableCollection<out GrantedAuthority>? {
        val authorities = userPrincipal?.authorities
            ?.map { SimpleGrantedAuthority(it) }?.toList()
            .orEmpty()
        return Collections.checkedCollection(authorities, SimpleGrantedAuthority::class.java)
    }

    override fun setAuthenticated(isAuthenticated: Boolean) {
        // not needed
    }

    override fun getName(): String? {
        return userPrincipal?.uuid.toString()
    }

    override fun getCredentials(): Any {
        return token
    }

    override fun getPrincipal(): Any? {
        return userPrincipal
    }

    override fun isAuthenticated(): Boolean {
        return userPrincipal != null
    }

    override fun getDetails(): Any? {
        return userPrincipal?.email
    }
}
