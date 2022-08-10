package com.kinorlev.appengine.v1.usecases.controllersusecases

import com.kinorlev.appengine.v1.models.*
import com.kinorlev.appengine.v1.usecases.controllersusecases.utils.FFTData2
import com.kinorlev.appengine.v1.usecases.controllersusecases.utils.FFTWrapper
import org.springframework.stereotype.Service

@Service
class CalculatePwfUseCase() {

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

        //currently, the affection of the sfirot is minimal, after the learning of the machine, it will be the model
        //3. match the results
//        val eqAffection = calculateEq(categorize)

        //3.1. match the results demo
        val eqAffection = calculateEq(fft,categorize)
        return CalculatePwfResponse(eqAffection)

        //  test
//        return CalculatePwfResponse(FrequencyResponse())
    }

    private fun calculateEq(fft: FrequencyFftResponse, categorize:Int): FrequencyResponse {
        val res = FrequencyResponse()
        res.eq = listOf(
                EqProperties(60,fft.vlf),
                EqProperties(300,fft.lf),
                EqProperties(800,fft.mf),
                EqProperties(2500,fft.hf),
                EqProperties(10000,fft.vhf)
        )
        val reverbAmount = calculateReverbAmount(res)
        val compressorAmount = calculateCompressor(res)
        val specialTuning = calculateSpecialTuning(categorize)

        res.reverb.amount = reverbAmount
        res.compressor.amount = compressorAmount
        res.specialTuning.freq = specialTuning
        print("---endOfCalcEq---")
        return res
    }

    private fun calculateSpecialTuning(categorize: Int): Int {
        return when(categorize){
            11 -> 432
            12 -> 526
            13 -> 440
            else -> 440
        }
    }

    /**
     * if the low-mid range are strong, we will add compressor
     */
    private fun calculateCompressor(frequencyResponse: FrequencyResponse): Double {
        var amount = 1
        for (i in 0 until frequencyResponse.eq.size){
            if (frequencyResponse.eq[i].amount>5 && i<=2){
                amount *= i
            }
        }
        print("Compressor $amount")

        return amount.toDouble()
    }

    /**
     * if the high-mid range are strong, we will add compressor
     */
    private fun calculateReverbAmount(frequencyResponse: FrequencyResponse): Double {
        var amount = 1
        for (i in 0 until frequencyResponse.eq.size){
            if (frequencyResponse.eq[i].amount>5 && i>=2){
                amount *= i
            }
        }
        print("reverb $amount")
        return amount.toDouble()
    }

    private fun calculateEq(categorize: Int): FrequencyResponse {

        return FrequencyResponse()
    }

    private fun calculateCategory(fft: FrequencyFftResponse): Int {

        val frequencies = arrayListOf<Double>(fft.vlf,fft.lf,fft.mf,fft.hf,fft.vhf)

//        var thirdHighest:Int = -1
//        var secondHighest: Int = -1
        var highestIndex:Int = 0
        for (f in 0 until frequencies.size){
            if (frequencies[f]>frequencies[highestIndex]){
//                thirdHighest = secondHighest
//                secondHighest = highestIndex
                highestIndex = f
            }
        }

        print(ParzufimAndSfirot(highestIndex+11) + "of parzufim is the highest in this calculation")

        return highestIndex
    }

     fun ParzufimAndSfirot(index: Int):String {

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
          *
          * חלוקה לפרצופים:
          * 11 עתיק
          *12 אבא
          * 13 אמא
          * 14 ז"א
          * 15 נוקבא
          * בינתיים נעבוד עם זה
          */


         val map = mapOf<Int,String>(
           1 to "Kohkhma",
           2 to "Bina",
           3 to "Daat",
           4 to "Khesed",
           5 to "Gvura",
           6 to "Tiferet",
           7 to "Nezach",
           8 to "Hod",
           9 to "Yesod",
           10 to "Malchut",
           11 to "Atik",
           12 to "ABA",
           13 to "IMMA",
           14 to "ZEER",
           15 to "NUKVA"
         )
         return map[index]?:"something missing in the function"
    }


    private fun calculateData(calculatePwfBody: CalculatePwfBody): FrequencyFftResponse {
        /**
         * 1. prepare data to fft
         * 2. calculate fft
         * 3. calculate frequencies
         */

        //  1. prepare data to fft
        val fftData = FFTData2.fromRaw(calculatePwfBody.data)

        //  2. calculate fft
        val periodToMagnitude = FFTWrapper(fftData).calculate()
        print("period $periodToMagnitude")

        //  3.calculate frequencies
        val frequenciesResponse = convertFftToFrequencies(periodToMagnitude)

        return frequenciesResponse

    }


    fun convertFftToFrequencies(fft: List<Pair<Double, Double>>): FrequencyFftResponse{
        //  3.calculate frequencies
        val frequenciesResponse = FrequencyFftResponse()
        val peaks:ArrayList<Peaks> = arrayListOf()
        var avgCounterVlf:Int = 1
        var avgCounterLf:Int = 1
        var avgCounterMf:Int = 1
        var avgCounterHf:Int = 1
        var avgCounterVhf:Int = 1
        // TODO: 10/07/2022 check the real results from fft - what are the correct units size - maybe will between 35-190

        print("convertFftToFrequencies 1")
        for (i in fft){
            val x = i.first
            val y = i.second

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