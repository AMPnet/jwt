package com.ampnet.core.jwt

import org.junit.jupiter.api.Assertions.assertEquals
import java.util.UUID

abstract class BaseTest {

    protected val publicKey = "-----BEGIN PUBLIC KEY-----\n" +
        "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAwWEc4LVT1pDI4eawIbSV\n" +
        "Ba1LvNVp87PS25Yyh3O2rmww+T9FpvmIWiQJfZDzKJxTUgFAFXJrurPPjgzdVbFB\n" +
        "qqWt7CmiA3VspgvnNPsd6KFbNNIsZIxlkM6ZOv3qikXZucc196TgJjz9qvXvXgeE\n" +
        "PKaApyzo0m8HHQztkB6q9g5YN5jwcH7YoiqR5PseUgwfoZIuujjB77SaWHEfCyTz\n" +
        "ObUeglipz8+t1mRd1QLQcw6b5lYd5kymn2oCumLsMG4XNtkC8teLcb3FhjhP9n0x\n" +
        "nHo6mEQIfienlRNj02X7m0jiilp+jz2m3P/38gm0dpjRmjrgSlfFGDD9LgrQnEoi\n" +
        "jQIDAQAB\n" +
        "-----END PUBLIC KEY-----"
    protected val privateKey = "-----BEGIN PRIVATE KEY-----\n" +
        "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDBYRzgtVPWkMjh\n" +
        "5rAhtJUFrUu81Wnzs9LbljKHc7aubDD5P0Wm+YhaJAl9kPMonFNSAUAVcmu6s8+O\n" +
        "DN1VsUGqpa3sKaIDdWymC+c0+x3ooVs00ixkjGWQzpk6/eqKRdm5xzX3pOAmPP2q\n" +
        "9e9eB4Q8poCnLOjSbwcdDO2QHqr2Dlg3mPBwftiiKpHk+x5SDB+hki66OMHvtJpY\n" +
        "cR8LJPM5tR6CWKnPz63WZF3VAtBzDpvmVh3mTKafagK6Yuwwbhc22QLy14txvcWG\n" +
        "OE/2fTGcejqYRAh+J6eVE2PTZfubSOKKWn6PPabc//fyCbR2mNGaOuBKV8UYMP0u\n" +
        "CtCcSiKNAgMBAAECggEAdnec7liHnPoj5qihnHR5mvm4XV3Mw9yVP6r7F5oe/yoO\n" +
        "spM9g3Z5Pue23LLxFB74uFNFxB3Nu5oufBKLgdTOqtFHBFfZBg9o2SyPnxh8ESjQ\n" +
        "rEJZqduM2TKIEui9R6pVIQfWmvjpzOMucA6sbsliDJngVQohM9GK5RQncVQoXgYI\n" +
        "0MuG+idcSvoHFIPVJ3fXi12xQ+jVHxaSt8+C6B6RsVGwhEbhTzr+ZkRqkraxblpt\n" +
        "ppoymDL82O3FOA4fK0wKQYs42b0JfSVAE3px0heywMQd5TaJMlesZUppFbJpKnXt\n" +
        "73zVqjJg7RdfNJ/ouK3+x+3ukYFh9L5RkzE2MRsQrQKBgQDn/xgmgINpmO4DelNs\n" +
        "bWI8zjN2xeYdA0nIYENsDQf5WmNzdVqi9PML4ajtZZKyDVujN28sZRZtc1ZmF97/\n" +
        "G+G3advhFPXslmysll1wgGYJkbvDlLjSa7TgLvGZgkMtdCNinJB3aK0Cwpwjl2zg\n" +
        "rtZzYYHgHHBdAOeqxqXUVhVFdwKBgQDVYyoUZRlUQPGcCglMWXuesbINV11OHIPn\n" +
        "1Mavss6BOL/1jRHIdM064DozNbFl2WWj54fSPU0RsoWx2yjluCr8ewaD/1g3oWWI\n" +
        "KNlT87Ffk31nYI7JIndgupSMMUVLl/rfOcHb5n1j7fVXCQI2usQpyKYwBh3P68Xs\n" +
        "rIiTQWJpGwKBgQDGp2LQpT7hzzgP9lE1CT0TgCYFUFRJB6XIVNWm0km/tSxvk0+3\n" +
        "NjRgOiziixVgLZwIWNFznGPl+NtEtLAYlpFi8tx7Ee27Vttbta3xRaEaFJZJVTIl\n" +
        "zGCnjssGfcp0Q3yBVjtGZHWxZV86AtISWuP7DMWfV/rHSJ0XWN5qwgOY5QKBgC31\n" +
        "TKPV3m2yCxeDZdtGzoQwW1vaPCNNqu8seZpv1WApe9aECpd06JrMnlxXLRz1hJmX\n" +
        "jT4vq8CrBXZ38EgBsWhtisq1HSfmH/sCaURWkRY3quOv/TC+tp1jsgi7t+P/GK4p\n" +
        "P2KAMvNp1dRoAU0OTp99MqXXO2aDrj46LrQgefSPAoGALhAPhr11cL1FLA3dgbMV\n" +
        "DQYvpxqiCvB7PrvvlSI0AR6WSXRtEcc2umRRTex534a/dS7d3BPQtn62OHgSiWpk\n" +
        "wfDeq+QcK2jJWofZx7aNZ/uMj8gVY08tN137LYU9CgQ0uHBbWd8zWRdjpeMYR7rb\n" +
        "zjW9ZL//qVd/1XumleYSg3U=\n" +
        "-----END PRIVATE KEY-----"

    protected val validityInMillis = 500 * 60L
    protected val userPrincipal = UserPrincipal(
        UUID.fromString("370a248e-cbd0-4af8-b1d5-0851a3d654db"),
        "e@mail.com",
        "Name",
        setOf("Auth"),
        true,
        true,
        "ampnet-demo"
    )

    protected fun assertUserPrincipal(decodedUserPrincipal: UserPrincipal) {
        assertEquals(userPrincipal.uuid.toString(), decodedUserPrincipal.uuid.toString())
        assertEquals(userPrincipal.email, decodedUserPrincipal.email)
        assertEquals(userPrincipal.name, decodedUserPrincipal.name)
        assertEquals(userPrincipal.authorities.toString(), decodedUserPrincipal.authorities.toString())
        assertEquals(userPrincipal.enabled, decodedUserPrincipal.enabled)
        assertEquals(userPrincipal.verified, decodedUserPrincipal.verified)
        assertEquals(userPrincipal.coop, decodedUserPrincipal.coop)
    }
}
