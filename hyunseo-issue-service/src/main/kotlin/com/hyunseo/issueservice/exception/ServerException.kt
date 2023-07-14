package com.hyunseo.issueservice.exception

import java.lang.RuntimeException

/**
 * @author ihyeonseo
 */
sealed class ServerException(
    val code: Int,
    override val message: String,
): RuntimeException(message)

// 데이터 존재하지 않았을 때 발생시키는 예외 클래스
data class NotFoundException(
    override val message: String,
): ServerException(404, message)

data class UnauthorizedException(
    override val message: String = "인증 정보가 잘못되었습니다",
): ServerException(401, message)