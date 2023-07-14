package com.hyunseo.userservice.service

import com.auth0.jwt.interfaces.DecodedJWT
import com.hyunseo.userservice.config.JWTProperties
import com.hyunseo.userservice.domain.entity.User
import com.hyunseo.userservice.domain.repository.userRepository
import com.hyunseo.userservice.exception.InvalidJwtTokenException
import com.hyunseo.userservice.exception.PasswordNotMatchedException
import com.hyunseo.userservice.exception.UserExistsException
import com.hyunseo.userservice.exception.UserNotFoundException
import com.hyunseo.userservice.model.SignInRequest
import com.hyunseo.userservice.model.SignInResponse
import com.hyunseo.userservice.model.SignUpRequest
import com.hyunseo.userservice.utils.ByCryptUtils
import com.hyunseo.userservice.utils.JWTClaim
import com.hyunseo.userservice.utils.JWTUtils
import org.springframework.stereotype.Service
import java.time.Duration

/**
 * @author ihyeonseo
 */
@Service
class UserService(
    private val userRepository: userRepository,
    private val jwtProperties: JWTProperties,
    private val cacheManager: CoroutineCacheManager<User>,
) {

    companion object {
        // 캐시에서 토큰의 ttl을 1분으로 설정하기 위해 상수를 정의한다.
        private val CACHE_TTL = Duration.ofMinutes(1)
    }

    suspend fun signUp(signUpRequest: SignUpRequest) {
        with(signUpRequest) {
            userRepository.findByEmail(email)?.let {
                throw UserExistsException()
            }

            // 회원가입을 진행시키면 된다.
            val user = User(
                email = email,
                password = ByCryptUtils.hash(password),
                username = username,
            )

            userRepository.save(user)
        }
    }

    suspend fun signIn(signInRequest: SignInRequest): SignInResponse {
        // repo에서 이메일로 유저를 찾지 못하면 UserNotFoundException을 내려준다.
        return with(userRepository.findByEmail(signInRequest.email) ?: throw UserNotFoundException()) {
            val verified = ByCryptUtils.verify(signInRequest.password, password)

            // 비밀번호를 검증하지 못햄ㅆ다면 PasswordNotMatchedException을 내려준다.
            if (!verified) {
                throw PasswordNotMatchedException()
            }

            // payload설정을 진행한다.
            val jwtClaim = JWTClaim(
                userId = id!!,
                email = email,
                profileUrl = profileUrl,
                username = username,
            )

            // 정상적인 절차를 거치면 JWT 토큰을 발급받는다.
            val token = JWTUtils.createToken(jwtClaim, jwtProperties)

            cacheManager.awaitPut(key = token, value = this, ttl = CACHE_TTL)

            SignInResponse(
                email = email,
                username = username,
                token = token,
            )
        }
    }

    suspend fun logout(token: String) {
        cacheManager.awaitEvict(token)
    }

    suspend fun getByToken(token: String): User {
        val cachedUser = cacheManager.awaitGetOrPush(key = token) {
            // 캐시가 유효하지 않은 경우 동작
            val decodedJWT: DecodedJWT = JWTUtils.decode(
                token,
                jwtProperties.secret,
                jwtProperties.issuer
            )

            // userId를 노출하지 않기 위해 token의 claims에서 불러와서 corotuineRepository에서 조회해와야한다.
            val userId: Long = decodedJWT.claims["userId"]?.asLong() ?: throw InvalidJwtTokenException()
            get(userId)
        }

        return cachedUser
    }

    suspend fun get(userId: Long): User {
        return userRepository.findById(userId) ?:throw UserNotFoundException()
    }

    suspend fun edit(
        token: String,
        username: String,
        profileUrl: String?
    ): User {
        val user: User = getByToken(token)
        val newUser = user.copy(username = username, profileUrl = profileUrl ?: user.profileUrl)

        return userRepository.save(newUser).also {
            cacheManager.awaitPut(key = token, value = it, ttl = CACHE_TTL)
        }
    }
}