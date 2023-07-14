package com.hyunseo.userservice.model

import com.hyunseo.userservice.domain.entity.User
import java.time.LocalDateTime

data class UserEditRequest(
    val username: String,
)

/**
 * User에 대한 정보에 대한 DTO를 정의한다.
 */
data class MeResponse(
    val id: Long,
    val profileUrl: String?,
    val username: String,
    val email: String,
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?,
) {
    companion object {
        operator fun invoke(user: User) = with(user) {
            MeResponse(
                id = id!!,
                profileUrl = if (profileUrl.isNullOrEmpty()) null else "https://localhost:9090/images/$profileUrl",
                username = username,
                email = email,
                createdAt = createdAt,
                updatedAt = updatedAt
            )
        }
    }
}