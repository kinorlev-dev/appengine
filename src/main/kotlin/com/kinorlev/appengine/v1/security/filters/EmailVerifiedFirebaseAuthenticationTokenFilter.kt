package com.kinorlev.appengine.v1.security.filters

import com.kinorlev.appengine.v1.security.authproviders.EmailVerifiedFirebaseAuthenticationProvider
import org.springframework.security.authentication.ProviderManager

class EmailVerifiedFirebaseAuthenticationTokenFilter
    : AbstractFirebaseAuthenticationTokenFilter(
    "EmailVerifiedFirebase", "/user/**") {

    override fun provideAuthManager() = ProviderManager(EmailVerifiedFirebaseAuthenticationProvider())
}