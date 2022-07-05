package com.kinorlev.appengine.v1.config

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.KotlinFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.kinorlev.appengine.v1.firebase.FirebaseUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Scope
import org.springframework.core.env.Environment
import java.text.SimpleDateFormat

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

    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    fun provideObjectMapper(): ObjectMapper {
        val mapper = ObjectMapper().apply {
            configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            enable(SerializationFeature.INDENT_OUTPUT)
            setSerializationInclusion(JsonInclude.Include.NON_NULL)
            disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
            registerModule(getKotlinModule())
            dateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")

        }
        return mapper
    }

    fun getKotlinModule() = KotlinModule.Builder()
        .enable(KotlinFeature.NullIsSameAsDefault)
        .enable(KotlinFeature.NullToEmptyCollection)
        .enable(KotlinFeature.NullToEmptyMap)
        .build()



}