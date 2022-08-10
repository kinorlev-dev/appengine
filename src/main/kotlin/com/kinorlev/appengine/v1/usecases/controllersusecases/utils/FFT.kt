package com.kinorlev.appengine.v1.usecases.controllersusecases.utils

import kotlin.math.ln

//https://www.ee.columbia.edu/~ronw/code/MEAPsoft/doc/html/FFT_8java-source.html
class FFT(var n: Int) {
    var m: Int

    // Lookup tables.  Only need to recompute when size of FFT changes.
    var cos: DoubleArray
    var sin: DoubleArray
    lateinit var window: DoubleArray

    init {
        m = (ln(n.toDouble()) / ln(2.0)).toInt()

        // Make sure n is a power of 2
        if (n != (1 shl m)) throw RuntimeException("FFT length must be power of 2 n= $n")


        // precompute tables
        cos = DoubleArray(n / 2)
        sin = DoubleArray(n / 2)

        //     for(int i=0; i<n/4; i++) {
        //       cos[i] = Math.cos(-2*Math.PI*i/n);
        //       sin[n/4-i] = cos[i];
        //       cos[n/2-i] = -cos[i];
        //       sin[n/4+i] = cos[i];
        //       cos[n/2+i] = -cos[i];
        //       sin[n*3/4-i] = -cos[i];
        //       cos[n-i]   = cos[i];
        //       sin[n*3/4+i] = -cos[i];
        //     }
        for (i in 0 until n / 2) {
            cos[i] = Math.cos(-2 * Math.PI * i / n)
            sin[i] = Math.sin(-2 * Math.PI * i / n)
        }
        makeWindow()
    }

    protected fun makeWindow() {
        // Make a blackman window:
        // w(n)=0.42-0.5cos{(2*PI*n)/(N-1)}+0.08cos{(4*PI*n)/(N-1)};
        window = DoubleArray(n)
        for (i in window.indices) window[i] = (0.42 - 0.5 * Math.cos(2 * Math.PI * i / (n - 1))
                + 0.08 * Math.cos(4 * Math.PI * i / (n - 1)))
    }

    /***************************************************************
     * fft.c
     * Douglas L. Jones
     * University of Illinois at Urbana-Champaign
     * January 19, 1992
     * http://cnx.rice.edu/content/m12016/latest/
     *
     * fft: in-place radix-2 DIT DFT of a complex input
     *
     * input:
     * n: length of FFT: must be a power of two
     * m: n = 2**m
     * input/output
     * x: double array of length n with real part of data
     * y: double array of length n with imag part of data
     *
     * Permission to copy and use this program is granted
     * as long as this header is included.
     */
    fun fft(x: DoubleArray, y: DoubleArray) {
        var i: Int
        var j: Int
        var k: Int
        var n1: Int
        var n2: Int
        var a: Int
        var c: Double
        var s: Double
        var e: Double
        var t1: Double
        var t2: Double


        // Bit-reverse
        j = 0
        n2 = n / 2
        i = 1
        while (i < n - 1) {
            n1 = n2
            while (j >= n1) {
                j = j - n1
                n1 = n1 / 2
            }
            j = j + n1
            if (i < j) {
                t1 = x[i]
                x[i] = x[j]
                x[j] = t1
                t1 = y[i]
                y[i] = y[j]
                y[j] = t1
            }
            i++
        }

        // FFT
        n1 = 0
        n2 = 1
        i = 0
        while (i < m) {
            n1 = n2
            n2 = n2 + n2
            a = 0
            j = 0
            while (j < n1) {
                c = cos[a]
                s = sin[a]
                a += 1 shl m - i - 1
                k = j
                while (k < n) {
                    t1 = c * x[k + n1] - s * y[k + n1]
                    t2 = s * x[k + n1] + c * y[k + n1]
                    x[k + n1] = x[k] - t1
                    y[k + n1] = y[k] - t2
                    x[k] = x[k] + t1
                    y[k] = y[k] + t2
                    k = k + n2
                }
                j++
            }
            i++
        }
    }

    companion object {
        // Test the FFT to make sure it's working
        @JvmStatic
        fun main(args: Array<String>) {
            val N = 32
            val fft = FFT(N)
            val window = fft.window
            val re = DoubleArray(N)
            val im = DoubleArray(N)

            // Impulse
            re[0] = 1.0
            im[0] = 0.0
            for (i in 1 until N) {
                re[i] = Math.sin(2 * Math.PI * 0.1 * i)
                im[i] = 0.0
                //re[i] = im[i] = 0;
            }
            beforeAfter(fft, re, im)

            // Nyquist
            for (i in 0 until N) {
                re[i] = Math.pow(-1.0, i.toDouble())
                im[i] = 0.0
            }
            beforeAfter(fft, re, im)

            // Single sin
            for (i in 0 until N) {
                re[i] = Math.cos(2 * Math.PI * i / N)
                im[i] = 0.0
            }
            beforeAfter(fft, re, im)

            // Ramp
            for (i in 0 until N) {
                re[i] = i.toDouble()
                im[i] = 0.0
            }
            beforeAfter(fft, re, im)
            var time = System.currentTimeMillis()
            val iter = 30000.0
            var i = 0
            while (i < iter) {
                fft.fft(re, im)
                i++
            }
            time = System.currentTimeMillis() - time
            println("Averaged " + (time / iter) + "ms per iteration")
        }

        protected fun beforeAfter(fft: FFT, re: DoubleArray, im: DoubleArray) {
            println("Before: ")
            printReIm(re, im)
            fft.fft(re, im)
            println("After: ")
            printReIm(re, im)
        }

        protected fun printReIm(re: DoubleArray, im: DoubleArray) {
            print("Re: [")
            for (i in re.indices) print(((re[i] * 1000).toInt() / 1000.0).toString() + " ")
            print("]\nIm: [")
            for (i in im.indices) print(((im[i] * 1000).toInt() / 1000.0).toString() + " ")
            println("]")
        }
    }
}