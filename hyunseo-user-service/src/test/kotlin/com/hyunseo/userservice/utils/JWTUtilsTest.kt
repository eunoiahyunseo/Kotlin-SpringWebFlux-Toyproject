package com.hyunseo.userservice.utils

import com.auth0.jwt.interfaces.DecodedJWT
import com.hyunseo.userservice.config.JWTProperties
import mu.KotlinLogging
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

/**
 * @author ihyeonseo
 */
class JWTUtilsTest {
    private val logger = KotlinLogging.logger {}

    @Test
    fun createTokenTest() {
        val jwtClaim = JWTClaim(
            userId = 1,
            email = "dev@gmail.com",
            profileUrl = "profile.jpg",
            username = "개발자",
        )

        val properties = JWTProperties(
            issuer = "toy-project",
            subject = "auth",
            expiresTime = 3600,
            secret = "my-secret",
        )

        val token = JWTUtils.createToken(jwtClaim, properties)

        // 잘 발급되었다면 token이 null이 되면 안된다.
        assertNotNull(token)

        logger.info { "token: $token" }
    }

    @Test
    fun decodeTest() {
        val jwtClaim = JWTClaim(
            userId = 1,
            email = "dev@gmail.com",
            profileUrl = "profile.jpg",
            username = "개발자",
        )

        val properties = JWTProperties(
            issuer = "toy-project",
            subject = "auth",
            expiresTime = 3600,
            secret = "my-secret",
        )

        val token = JWTUtils.createToken(jwtClaim, properties)

        val decode: DecodedJWT? = JWTUtils.decode(
            token,
            secret = properties.secret,
            issuer = properties.issuer
        )

        with(decode!!) {
            logger.info { "claim : $claims" }

            val userId = claims["userId"]!!.asLong()
            assertEquals(userId, jwtClaim.userId)

            val email = claims["email"]!!.asString()
            assertEquals(email, jwtClaim.email)

            val profileUrl = claims["profileUrl"]!!.asString()
            assertEquals(profileUrl, jwtClaim.profileUrl)

            val username = claims["username"]!!.asString()
            assertEquals(username, jwtClaim.username)
        }
    }
}