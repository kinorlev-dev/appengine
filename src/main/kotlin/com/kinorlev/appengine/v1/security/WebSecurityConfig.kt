package com.kinorlev.appengine.v1.security



import com.kinorlev.appengine.v1.security.filters.AbstractFirebaseAuthenticationTokenFilter
import com.kinorlev.appengine.v1.security.filters.AnonymousFirebaseAuthenticationTokenFilter
import com.kinorlev.appengine.v1.security.filters.EmailVerifiedFirebaseAuthenticationTokenFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
class WebSecurityConfig : WebSecurityConfigurerAdapter() {


    override fun configure(http: HttpSecurity) {
        /*http
            .authorizeRequests { a ->
                a
                    .antMatchers("/public").permitAll()
                    //.mvcMatchers(HttpMethod.GET,"/public").hasAnyAuthority("user")
                    .anyRequest().authenticated()


            }.httpBasic()
*/
        /*http
            .authorizeRequests()
            .antMatchers("/private/private")
            .authenticated()*/

        /*http.authorizeRequests()
            .regexMatchers("/private/private")
            .authenticated()*/

        http.csrf().disable()
        http
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

        http
            .headers()
            .cacheControl()
            .disable()



        addFilters(http)
//        http.addFilterBefore(
//            authenticationTokenFilterBean(),
//            UsernamePasswordAuthenticationFilter::class.java
//        )
//
//        http.addFilterBefore(
//            clientAuthFilterBean(),
//            UsernamePasswordAuthenticationFilter::class.java
//        )
        //http.headers().frameOptions().disable() //X-Frame-Options: DENY
    }

    private fun addFilters(http: HttpSecurity) {
        val filters: List<AbstractFirebaseAuthenticationTokenFilter> = listOf(
            EmailVerifiedFirebaseAuthenticationTokenFilter(),
            AnonymousFirebaseAuthenticationTokenFilter(),
        )
        filters.forEach { filter ->
            filter.apply {
                setAuthenticationManager(filter.provideAuthManager())
                setAuthenticationSuccessHandler { request: HttpServletRequest?, response: HttpServletResponse?, authentication: Authentication? -> }
            }
            http.addFilterBefore(
                filter,
                UsernamePasswordAuthenticationFilter::class.java
            )
        }
        //addDebugFilter(http)
    }

    /*private fun addDebugFilter(http: HttpSecurity) {
        val debugFilter = DebugFilter()
        debugFilter.setAuthenticationManager(ProviderManager(DebugProvider()))
        debugFilter.setAuthenticationSuccessHandler { request: HttpServletRequest?, response: HttpServletResponse?, authentication: Authentication? -> }
        http.addFilterBefore(debugFilter, UsernamePasswordAuthenticationFilter::class.java)
    }*/
/*
    @kotlin.jvm.Throws(Exception::class)
    fun authenticationTokenFilterBean(): FirebaseAuthenticationTokenFilter {
        return FirebaseAuthenticationTokenFilter().apply {
            setAuthenticationManager(authenticationManager())
            setAuthenticationSuccessHandler { request: HttpServletRequest?, response: HttpServletResponse?, authentication: Authentication? -> }
        }
    }

    fun clientAuthFilterBean(): ClientAuthenticationFilter {
        return ClientAuthenticationFilter().apply {
            setAuthenticationManager(authenticationManager())
            setAuthenticationSuccessHandler { request: HttpServletRequest?, response: HttpServletResponse?, authentication: Authentication? -> }
        }
    }


    @Bean
    override fun authenticationManager() =
        //ProviderManager(listOf(FirebaseAuthenticationProvider(), ClientAuthenticationProvider()))
        ProviderManager(
            listOf(
                EmailVerifiedFirebaseAuthenticationProvider(),
                AnonymousFirebaseAuthenticationProvider()
            )
        )*/


}
