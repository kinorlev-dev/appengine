package com.kinorlev.appengine.v1.security.filters


import com.kinorlev.appengine.v1.security.model.FirebaseAuthenticationToken
import org.springframework.security.authentication.ProviderManager
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter
import org.springframework.security.web.authentication.session.SessionAuthenticationException
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

abstract class AbstractFirebaseAuthenticationTokenFilter(
        private val TAG: String,
        defaultFilterProcessesUrl: String) :
        AbstractAuthenticationProcessingFilter(defaultFilterProcessesUrl) {
    companion object {
        const val TOKEN_HEADER = "Authorization"
    }

    abstract fun provideAuthManager(): ProviderManager

    override fun attemptAuthentication(request: HttpServletRequest, response: HttpServletResponse?): Authentication {
        println("$TAG attemptAuthentication")
        val authToken = request.getHeader(TOKEN_HEADER)
        if (authToken.isNullOrEmpty()) {
            throw SessionAuthenticationException("$TAG Invalid auth token")
        }
        val firebaseAuthenticationToken = FirebaseAuthenticationToken(authToken.substring(7, authToken.length))
        val authenticate = authenticationManager.authenticate(firebaseAuthenticationToken)
        return authenticate
    }

    override fun successfulAuthentication(
            request: HttpServletRequest,
            response: HttpServletResponse,
            chain: FilterChain,
            authResult: Authentication
    ) {
        println("$TAG successfulAuthentication")
        super.successfulAuthentication(request, response, chain, authResult)
        chain.doFilter(request, response)
    }

    override fun unsuccessfulAuthentication(
            request: HttpServletRequest?,
            response: HttpServletResponse?,
            failed: AuthenticationException?
    ) {
        val message = if (failed != null) {
            failed.message
        } else {
            "$TAG un known message"
        }
        println("$TAG unsuccessfulAuthentication message: $message")
        super.unsuccessfulAuthentication(request, response, failed)
    }


}