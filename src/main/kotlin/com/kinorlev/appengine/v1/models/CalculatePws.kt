package com.kinorlev.appengine.v1.models

class CalculatePwfBody(val data: List<PwfData> = listOf<PwfData>())
data class PwfData(val ts: Long = 0, val wave: Long = 0)

class CalculatePwfResponse(val message: FrequencyResponse = FrequencyResponse()) : BaseResponse("CalculatePws")


data class FrequencyResponse (
        var eq: List<EqProperties> = listOf(),
        var reverb: ReverbProperties = ReverbProperties(),
        var compressor: CompressorProperties = CompressorProperties(),
        var specialTuning: SpecialTuning = SpecialTuning()

        /**
         * response:
         * eq:[{frequency: 10000(Int in khz 30-20000), amount = 3(double in db 1-15)},{frequency: 10000(Int in khz 30-20000), amount = 3(double in db 1-15)}]
         * reverb: amount: double in 0-1
         * compressor: amount: double in 0-1
         * specialTuning: freq: Int in 0-20000
         */
)

data class EqProperties(
        //Int in khz 30-20000
        var freq: Int = 0,
        var amount: Double = 0.0
)

data class ReverbProperties(
        var amount: Double = 0.0
)


data class CompressorProperties(
        var amount: Double = 0.0
)

data class SpecialTuning(
        //Int in khz 30-20000
        // probably 432/526/440
        var freq: Int = -1
)

sealed class Sfirot{
        object kohkhma:Sfirot()
        object bina:Sfirot()
        object daat:Sfirot()
        object khesed:Sfirot()
        object gvura:Sfirot()
        object tiferet:Sfirot()
        object nezach:Sfirot()
        object Hod:Sfirot()
        object Yesod:Sfirot()
        object Malchut : Sfirot()
}

sealed class Parzufim{
        object AtikYomin: Parzufim()
        object Aba: Parzufim()
        object Ima: Parzufim()
        object ZeerAnpin: Parzufim()
        object Nukva: Parzufim()
}