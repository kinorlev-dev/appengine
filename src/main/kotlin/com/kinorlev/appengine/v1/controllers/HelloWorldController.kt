package com.kinorlev.appengine.v1.controllers


import org.springframework.web.bind.annotation.*

@RestController
class HelloWorldController {

    @GetMapping("hello")
    fun getHelloWorldMessage() = HelloWorldMessage()

    @GetMapping("helloWorld2")
    fun getHelloWorldMessage2(): HelloWorldMessage {
        return HelloWorldMessage()
    }

    //consumes = ["application/x-www-form-urlencoded;charset=UTF-8"]
    @RequestMapping(
        value = arrayOf("hello3"),
        method = [RequestMethod.POST, RequestMethod.GET],
        consumes = ["application/x-www-form-urlencoded"]
    )
    fun textContentType(@RequestBody body: String): HelloWorldMessage {
        println(body)
        return HelloWorldMessage("aaaaaaaaaaaaaa")
    }

}

data class HelloWorldMessage(var message: String = "hello world")