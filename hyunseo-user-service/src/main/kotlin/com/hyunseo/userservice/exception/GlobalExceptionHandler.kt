package com.hyunseo.userservice.exception

import com.fasterxml.jackson.databind.ObjectMapper
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.mono
import mu.KotlinLogging
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.MediaType
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

/**
 * @author ihyeonseo
 *
 */

@Configuration
class GlobalExceptionHandler(private val objectMapper: ObjectMapper): ErrorWebExceptionHandler {
    private val logger = KotlinLogging.logger {}

    override fun handle(exchange: ServerWebExchange, ex: Throwable): Mono<Void> = mono {
        logger.error { ex.message }

        val errorResponse = if (ex is ServerException) {
            ErrorResponse(code = ex.code, message = ex.message)
        } else {
            // 우리가 정의하지 않은 ServerException 이다.
            ErrorResponse(code = 500, message = "Internal Server Error")
        }

        // Exception이 발생하더라도 statusCode는 OK로 내려주고 응답으로 어떤 Exception이 발생했는지 내려준다.
        with(exchange.response) {
            statusCode = HttpStatus.OK
            headers.contentType = MediaType.APPLICATION_JSON

            val dataBuffer = bufferFactory().wrap(
                objectMapper.writeValueAsBytes(errorResponse)
            )

            writeWith(dataBuffer.toMono()).awaitSingle()
        }
    }
}