//https://github.com/vivin/DigitRecognizingNeuralNetwork
package utils

class DigitImage(private val label: Int, imgData: ByteArray) {
    val imgData: DoubleArray = DoubleArray(imgData.size, { i -> (imgData[i].toInt() and 0xFF).toDouble() })

    init {
        otsu()
    }

    private fun otsu() {
        val histogram = IntArray(256)

        for (datum in imgData) {
            histogram[datum.toInt()]++
        }

        var sum = 0.0
        for (i in histogram.indices) {
            sum += (i * histogram[i]).toDouble()
        }

        var sumB = 0.0
        var wB = 0
        var wF = 0

        var maxVariance = 0.0
        var threshold = 0

        var i = 0
        var found = false

        while (i < histogram.size && !found) {
            wB += histogram[i]

            if (wB != 0) {
                wF = imgData.size - wB

                if (wF != 0) {
                    sumB += (i * histogram[i]).toDouble()

                    val mB = sumB / wB
                    val mF = (sum - sumB) / wF

                    val varianceBetween = wB * Math.pow(mB - mF, 2.0)

                    if (varianceBetween > maxVariance) {
                        maxVariance = varianceBetween
                        threshold = i
                    }
                } else {
                    found = true
                }
            }

            i++
        }
        i = 0
        while (i < imgData.size) {
            imgData[i] = if (imgData[i] <= threshold) 0.0 else 1.0
            i++
        }
    }
}