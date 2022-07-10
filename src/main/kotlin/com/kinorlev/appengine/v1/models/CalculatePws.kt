package com.kinorlev.appengine.v1.models

class CalculatePwfBody(val data: List<PwfData> = emptyList())
data class PwfData(val ts: Long = 0, val wave: Long = 0)

class CalculatePwfResponse(val message: String = "") : BaseResponse("CalculatePws")


data class FrequencyResponse (
        var eq: List<EqProperties> = listOf(),
        var reverb: ReverbProperties = ReverbProperties(),
        var compressor: CompressorProperties = CompressorProperties()

        /**
         * response:
         * eq:[{frequency: 10000(Int in khz 30-20000), amount = 3/-3(double in db 1-15)},{frequency: 10000(Int in khz 30-20000), amount = 3/-3(double in db 1-15)}]
         * reverb: amount: double in 0-1
         * compressor: amount: double in 0-1
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
