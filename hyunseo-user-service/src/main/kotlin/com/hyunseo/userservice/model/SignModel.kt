package com.hyunseo.userservice.model


data class SignUpRequest(
    val email: String,
    val password: String,
    val username: String,
)

data class SignInRequest(
    val email: String,
    val password: String,
)

/**
 * controller러 부터 로그인되었을 때 응답받을 response값
 */
data class SignInResponse(
    val email: String,
    val username: String,
    val token: String,
)