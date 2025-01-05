package com.dgu.prompt.blaybus_backend

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class TestController {

    @GetMapping("/test")
    fun testEndpoint(): String {
        return "Hello from Blaybus Backend!"
    }
}