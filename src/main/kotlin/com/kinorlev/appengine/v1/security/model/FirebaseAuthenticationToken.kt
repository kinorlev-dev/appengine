package com.kinorlev.appengine.v1.security.model

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken

class FirebaseAuthenticationToken(val token: String) :
    UsernamePasswordAuthenticationToken(null, null) {
}

class ClientAuthentication(val client: String) :
    UsernamePasswordAuthenticationToken(null, null) {
}

