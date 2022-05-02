package com.kinorlev.appengine.extension


import com.google.firebase.auth.FirebaseToken
import com.kinorlev.appengine.v1.ServiceLogger
import com.kinorlev.appengine.v1.security.model.FirebaseUserDetails
import org.springframework.security.core.Authentication


fun Authentication.EXT_firebaseToken(logger: ServiceLogger): FirebaseToken {
    return try {
        val fbUserDetails = principal as FirebaseUserDetails
        val firebaseToken: FirebaseToken = fbUserDetails.firebaseToken
        firebaseToken
    } catch (e: Exception) {
        e.printStackTrace()
        logger.exception(e)
        throw e
    }
}