package com.kinorlev.appengine.v1.config

import com.kinorlev.appengine.v1.firebase.FirebaseUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Scope
import org.springframework.core.env.Environment

@Configuration
class AppConfig {

    @Autowired
    lateinit var env: Environment

    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    fun provideEnvironmentWrapper() = EnvironmentWrapper(env)

    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    fun provideFirestore(environmentWrapper:EnvironmentWrapper) = FirebaseUtils(environmentWrapper)



}