package com.kinorlev.appengine.v1.controllers


import com.kinorlev.appengine.v1.models.BaseResponse
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import javax.servlet.http.HttpServletRequest

@RestControllerAdvice
class ExceptionHandlerController {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(Exception::class)
    fun throwGeneralException(req: HttpServletRequest, e: Exception): BaseResponse {
        println("***********************************************")
        println("throwGeneralException ${e.message}")
        val baseResponse = BaseResponse()
        baseResponse.succeed = false
        baseResponse.errorDescription = e.message ?: "error message is null"
        println("***********************************************")

        return baseResponse
    }

    /*@ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(SessionAuthenticationException::class, AuthenticationException::class)
    fun throwSessionAuthenticationException(req: HttpServletRequest, e: Exception): BaseResponse {
        println("***********************************************")
        println("SessionAuthenticationException ${e.message}")
        val baseResponse = BaseResponse()
        baseResponse.succeed = false
        baseResponse.errorDescription = e.message ?: "error message is null"
        println("***********************************************")

        return baseResponse
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(KinorAuthException::class)
    fun throwAuthException(req: HttpServletRequest, e: Exception): BaseResponse {
        println("***********************************************")
        println("throwAuthException ${e.message}")
        val baseResponse = BaseResponse()
        baseResponse.succeed = false
        baseResponse.errorDescription = e.message ?: "error message is null"
        println("***********************************************")

        return baseResponse
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(FirebaseAuthException::class)
    fun throwFirebaseAuthException(req: HttpServletRequest, e: Exception): BaseResponse {
        println("***********************************************")
        println("FirebaseAuthException ${e.message}")
        val baseResponse = BaseResponse()
        baseResponse.succeed = false
        baseResponse.errorDescription = e.message ?: "error message is null"
        println("***********************************************")

        return baseResponse
    }


*/
}
class KinorAuthException(message:String):RuntimeException(message)