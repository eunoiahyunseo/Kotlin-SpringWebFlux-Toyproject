package com.hyunseo.userservice.domain.repository

import com.hyunseo.userservice.domain.entity.User
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

/**
 * @author ihyeonseo
 */
interface userRepository: CoroutineCrudRepository<User, Long> {

    suspend fun findByEmail(email: String): User?
}