package com.kinorlev.appengine.v1.controllers.utils

/******************************************************************************
 * Compilation:  javac FFT.java
 * Execution:    java FFT n
 * Dependencies: Complex.java
 *
 * Compute the FFT and inverse FFT of a length n complex sequence
 * using the radix 2 Cooley-Tukey algorithm.
 *
 * Bare bones implementation that runs in O(n log n) time and O(n)
 * space. Our goal is to optimize the clarity of the code, rather
 * than performance.
 *
 * This implementation uses the primitive root of unity w = e^(-2 pi i / n).
 * Some resources use w = e^(2 pi i / n).
 *
 * Reference: https://www.cs.princeton.edu/~wayne/kleinberg-tardos/pdf/05DivideAndConquerII.pdf
 *
 * Limitations
 * -----------
 * -  assumes n is a power of 2
 *
 * -  not the most memory efficient algorithm (because it uses
 * an object type for representing complex numbers and because
 * it re-allocates memory for the subarray, instead of doing
 * in-place or reusing a single temporary array)
 *
 * For an in-place radix 2 Cooley-Tukey FFT, see
 * https://introcs.cs.princeton.edu/java/97data/InplaceFFT.java.html
 *
 */
object FFT {


    // compute the FFT of x[], assuming its length n is a power of 2

    fun fft(resource: Array<Complex?>): Array<Complex?> {
        var x : Array<Complex?> = resource
        if (x.size % 2 != 0){
            x = x.copyOf(x.size -1)
        }


        val n = x.size

        // base case
        if (n == 1) return arrayOf<Complex?>(x[0])

        // radix 2 Cooley-Tukey FFT
        require(n % 2 == 0) { "n is not a power of 2" }


        // compute FFT of even terms
        val even: Array<Complex?> = arrayOfNulls<Complex>(n / 2)
        for (k in 0 until n / 2) {
            even[k] = x[2 * k]
        }
        val evenFFT: Array<Complex?> = fft(even)

        // compute FFT of odd terms
        val odd: Array<Complex?> = even // reuse the array (to avoid n log n space)
        for (k in 0 until n / 2) {
            odd[k] = x[2 * k + 1]
        }
        val oddFFT: Array<Complex?> = fft(odd)

        // combine
        val y: Array<Complex?> = arrayOfNulls<Complex>(n)
        for (k in 0 until n / 2) {
            val kth = -2 * k * Math.PI / n
            val wk = Complex(Math.cos(kth), Math.sin(kth))
            y[k] = oddFFT[k]?.let { wk.times(it) }?.let { evenFFT[k]!!.plus(it) }
            y[k + n / 2] = oddFFT[k]?.let { wk.times(it) }?.let { evenFFT[k]?.minus(it) }
        }
        return y
    }

    // compute the inverse FFT of x[], assuming its length n is a power of 2
    fun ifft(x: Array<Complex?>): Array<Complex?> {
        val n = x.size
        var y: Array<Complex?> = arrayOfNulls<Complex>(n)

        // take conjugate
        for (i in 0 until n) {
            y[i] = x[i]?.conjugate()
        }

        // compute forward FFT
        y = fft(y)

        // take conjugate again
        for (i in 0 until n) {
            y[i] = y[i]?.conjugate()
        }

        // divide by n
        for (i in 0 until n) {
            y[i] = y[i]?.scale(1.0 / n)
        }
        return y
    }

    // compute the circular convolution of x and y
    fun cconvolve(x: Array<Complex?>, y: Array<Complex?>): Array<Complex?> {

        // should probably pad x and y with 0s so that they have same length
        // and are powers of 2
        require(x.size == y.size) { "Dimensions don't agree" }
        val n = x.size

        // compute FFT of each sequence
        val a: Array<Complex?> = fft(x)
        val b: Array<Complex?> = fft(y)

        // point-wise multiply
        val c: Array<Complex?> = arrayOfNulls<Complex>(n)
        for (i in 0 until n) {
            c[i] = b[i]?.let { a[i]?.times(it) }
        }

        // compute inverse FFT
        return ifft(c)
    }

    // compute the linear convolution of x and y
    fun convolve(x: Array<Complex?>, y: Array<Complex?>): Array<Complex?> {
        val ZERO = Complex(0.0, 0.0)
        val a: Array<Complex?> = arrayOfNulls<Complex>(2 * x.size)
        for (i in x.indices) a[i] = x[i]
        for (i in x.size until 2 * x.size) a[i] = ZERO
        val b: Array<Complex?> = arrayOfNulls<Complex>(2 * y.size)
        for (i in y.indices) b[i] = y[i]
        for (i in y.size until 2 * y.size) b[i] = ZERO
        return cconvolve(a, b)
    }

    // compute the DFT of x[] via brute force (n^2 time)
    fun dft(x: Array<Complex?>): Array<Complex?> {
        val n = x.size
        val ZERO = Complex(0.0, 0.0)
        val y: Array<Complex?> = arrayOfNulls<Complex>(n)
        for (k in 0 until n) {
            y[k] = ZERO
            for (j in 0 until n) {
                val power = k * j % n
                val kth = -2 * power * Math.PI / n
                val wkj = Complex(Math.cos(kth), Math.sin(kth))
                y[k] = x[j]?.let { y[k]?.plus(it.times(wkj)) }
            }
        }
        return y
    }

    // display an array of Complex numbers to standard output
    fun show(x: Array<Complex?>, title: String?) {
        println(title)
        println("-------------------")
        for (i in x.indices) {
            println(x[i])
        }
        println()
    }

    /***************************************************************************
     * Test client and sample execution
     *
     * % java FFT 4
     * x
     * -------------------
     * -0.03480425839330703
     * 0.07910192950176387
     * 0.7233322451735928
     * 0.1659819820667019
     *
     * y = fft(x)
     * -------------------
     * 0.9336118983487516
     * -0.7581365035668999 + 0.08688005256493803i
     * 0.44344407521182005
     * -0.7581365035668999 - 0.08688005256493803i
     *
     * z = ifft(y)
     * -------------------
     * -0.03480425839330703
     * 0.07910192950176387 + 2.6599344570851287E-18i
     * 0.7233322451735928
     * 0.1659819820667019 - 2.6599344570851287E-18i
     *
     * c = cconvolve(x, x)
     * -------------------
     * 0.5506798633981853
     * 0.23461407150576394 - 4.033186818023279E-18i
     * -0.016542951108772352
     * 0.10288019294318276 + 4.033186818023279E-18i
     *
     * d = convolve(x, x)
     * -------------------
     * 0.001211336402308083 - 3.122502256758253E-17i
     * -0.005506167987577068 - 5.058885073636224E-17i
     * -0.044092969479563274 + 2.1934338938072244E-18i
     * 0.10288019294318276 - 3.6147323062478115E-17i
     * 0.5494685269958772 + 3.122502256758253E-17i
     * 0.240120239493341 + 4.655566391833896E-17i
     * 0.02755001837079092 - 2.1934338938072244E-18i
     * 4.01805098805014E-17i
     *
     */
    @JvmStatic
    fun main(args: Array<String>) {
        val n = args[0].toInt()
        val x: Array<Complex?> = arrayOfNulls<Complex>(n)

        // original data
        for (i in 0 until n) {
            x[i] = Complex(i.toDouble(), 0.0)
        }
        show(x, "x")

        // FFT of original data
        val y: Array<Complex?> = fft(x)
        show(y, "y = fft(x)")

        // FFT of original data
        val y2: Array<Complex?> = dft(x)
        show(y2, "y2 = dft(x)")

        // take inverse FFT
        val z: Array<Complex?> = ifft(y)
        show(z, "z = ifft(y)")

        // circular convolution of x with itself
        val c: Array<Complex?> = cconvolve(x, x)
        show(c, "c = cconvolve(x, x)")

        // linear convolution of x with itself
        val d: Array<Complex?> = convolve(x, x)
        show(d, "d = convolve(x, x)")
    }
}

fun getFrequenciesFromFft(): FrequencyResponse{

}

data class FrequencyResponse (
        var hrAmount: Int,

        )