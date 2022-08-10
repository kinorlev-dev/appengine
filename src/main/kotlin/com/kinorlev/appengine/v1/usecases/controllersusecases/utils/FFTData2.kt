package com.kinorlev.appengine.v1.usecases.controllersusecases.utils


import com.kinorlev.appengine.v1.models.PwfData
import kotlin.math.roundToInt
import kotlin.properties.Delegates

class FFTData2 constructor(
    private val timeToSignal: List<Pair<Double, Double>>,
    private val strategic: FillGapStrategic = FillGapStrategic.Linear
) {

    companion object {

        fun fromRaw(rawData: List<PwfData>, strategic: FillGapStrategic = FillGapStrategic.AsPrevious): FFTData2 {
            val dataFormat = rawData.map {
                Pair(it.ts.toDouble(), it.wave.toDouble())
            }
            return FFTData2(dataFormat)
        }
    }

    enum class FillGapStrategic {
        Linear, AsPrevious
    }

    val sizeOfSamplesBeforeManipulation = timeToSignal.size
    val sizeOfSamplesAfterAddingApproxiamateSignals: Int
    var sizeOfSamplesAfterCompleteToPower2 by Delegates.notNull<Int>()

    val time: List<Double>
    val signal: List<Double>
    val allFrequencies: List<Double>

    private val quarter = 0.25
    private val half = 0.50
    private val threeQuarters = 0.75


    init {
        var tempTime: List<Double> = emptyList()
        var tempSignal: List<Double> = emptyList()
        tempTime = roundTimeValuesTo1DigitAfterDot()
        val timeWithMissingSamples = addApproxiamateSignalsWhenNeeded(tempTime, timeToSignal.map { it.second })
        tempTime = timeWithMissingSamples.first
        tempSignal = timeWithMissingSamples.second

        sizeOfSamplesAfterAddingApproxiamateSignals = tempTime.size

        val powersList = completeValuesToPowerOf2(tempTime, tempSignal)
        tempTime = powersList.first
        tempSignal = powersList.second
        time = tempTime
        signal = tempSignal
        allFrequencies = calculateFrequencies(time)
    }



    /**
     * [1.0,1.23],[1.51,2.22] -> [1.0,1.23],[1.25,0.0],[1.51,2.22]
     */
    private fun addApproxiamateSignalsWhenNeeded(
        tempTime: List<Double>,
        tempSignal: List<Double>
    ): Pair<List<Double>, List<Double>> {
        val timeWithApproximateValues = ArrayList<Double>()
        val signalWithApproximateValues = ArrayList<Double>()
        val lastIndex = tempTime.lastIndex
        tempTime.forEachIndexed { index, time ->
            if (index < lastIndex) {
                val nextTime: Double = tempTime[index + 1]
                var currentTime = time
                timeWithApproximateValues.add(currentTime)
                signalWithApproximateValues.add(tempSignal[index])
                var numOfMissingElements = 0
                while (nextTime - currentTime > 0.1) {
                    numOfMissingElements += 1
                    currentTime+=0.1
                }
                val firstSignal = tempSignal[index]
                val nextRealSignal = tempSignal[index + 1]
                val missingElements = getMissingApproximatesSignals(time,firstSignal,nextRealSignal,numOfMissingElements)
                timeWithApproximateValues.addAll(missingElements.first)
                signalWithApproximateValues.addAll(missingElements.second)
            } else {
//                timeWithZeros.add(time)
//                signalWithZeros.add(tempSignal[index])
            }
        }
        return timeWithApproximateValues to signalWithApproximateValues
    }

    fun getMissingApproximatesSignals(startTime: Double, currentIteratedSignal: Double, nextRealSignal: Double, numOfMissingElements: Int): Pair<List<Double>, List<Double>> {
        val timeWithQuarters = ArrayList<Double>()
        val signalWithQuarters = ArrayList<Double>()
        val curve = (nextRealSignal - currentIteratedSignal) / (1.0 + numOfMissingElements.toDouble())
        var currentTime = startTime
        var counter = 0
        while (counter < numOfMissingElements) {
            currentTime += 0.1
            timeWithQuarters.add(currentTime.EXT_Round(1))
            when (strategic) {
                FillGapStrategic.Linear -> {
                    val approximateSignal = currentIteratedSignal + curve * (counter + 1).toDouble()
                    signalWithQuarters.add(approximateSignal.EXT_Round(1))
                }
                FillGapStrategic.AsPrevious -> {
                    signalWithQuarters.add(currentIteratedSignal)
                }
            }

            counter += 1
        }

        return Pair(timeWithQuarters, signalWithQuarters)
    }

    /**
     * 1.22->1.00
     * 1.33->1.25
     * 1.66->1.50
     * 188->1.75
     */
    private fun roundToSecondQuoarters(tempTime: List<Double>): ArrayList<Double> {
        val roundTime = ArrayList<Double>()

        tempTime.forEach {time->
            val roundedGoodVal = foundNumToQuarterJump(time)
            roundTime.add(roundedGoodVal)
        }
        return roundTime
    }
    fun foundNumToQuarterJump(num:Double): Double {
        val intVal: Int = num.toInt()
        val afterDot = num - intVal
        val decimalVal = when {
            afterDot < quarter -> 0.00
            afterDot < half -> 0.25
            afterDot < threeQuarters -> 0.50
            else -> 0.75
        }
        val goodVal: Double = intVal + decimalVal
        val roundedGoodVal = innerRound(goodVal)
        return roundedGoodVal
    }

    fun calculateFrequencies(time:List<Double>): List<Double> {
        val allFrequencies = ArrayList<Double>()
        val sampleRateInHz = 0.1
        val numOfSamples = time.size
        time.forEachIndexed { i, t ->
            val freqInHz = i * (1.0 / (numOfSamples * sampleRateInHz))
            allFrequencies.add(freqInHz)
        }
        return allFrequencies
    }


    /**
     * 5.7597547u5 -> 5.76
     */
    fun roundTimeValuesTo1DigitAfterDot(): List<Double> {
        val tempTime = ArrayList<Double>()
        val tempSignal = ArrayList<Double>()
        timeToSignal.forEachIndexed { i, timeToSig ->
            tempTime.add(timeToSig.first.EXT_Round(1))
            tempSignal.add(timeToSig.second)
        }
        return tempTime

    }

    /**
     * 5.7597547u5 -> 5.76
     */
    fun roundTimeValuesTo2DigitAfterDot(): List<Double> {
        val tempTime = ArrayList<Double>()
        val tempSignal = ArrayList<Double>()
        timeToSignal.forEachIndexed { i, timeToSig ->
            tempTime.add(innerRound(timeToSig.first))
            tempSignal.add(timeToSig.second)
        }
        return tempTime

    }

    /**
     * fft algorithm require that sample size will be number that is power of 2 (2,4,8,16,32..)
     */
    fun completeValuesToPowerOf2(tempTime: List<Double>, tempSignal: List<Double>): Pair<ArrayList<Double>, ArrayList<Double>> {
        val timePowerList = ArrayList(tempTime)
        val signalPowerList = ArrayList(tempSignal)
        val size = tempTime.size
        if (size <= 1) {
            sizeOfSamplesAfterCompleteToPower2 = size
            return timePowerList to signalPowerList
        }
        var powered = 2.0
        var power = 0.0

        while (powered < size) {
            power += 1.0
            powered = Math.pow(2.0, power)
            println(powered)
        }

        if (powered.toInt() == size) {
            sizeOfSamplesAfterCompleteToPower2 = size
            return timePowerList to signalPowerList
        }
        val elementsDiff = powered - size
        val step = 0.1
        var current = tempTime.last()
        repeat(elementsDiff.toInt()) {
            current += step
            timePowerList.add(innerRound(current))
            signalPowerList.add(0.0)
        }
        sizeOfSamplesAfterCompleteToPower2 = timePowerList.size
        return timePowerList to signalPowerList
    }



    private fun innerRound(num: Double, decimals: Int = 2): Double =
        "%.${decimals}f".format(num).toDouble()

    fun getSummary(): String {
        var contnet = "----START PRINT FFT-DATA SUMMARY----\n"
        contnet += "sizeOfSamplesBeforeManipulation: $sizeOfSamplesBeforeManipulation \n"
        contnet += "sizeOfSamplesAfterAddingApproxiamateSignals: $sizeOfSamplesAfterAddingApproxiamateSignals \n"
        contnet += "sizeOfSamplesAfterCompleteToPower2: $sizeOfSamplesAfterCompleteToPower2 \n"
        contnet += "time to signal:\n"
        time.forEachIndexed { i, t ->
            contnet += "$t,${signal[i]}\n"
        }
        contnet += "time to signal:\n"
        allFrequencies.forEach {
            contnet += "$it , "
        }
        contnet += "\n"
        contnet += "----END  FFT-DATA SUMMARY----\n"
        return contnet
    }

    fun getTimeToSignalXlFormat(): String {
        var contnet =""
        time.forEachIndexed { i, t ->
            contnet += "$t,${signal[i]}\n"
        }
        return contnet
    }

}


fun Double.EXT_Round(decimals: Int = 2): Double {
    return try {
        "%.${decimals}f".format(this).toDouble()
    } catch (e: Exception) {

        val roundoff = (this * 100.0).roundToInt() / 100.0
        roundoff
    }

}