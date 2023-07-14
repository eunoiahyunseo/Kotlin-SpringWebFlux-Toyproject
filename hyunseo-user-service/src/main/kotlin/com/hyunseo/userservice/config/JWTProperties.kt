package com.hyunseo.userservice.config

import org.springframework.boot.context.properties.ConfigurationProperties

/**
 * @author ihyeonseo
 */


@ConfigurationProperties(prefix = "jwt")
data class JWTProperties(
    val issuer: String, // 발행자
    val subject: String, // 주제
    val expiresTime: Long, // 만료 시간
    val secret: String, // 시크릿키
)