package neural

import java.util.*

class Neuron {
    private var weight : IntArray = IntArray(784) // веса нейронов
    private val minimum = 55 // порог

    init {
        randomizeWeights()
    }

    fun transferHard(input : IntArray) : Int {
        var power = 0
        for (i in weight.indices) {
                power += weight[i] * input[i]
            }
        if (power >= minimum) {
            return 1
        } else {
            return 0
        }
    }

    fun transfer(input : IntArray) : Int {
        var power = 0
        for (i in weight.indices) {
            power += weight[i] * input[i]
        }
        return power
    }

    private fun randomizeWeights() {
        for (i in weight.indices) {
            weight[i] = (0..10).random()
        }
    }

    fun ClosedRange<Int>.random() = Random().nextInt(endInclusive - start) +  start

    fun changeWeights(input : IntArray, d : Int) {
        for (i in weight.indices) {
            weight[i] += d * input[i]
        }
    }
}