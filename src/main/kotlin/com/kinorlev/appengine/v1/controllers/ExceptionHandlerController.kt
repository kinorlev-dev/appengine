package com.kinorlev.appengine.v1.controllers


import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import javax.servlet.http.HttpServletRequest

@RestControllerAdvice
class ExceptionHandlerController {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(Exception::class)
    fun throwException(req: HttpServletRequest, e: Exception): Map<String, Any>? {
        println("ExceptionHandlerController ${e.message}")
        val baseResponse = HashMap<String, Any>()
        baseResponse["succeeds"] = false
        baseResponse["message"] = e.message ?: "error message is null"
        return baseResponse
    }

}