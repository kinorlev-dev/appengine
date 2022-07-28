package com.kinorlev.appengine.v1.controllers


import com.fasterxml.jackson.databind.ObjectMapper
import com.kinorlev.appengine.extension.EXT_firebaseToken
import com.kinorlev.appengine.v1.ServiceLogger
import com.kinorlev.appengine.v1.models.CalculatePwfBody
import com.kinorlev.appengine.v1.models.CalculatePwfResponse
import com.kinorlev.appengine.v1.usecases.controllersusecases.CalculatePwsUseCase
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import io.swagger.annotations.Tag

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@Api(description = "Api for registered firebase users (anonymous + verified)")
@RestController
class AnonymousController {

    @Autowired
    lateinit var mapper: ObjectMapper

    @Autowired
    lateinit var calculatePwsUseCase: CalculatePwsUseCase


    data class MyNameResponse(val myName: String)

    @ApiOperation(value = "For debug,verify token mechanism works")
    @ApiResponses(ApiResponse(code = 401, message = "Strange, the call should be stopped at filter"))
    @GetMapping("anonymous/sayMyName")
    fun sayMyName(authentication: Authentication): MyNameResponse {
        val logger = ServiceLogger(mapper)
        logger.start("*********** sayMyName ***********")
        val firebaseToken = authentication.EXT_firebaseToken(logger)
        var nameOr = firebaseToken.name
        if (nameOr.isNullOrEmpty()) {
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