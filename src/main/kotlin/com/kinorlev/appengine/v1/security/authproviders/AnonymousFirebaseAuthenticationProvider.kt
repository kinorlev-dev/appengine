package com.kinorlev.appengine.v1.security.authproviders



import com.google.api.core.ApiFuture
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseToken
import com.kinorlev.appengine.v1.security.model.FirebaseAuthenticationToken

import com.kinorlev.appengine.v1.security.model.FirebaseUserDetails
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken

class AnonymousFirebaseAuthenticationProvider : AbstractFirebaseAuthenticationProvider() {
    override fun verifyUser(
        authentication: UsernamePasswordAuthenticationToken,
        checkRevoked: Boolean
    ): FirebaseUserDetails {
        val authenticationToken = authentication as FirebaseAuthenticationToken
        val task: ApiFuture<FirebaseToken> =
            FirebaseAuth.getInstance().verifyIdTokenAsync(authenticationToken.token, checkRevoked)
        val token = task.get()
        return FirebaseUserDetails(token)
    }
}