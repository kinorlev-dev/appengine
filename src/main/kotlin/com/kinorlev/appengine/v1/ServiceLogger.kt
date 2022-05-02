package com.kinorlev.appengine.v1



import com.fasterxml.jackson.databind.ObjectMapper
import com.kinorlev.appengine.extension.Ext_message

class ServiceLoggerException(message: String) : RuntimeException(message)

class ServiceLogger(private val mapper: ObjectMapper) {

    companion object {
        const val START = "start"
        const val END = "end"
        const val FAILED_BECAUSE = "failed_because"
        const val EXCEPTION = "exception"
        const val MILE_STONE = "mile_stone"
        const val MESSAGE = "message"
        const val SUCCESS = "success"
        const val SUMMARY = "summary"
        const val RESPONSE_CODE = "responseCode"
        const val ICOUNT_FAILURE = "icountFailure"
    }

    private val JSON_LOG = mapper.createObjectNode()

    fun start(message: String) {
        JSON_LOG.put(START, message)
    }


    fun responseCode(message: String) {
        JSON_LOG.put(RESPONSE_CODE, message)
    }

    fun summary(message: String) {
        JSON_LOG.put(SUMMARY, message)
    }

    fun success(message: String) {
        JSON_LOG.put(SUCCESS, message)
    }

    private var milStoneCounter = 1
    fun mileStone(message: String) {
        JSON_LOG.put("${MILE_STONE}_$milStoneCounter", message)
        milStoneCounter += 1
    }

    fun failedBecause(message: String) {
        JSON_LOG.put(FAILED_BECAUSE, message)
    }

    fun failedBecauseFirebaseTokenIsNull() {
        JSON_LOG.put(FAILED_BECAUSE, "firebaseToken==null")
    }

    fun end(message: String) {
        JSON_LOG.put(END, message)
    }

    fun exception(ex: Exception) {
        JSON_LOG.put(EXCEPTION, ex.Ext_message())
    }

    fun createException(errorMessage: String) {
        JSON_LOG.put(EXCEPTION, ServiceLoggerException(errorMessage).Ext_message())
    }

    private var messageCounter = 1
    fun addMessage(key: String, message: String) {
        JSON_LOG.put(key, message)
    }

    override fun toString(): String = mapper.writeValueAsString(JSON_LOG)

}