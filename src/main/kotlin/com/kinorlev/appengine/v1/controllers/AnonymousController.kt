package com.kinorlev.appengine.v1.controllers


import com.fasterxml.jackson.databind.ObjectMapper
import com.kinorlev.appengine.extension.EXT_firebaseToken
import com.kinorlev.appengine.v1.ServiceLogger
import com.kinorlev.appengine.v1.model.CalculatePwfBody
import com.kinorlev.appengine.v1.model.CalculatePwfResponse
import com.kinorlev.appengine.v1.usecases.controllersusecases.CalculatePwsUseCase
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

    @Autowired
    lateinit var calculatePwsUseCase: CalculatePwsUseCase

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

    @PostMapping("anonymous/calculatePwf")
    fun calculatePwf(authentication: Authentication, @RequestBody body: CalculatePwfBody): CalculatePwfResponse {
        val logger = ServiceLogger(mapper)
        logger.start("*********** calculatePws ***********")
        val response = calculatePwsUseCase.calculate(body)
        logger.end("*********** calculatePws ***********")
        println(logger.toString())
        return response
    }
}