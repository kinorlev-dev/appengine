package com.kinorlev.appengine.v1.model

class CalculatePwfBody(val data: List<PwfData> = emptyList())
data class PwfData(val ts: Long = 0, val wave: Long = 0)

class CalculatePwfResponse(val message: String = "") : BaseResponse("CalculatePws")

