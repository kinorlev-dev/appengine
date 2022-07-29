package com.kinorlev.appengine.v1.model

import com.kinorlev.appengine.v1.models.BaseResponse
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty

class CalculatePwsBody(val data: List<PwsData> = emptyList())
@ApiModel("Sensor raw data")
data class PwsData(
    @ApiModelProperty("Ts in ms of initialization of Entity (not from sensor)")
    val ts: Long = 0,
    @ApiModelProperty("Received from sensor (not calculated)")
    val wave: Long = 0)

class CalculatePwsResponse(val message: String = "") : BaseResponse("CalculatePws")

