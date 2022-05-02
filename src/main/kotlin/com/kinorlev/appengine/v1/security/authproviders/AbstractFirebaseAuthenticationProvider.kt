package com.kinorlev.appengine.v1.security.authproviders



import com.kinorlev.appengine.v1.security.model.FirebaseAuthenticationToken
import com.kinorlev.appengine.v1.security.model.FirebaseUserDetails
import org.springframework.security.authentication.ProviderManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.web.authentication.session.SessionAuthenticationException
import org.springframework.stereotype.Component
import java.lang.RuntimeException
import java.util.concurrent.ExecutionException

@Component
abstract class AbstractFirebaseAuthenticationProvider : AbstractUserDetailsAuthenticationProvider() {

    override fun supports(authentication: Class<*>?) =
            FirebaseAuthenticationToken::class.java.isAssignableFrom(authentication)

    override fun additionalAuthenticationChecks(
            userDetails: UserDetails?,
            authentication: UsernamePasswordAuthenticationToken?
    ) {

    }

    override fun retrieveUser(username: String?, authentication: UsernamePasswordAuthenticationToken): UserDetails {
        try {
            println("retrieveUser")
            return verifyUser(authentication, false)//CLIENT SHOULD CHECK AND REFRESH TOKEN BEFORE REQUEST
        } catch (e: Exception) {
            try {
                println("retrieveUser check revoked = TRUE")
                return verifyUser(authentication, true)
            } catch (e: Exception) {
                println("retrieveUse failed message= ${e.message}")
                when (e) {
                    is InterruptedException, is ExecutionException -> {
                        throw SessionAuthenticationException(e.message)
                    }
                    else -> throw RuntimeException("retrieveUser failed. message: ${e.message}")
                }
            }

        }
    }

    abstract fun verifyUser(
            authentication: UsernamePasswordAuthenticationToken,
            checkRevoked: Boolean
    ): FirebaseUserDetails


}