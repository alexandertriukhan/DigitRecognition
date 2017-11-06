package core
import javafx.application.Application
import javafx.fxml.FXMLLoader.load
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.stage.Stage
import neural.NeuralNetwork
import utils.DigitImage
import utils.MnistLoader

class Main : Application() {

    private val layout = "/resources/main.fxml"
    private val mnistLoader: MnistLoader = MnistLoader("/resources/mnist/train-labels.idx1-ubyte",
            "/resources/mnist/train-images.idx3-ubyte")
    private val neuralNetwork = NeuralNetwork()
    private val digitImage: List<DigitImage> = mnistLoader.loadDigitImages()

    override fun start(primaryStage: Stage?) {
        primaryStage?.scene = Scene(load<Parent?>(Main.javaClass.getResource(layout)))
        primaryStage?.show()
    }

    fun studyMnist() {
        for (i in digitImage) {
            neuralNetwork.study(i.imgData, i.label)
        }
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            launch(Main::class.java)
        }
    }

}