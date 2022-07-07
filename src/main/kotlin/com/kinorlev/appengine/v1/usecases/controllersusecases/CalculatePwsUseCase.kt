package com.kinorlev.appengine.v1.usecases.controllersusecases

import com.kinorlev.appengine.v1.usecases.controllersusecases.utils.FFT
import com.kinorlev.appengine.v1.model.CalculatePwfBody
import com.kinorlev.appengine.v1.model.CalculatePwfResponse
import org.springframework.stereotype.Service

@Service
class CalculatePwsUseCase() {


    fun calculate(body: CalculatePwfBody): CalculatePwfResponse {
        println(body.data.joinToString())
        /**
         * 1. calculate the data
         * 2. categorize it
         * 3. match the results
         * 4. send it back
         */

        calculateData(body)

        return CalculatePwfResponse("aaaaaaaaaaaaaaaa")
    }

    private fun calculateData(calculatePwfBody: CalculatePwfBody): frequencyFftResponse {
        val data = calculatePwfBody.data
        val fft = FFT.fft(calculatePwfBody.data)
        return
    }


}

data class frequencyFftResponse(
//        LF = lowFrequency in Hz - relate to high frequency in equalizer (1500-7000khz)
        var hf: Int,
//        MF = midFrequency in Hz - relate to high frequency in equalizer (460-1500khz)
        var mf: Int,
//        LF = lowFrequency in Hz  - relate to low mid frequency in equalizer(120-460khz)
        var lf: Int,
//        VLF = very low Frequency in Hz - relate to bass frequency in equalizer(0-120khz)
        var vlf: Int,
//        VHF = Very high Frequency in Hz - i dont know if its exist - relate to high frequency in equalizer(7-20khz)
        var vhf: Int
)