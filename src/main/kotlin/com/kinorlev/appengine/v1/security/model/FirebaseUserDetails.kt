package com.kinorlev.appengine.v1.security.model

import com.google.firebase.auth.FirebaseToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class FirebaseUserDetails constructor(val firebaseToken: FirebaseToken) : UserDetails {

    companion object {
        private const val serialVersionUID = 1L
    }
    val email = firebaseToken.email
    private var username = firebaseToken.name
    var id: String = firebaseToken.uid
    private val enabled = true
    private val credentialsNonExpired = true
    private val isAccountNonLocked = true
    private val accountNonExpired = true
    private val password: String? = null
    val authorities = ArrayList<GrantedAuthority>()/*.apply {
        val aut = GrantedAuthority {
            "fullyAuthenticated"
        }
        add(aut)
    }*/
    override fun getUsername() = if(username.isNullOrEmpty()) email else username
    override fun isEnabled() = enabled
    override fun isCredentialsNonExpired() = credentialsNonExpired
    override fun isAccountNonExpired() = accountNonExpired
    override fun isAccountNonLocked() = isAccountNonLocked
    override fun getPassword() = password!!

    override fun getAuthorities(): Collection<GrantedAuthority> {
        return authorities
    }

}