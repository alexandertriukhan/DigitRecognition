package neural

class NeuralNetwork {
    private var neurons : Array<Neuron> = Array(10,{Neuron()})

    // функция распознавания символа, используется для обучения
    fun handleHard(input : IntArray) : IntArray
    {
        var output = IntArray(neurons.size)
        for ((index, value) in neurons.withIndex()) {
            output[index] = value.transferHard(input)
        }
        return output
    }

    // функция распознавания символа, используется для конечного ответа
    fun handle(input : IntArray) : IntArray
    {
        var output = IntArray(neurons.size)
        for ((index, value) in neurons.withIndex()) {
            output[index] = value.transfer(input)
        }
        return output
    }

    // ответ сети
    fun getAnswer(input : IntArray) : Int
    {
        var output = handle(input)
        var maxIndex = 0
        for ((index) in output.withIndex()) {
            if (output[index] > output[maxIndex])
                maxIndex = index
        }
        return maxIndex
    }

    // функция обучения
    fun study(input : IntArray, correctAnswer : Int)
    {
        var correctOutput = IntArray(neurons.size)
        correctOutput[correctAnswer] = 1
        var output = handleHard(input)
        while(!(compareArrays(correctOutput,output)))
        {
            for ((index, value) in neurons.withIndex()) {
                val dif = correctOutput[index] - output[index]
                value.changeWeights(input,dif)
            }
            output = handleHard(input)
        }
    }

    // сравнение двух вектор
    fun compareArrays(a : IntArray,b : IntArray) : Boolean
    {
        if(a.size != b.size)
            return false
        for(i in a.indices)
        if(a[i] != b[i])
            return false
        return true
    }
}