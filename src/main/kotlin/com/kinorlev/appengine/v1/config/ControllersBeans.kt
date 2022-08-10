package com.kinorlev.appengine.v1.config

import com.kinorlev.appengine.v1.usecases.controllersusecases.CalculatePwfUseCase
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Service

@Service
class ControllersBeans {

    @Bean
    fun provideCalculatePwsUseCase() = CalculatePwfUseCase()
}