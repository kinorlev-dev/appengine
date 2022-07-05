package com.kinorlev.appengine.v1.controllers


import com.fasterxml.jackson.databind.ObjectMapper
import com.kinorlev.appengine.extension.EXT_firebaseToken
import com.kinorlev.appengine.v1.ServiceLogger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class AnonymousController {

    @Autowired
    lateinit var mapper: ObjectMapper

    @PostMapping("anonymous/hello")
    fun generatePayPage(
        authentication: Authentication,
        @RequestBody body: HelloWorldMessage
    ): HelloWorldMessage {
        val logger = ServiceLogger(mapper)
        logger.start("hello Anon")
        val firebaseToken = authentication.EXT_firebaseToken(logger)
        logger.end("hello anon")
        println(logger.toString())
        return HelloWorldMessage()
    }

    data class MyNameResponse(val myName: String)

    @GetMapping("anonymous/sayMyName")
    fun sayMyName(authentication: Authentication): MyNameResponse {
        val logger = ServiceLogger(mapper)
        logger.start("*********** sayMyName ***********")
        val firebaseToken = authentication.EXT_firebaseToken(logger)
        var nameOr = firebaseToken.name
        if (nameOr.isEmpty()) {
            nameOr = if (firebaseToken.isEmailVerified) {
                firebaseToken.email
            } else {
                "user anonymous but his uid is ${firebaseToken.uid}"
            }
        }
        logger.end("*********** sayMyName ***********")
        println(logger.toString())
        return MyNameResponse(nameOr)
    }
}