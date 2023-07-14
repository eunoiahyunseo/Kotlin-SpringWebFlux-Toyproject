package com.hyunseo.userservice.utils

import at.favre.lib.crypto.bcrypt.BCrypt

/**
 * @author ihyeonseo
 */
object ByCryptUtils {

    fun hash(password: String) =
        BCrypt.withDefaults().hashToString(12, password.toCharArray())

    // 사용자로부터 입력받은 평뮨과 DB에 저장된 해시된 비밀번호를 verify한다.
    fun verify(password: String, hashedPassword: String) =
        BCrypt.verifyer().verify(password.toCharArray(), hashedPassword).verified
}