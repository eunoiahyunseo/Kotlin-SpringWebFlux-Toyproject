package com.hyunseo.issueservice.web

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

/**
 * @author ihyeonseo
 */

@Controller
class PageController {

    @GetMapping(value = ["/", "/index"])
    fun index() = "index"

    @GetMapping("/issueapp")
    fun issueApp() = "issueapp"

    @GetMapping("/signup")
    fun signup() = "signup"
}

