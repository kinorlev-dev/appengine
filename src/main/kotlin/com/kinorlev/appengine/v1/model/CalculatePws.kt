package com.kinorlev.appengine.v1.model

class CalculatePwsBody(val data: List<PwsData> = emptyList())
data class PwsData(val ts: Long = 0, val wave: Long = 0)

class CalculatePwsResponse(val message: String = "") : BaseResponse("CalculatePws")

