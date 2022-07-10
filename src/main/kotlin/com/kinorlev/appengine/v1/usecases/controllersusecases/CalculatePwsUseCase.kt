package com.kinorlev.appengine.v1.usecases.controllersusecases

import com.kinorlev.appengine.v1.models.CalculatePwfBody
import com.kinorlev.appengine.v1.models.CalculatePwfResponse
import com.kinorlev.appengine.v1.models.EqProperties
import com.kinorlev.appengine.v1.models.FrequencyResponse
import com.kinorlev.appengine.v1.usecases.controllersusecases.utils.Complex
import com.kinorlev.appengine.v1.usecases.controllersusecases.utils.FFT
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

        //1. calculate the data
        val fft = calculateData(body)

        //2. categorize it
        val categorize = calculateCategory(fft)

        //3. match the results
//        val eqAffection = calculateEq(categorize)

        //3.1. match the results demo
        val eqAffection = calculateEq(fft)

        return CalculatePwfResponse("aaaaaaaaaaaaaaaa")
    }

    private fun calculateEq(fft: FrequencyFftResponse): FrequencyResponse {
        val res = FrequencyResponse()
        res.eq = listOf(
                EqProperties(60,fft.vlf),
                EqProperties(300,fft.lf),
                EqProperties(800,fft.mf),
                EqProperties(2500,fft.hf),
                EqProperties(10000,fft.vhf)
        )

        res.reverb.amount = 0.1
        res.compressor.amount = 0.1
        return res
    }

    private fun calculateEq(categorize: Int): FrequencyResponse {

        return FrequencyResponse()
    }

    private fun calculateCategory(fft: FrequencyFftResponse): Int {
        /**
         * 0 - null
         * 1 - חכמה
         * 2 - בינה
         * 3 - דעת
         * 4 - חסד
         * 5 - גבורה
         * 6 - תפארת
         * 7 - נצח
         * 8 - הוד
         * 9 - יסוד
         * 10 - מלכות
         */

        val frequencies = arrayListOf<Double>(fft.vlf,fft.lf,fft.mf,fft.hf,fft.vhf)

        for(i in fft.highestPeaks){}

        return 0
    }

    private fun calculateData(calculatePwfBody: CalculatePwfBody): FrequencyFftResponse {
        /**
         * 1. prepare data to fft
         * 2. calculate fft
         * 3. calculate frequencies
         */

        //  1. prepare data to fft
        val preparedData : ArrayList<Complex?> = arrayListOf()
        for (pwfData in calculatePwfBody.data){
            preparedData.add(Complex(pwfData.ts.toDouble(),pwfData.wave.toDouble()))
        }
        //  2. calculate fft
        val fft = FFT.fft(preparedData.toArray() as Array<Complex?>)

        //  3.calculate frequencies
        val frequenciesResponse = convertFftToFrequencies(fft)

        return frequenciesResponse

    }


    fun convertFftToFrequencies(fft : Array<Complex?>): FrequencyFftResponse{
        //  3.calculate frequencies
        val frequenciesResponse = FrequencyFftResponse()
        val peaks:ArrayList<Peaks> = arrayListOf()
        var avgCounterVlf:Int = 0
        var avgCounterLf:Int = 0
        var avgCounterMf:Int = 0
        var avgCounterHf:Int = 0
        var avgCounterVhf:Int = 0
        // TODO: 10/07/2022 check the real results from fft - what are the correct units size - maybe will between 35-190

        for (i in fft){
            val x = i?.re()?.dec()!!
            val y = i.im().dec()

            peaks.add(Peaks(x,y))

            if (x > 0.0 && x < 1.2 ){
                frequenciesResponse.vlf+=y
                avgCounterVlf ++
            }

            if (x >= 1.2 && x < 4.6 ){
                frequenciesResponse.lf+=y
                avgCounterLf ++
            }
            if (x >= 4.6 && x < 15.0 ){
                frequenciesResponse.mf+=y
                avgCounterMf ++
            }
            if (x >= 15 && x < 700 ){
                frequenciesResponse.hf+=y
                avgCounterHf ++
            }
            if (x >= 700 && x < 2000 ){
                frequenciesResponse.vhf+=y
                avgCounterVhf ++
            }
        }

        frequenciesResponse.vlf = frequenciesResponse.vlf/avgCounterVlf
        frequenciesResponse.lf = frequenciesResponse.lf/avgCounterLf
        frequenciesResponse.mf = frequenciesResponse.mf/avgCounterMf
        frequenciesResponse.hf = frequenciesResponse.hf/avgCounterHf
        frequenciesResponse.vhf = frequenciesResponse.vhf/avgCounterVhf
        frequenciesResponse.highestPeaks = findPeak(peaks)

        return frequenciesResponse
    }

    fun findPeak(arr: ArrayList<Peaks>): ArrayList<Peaks> {
        val n = arr.size
        // First or last element is peak element
        if (n == 1) return arrayListOf(arr[0])
        if (arr[0].y >= arr[1].y) return arrayListOf(arr[0])
        if (arr[n - 1].y >= arr[n - 2].y) return arrayListOf(arr[n-1])
        // Check for every other element
        for (i in 1 until n - 1) {
            // Check if the neighbors are smaller
            if (arr[i].y >= arr[i - 1].y && arr[i].y >= arr[i + 1].y) return arrayListOf(arr[i])
        }
        return arrayListOf(arr[0])
    }
}

data class FrequencyFftResponse(
//        LF = lowFrequency in Hz - relate to high frequency in equalizer (1500-7000khz)
        var hf: Double =0.0,
//        MF = midFrequency in Hz - relate to high frequency in equalizer (460-1500khz)
        var mf: Double =0.0,
//        LF = lowFrequency in Hz  - relate to low mid frequency in equalizer(120-460khz)
        var lf: Double =0.0,
//        VLF = very low Frequency in Hz - relate to bass frequency in equalizer(0-120khz)
        var vlf: Double =0.0,
//        VHF = Very high Frequency in Hz - i dont know if its exist - relate to high frequency in equalizer(7k-20khz)
        var vhf: Double =0.0,
        // where are the peaks
        var highestPeaks : ArrayList<Peaks> = arrayListOf()
)

data class Peaks(
        var x: Double,
        var y: Double
)