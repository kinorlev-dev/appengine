package com.kinorlev.appengine.v1.controllers


import com.google.cloud.firestore.DocumentSnapshot
import com.kinorlev.appengine.v1.firebase.FirebaseUtils
import com.kinorlev.appengine.v1.models.FrequencyResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
class MusicToEqConverterController {

    @Autowired
    lateinit var firebaseUtils: FirebaseUtils

//    @GetMapping("getEqByPwf")
//    fun getEqPropertiesByPwfData(): EQProperties {
//        val ref = firebaseUtils.fireStoreRef
//
//        return EQProperties()
//    }

    data class Ping(val hello: String = "")

    @GetMapping("pingFirestore")
    fun pingFirestore(): HelloWorldMessage {
        val ref = firebaseUtils.fireStoreRef.ping().get()
        val docSnapshot: DocumentSnapshot = ref.get()
        val ping: Ping? = docSnapshot.toObject(Ping::class.java)
        val message = "ping -hello- field value is : ${ping?.hello}"
        return HelloWorldMessage(message)
    }



    fun getFrequenciesFromFft(): FrequencyResponse {
        return FrequencyResponse()
    }


}
