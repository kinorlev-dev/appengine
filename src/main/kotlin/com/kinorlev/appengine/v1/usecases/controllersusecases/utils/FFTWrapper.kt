package com.kinorlev.appengine.v1.usecases.controllersusecases.utils

class FFTWrapper constructor(private val fftData: FFTData2) {

    companion object{
        var firstFrequency: Double = 0.05
        var lastFrequency: Double = 0.25
        var DominateFReqWideness: Double =  0.04
    }
    fun calculate(filterFrequencies: Boolean = true): List<Pair<Double, Double>> {

            val signal = fftData.signal
            val fftExecutor = FFT(signal.size)
            val realArray = DoubleArray(signal.size) {
                signal[it]
            }
            val imgArray = DoubleArray(signal.size) {
                0.0
            }
            fftExecutor.fft(realArray, imgArray)
            val magnitudes: List<Double> = realArray.mapIndexed { index, t ->
                val complex = Complex(t, imgArray[index])
                complex.abs()
            }

//            val periodToMagnitude = mapMagnittudesToPeriodicsAndFilter(magnitudes)
            val freqToMagnitude =
                mapMagnittudesToFrequenciesAndFilter(magnitudes, filterFrequencies)
//            val widePeriodToMagnitude = gatherFrequenciesByUnit(freqToMagnitude)
//            val widePeriodToMagnitude = gatherPeriodsByUnit(periodToMagnitude)
            return freqToMagnitude
    }


    /**
     * [[5.0,1],[5.1,2]...]->[[5,3]...]
     */
    private fun gatherPeriodsByUnit(periodToMagnitude: List<Pair<Double, Double>>): List<Pair<Double, Double>> {
        val unitsToValues = HashMap<Int, Double>()
        periodToMagnitude.forEach {
            val unit = ((it.first / 10) * 10).toInt()
            val unitToValue: Double? = unitsToValues[unit]
            if (unitToValue == null) {
                unitsToValues[unit] = it.second
            } else {
                unitsToValues[unit] = unit + it.second
            }
        }
        val retVal: List<Pair<Double, Double>> = unitsToValues.map {
            Pair(it.key.toDouble(), it.value)
        }
        return retVal.sortedBy {
            it.first
        }
    }
/**
     * [[5.0,1],[5.1,2]...]->[[5,3]...]
     */
    private fun gatherFrequenciesByUnit(periodToMagnitude: List<Pair<Double, Double>>): List<Pair<Double, Double>> {
        val unitsToValues = HashMap<Int, Double>()
        periodToMagnitude.forEach {
            val unit = ((it.first * 10) / 10).toInt()
            val unitToValue: Double? = unitsToValues[unit]
            if (unitToValue == null) {
                unitsToValues[unit] = it.second
            } else {
                unitsToValues[unit] = unit + it.second
            }
        }
        val retVal: List<Pair<Double, Double>> = unitsToValues.map {
            Pair(it.key.toDouble(), it.value)
        }
        return retVal.sortedBy {
            it.first
        }
    }

//    private fun mapMagnittudesToPeriodicsAndFilter(magnitudes: List<Double>): List<Pair<Double, Double>> {
//        val periodToMagnitude: List<Pair<Double, Double>> =
//            fftData.allPeriodics.mapIndexed { index, period ->
//                Pair(period, magnitudes[index])
//            }.filter {
//                it.first > 5.0 && it.first < 15
//            }
//        return periodToMagnitude
//    }

    private fun mapMagnittudesToFrequenciesAndFilter(
        magnitudes: List<Double>,
        filterFrequencies: Boolean
    ): List<Pair<Double, Double>> {
        var periodToMagnitude: List<Pair<Double, Double>> =
            fftData.allFrequencies.mapIndexed { index, period ->
                Pair(period, magnitudes[index])
            }
        if (filterFrequencies) {
            periodToMagnitude = periodToMagnitude.filter {
                it.first in firstFrequency..lastFrequency
            }
        }

        return periodToMagnitude
    }
}