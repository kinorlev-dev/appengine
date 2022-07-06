package com.kinorlev.appengine.v1.usecases.controllersusecases

import com.kinorlev.appengine.v1.model.CalculatePwsBody
import com.kinorlev.appengine.v1.model.CalculatePwsResponse
import org.springframework.stereotype.Service

@Service
class CalculatePwsUseCase() {


    fun calculate(body: CalculatePwsBody): CalculatePwsResponse {
        println(body.data.joinToString())
        return CalculatePwsResponse("aaaaaaaaaaaaaaaa")
    }
}