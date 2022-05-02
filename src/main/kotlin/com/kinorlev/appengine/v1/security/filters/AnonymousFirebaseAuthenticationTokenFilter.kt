package com.kinorlev.appengine.v1.security.filters


import com.kinorlev.appengine.v1.security.authproviders.AnonymousFirebaseAuthenticationProvider
import org.springframework.security.authentication.ProviderManager

class AnonymousFirebaseAuthenticationTokenFilter
    : AbstractFirebaseAuthenticationTokenFilter(
    "AnonymousFirebase", "/anonymous/**"
) {

    override fun provideAuthManager() = ProviderManager(AnonymousFirebaseAuthenticationProvider())
}