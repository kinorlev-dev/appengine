package com.kinorlev.appengine.v1.controllers


import com.google.api.core.ApiFuture
import com.google.cloud.firestore.DocumentSnapshot
import com.kinorlev.appengine.v1.firebase.FireStoreRef
import com.kinorlev.appengine.v1.firebase.FirebaseUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
class HelloWorldController {

    @Autowired
    lateinit var firebaseUtils: FirebaseUtils

    @GetMapping("hello")
    fun getHelloWorldMessage() = HelloWorldMessage()

    data class Ping(val hello: String = "")

    @GetMapping("pingFirestore2")
    fun pingFirestore(): HelloWorldMessage {
        val ref = firebaseUtils.fireStoreRef.ping().get()
        val docSnapshot: DocumentSnapshot = ref.get()
        val ping: Ping? = docSnapshot.toObject(Ping::class.java)
        val message = "ping -hello- field value is : ${ping?.hello}"
        return HelloWorldMessage(message)
    }



}

data class HelloWorldMessage(var message: String = "hello world")