package core
import javafx.application.Application
import javafx.fxml.FXMLLoader.load
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.stage.Stage
import utils.DigitImage
import utils.MnistLoader

class Main : Application() {

    private val layout = "/resources/main.fxml"

    override fun start(primaryStage: Stage?) {
        primaryStage?.scene = Scene(load<Parent?>(Main.javaClass.getResource(layout)))
        primaryStage?.show()
        val mnistLoader: MnistLoader = MnistLoader("/resources/mnist/train-labels.idx1-ubyte",
                "/resources/mnist/train-images.idx3-ubyte")
        val digitImage: List<DigitImage> = mnistLoader.loadDigitImages()
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            launch(Main::class.java)
        }
    }

}