package com.hyunseo.issueservice.exception

import com.auth0.jwt.exceptions.TokenExpiredException
import mu.KotlinLogging
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

/**
 * @author ihyeonseo
 */

@RestControllerAdvice
class GlobalExceptionHandler {
    private val logger = KotlinLogging.logger { }

    @ExceptionHandler(ServerException::class)
    fun handleServerException(ex: ServerException): ErrorResponse {
        logger.error { ex.message }
        return ErrorResponse(code = ex.code, message = ex.message)
    }

    // TokenExpiredException 우리가 지정한 서버 익셉션과즌 전혀 연관이 없기 때문에 분리해주었음
    @ExceptionHandler(TokenExpiredException::class)
    fun handleTokenExpiredException(ex: TokenExpiredException): ErrorResponse {
        logger.error { ex.message }
        return ErrorResponse(code = 401, message = "Token Expired Error")
    }

    @ExceptionHandler(Exception::class)
    fun handleException(ex: Exception): ErrorResponse {
        logger.error { ex.message }
        return ErrorResponse(code = 500, message = "Internal Server Error")
    }
}