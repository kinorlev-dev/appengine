package com.kinorlev.appengine.v1.models

/**
 * this model represent the properties which returs from the server according the shape of the wave.
 */
data class EQProperties(
        var frequencyToEmphasize: IntArray = intArrayOf(),
        var frequencyToDecrease: IntArray = intArrayOf(),
        var reverbAmount: Int = 0,
        var compressorAmount: Int = 0
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as EQProperties

        if (!frequencyToEmphasize.contentEquals(other.frequencyToEmphasize)) return false
        if (!frequencyToDecrease.contentEquals(other.frequencyToDecrease)) return false
        if (reverbAmount != other.reverbAmount) return false
        if (compressorAmount != other.compressorAmount) return false

        return true
    }

    override fun hashCode(): Int {
        var result = frequencyToEmphasize.contentHashCode()
        result = 31 * result + frequencyToDecrease.contentHashCode()
        result = 31 * result + reverbAmount
        result = 31 * result + compressorAmount
        return result
    }

}
