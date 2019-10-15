package com.ampnet.core.jwt

import org.junit.jupiter.api.Assertions.assertEquals
import java.util.UUID

abstract class BaseTest {

    protected val signingKey = "afds89saydf98aysd9fhas9fhs9a8dhf89sadhf89ahf8a9shfashf98adfu98asuf98ausf.,v,."
    protected val validityInMillis = 50 * 60L
    protected val userPrincipal = UserPrincipal(
        UUID.fromString("370a248e-cbd0-4af8-b1d5-0851a3d654db"),
        "e@mail.com",
        "Name",
        setOf("Auth"),
        true
    )

    protected fun assertUserPrincipal(decodedUserPrincipal: UserPrincipal) {
        assertEquals(userPrincipal.uuid.toString(), decodedUserPrincipal.uuid.toString())
        assertEquals(userPrincipal.email, decodedUserPrincipal.email)
        assertEquals(userPrincipal.name, decodedUserPrincipal.name)
        assertEquals(userPrincipal.authorities.toString(), decodedUserPrincipal.authorities.toString())
        assertEquals(userPrincipal.enabled, decodedUserPrincipal.enabled)
    }
}
