package com.kinorlev.appengine.v1.model

open class BaseResponse constructor(var method: String = "") {
    var succeed: Boolean = true
    var errorDescription: String = "no-error"
    var errorDetails: String = ""
    val ts: Long = System.currentTimeMillis()
}