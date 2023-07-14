package com.hyunseo.userservice.service

import org.springframework.stereotype.Component
import java.time.Duration
import java.time.Instant
import java.util.concurrent.ConcurrentHashMap

/**
 * @author ihyeonseo
 *
 * Redis 같은 걸 사용하지 않고 간단히 서버에서 ConcurrentHash를 사용해서
 * token 정보를 저장하기 위해 만든 CoroutineCacheManager이다.
 */


@Component
class CoroutineCacheManager<T> {

    // 스레드에 안전한 hashMap -> lock도 안걸면서 부분적으로 동시성을 제공
    private val localCache = ConcurrentHashMap<String, CacheWrapper<T>>()

    // 해당 ttl 객체 만큼만 cache 객체가 살아있도록 만들 것이다.
    suspend fun awaitPut(key: String, value: T, ttl: Duration) {
        localCache[key] = CacheWrapper(
            cached = value, Instant.now().plusMillis(ttl.toMillis()))
    }

    suspend fun awaitEvict(key: String) {
        localCache.remove(key)
    }

    suspend fun awaitGetOrPush(
        key: String,
        ttl: Duration?= Duration.ofMinutes(5), // 기본 ttl을 5분으로 설정한다.
        supplier: suspend () -> T,
    ) : T {
        val now = Instant.now()
        val cacheWrapper = localCache[key]

        val cached =
            if (cacheWrapper == null) {
                CacheWrapper(cached = supplier(),
                    ttl = now.plusMillis(ttl!!.toMillis())
                ).also {
                    localCache[key] = it
                }
            } else if (now.isAfter(cacheWrapper.ttl)) {
                localCache.remove(key)
                CacheWrapper(
                    cached = supplier(),
                    ttl = now.plusMillis(ttl!!.toMillis())
                ).also {
                    localCache[key] = it
                }
            } else {
                cacheWrapper
            }

        checkNotNull(cached.cached)
        return cached.cached
    }

    data class CacheWrapper<T>(val cached: T, val ttl: Instant)
}