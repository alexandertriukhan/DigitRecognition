package controller

import javafx.fxml.FXML
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import javafx.scene.control.Button
import core.Main
import javafx.embed.swing.SwingFXUtils
import javafx.scene.SnapshotParameters
import javafx.scene.control.Label
import javafx.scene.control.ProgressBar
import javafx.scene.transform.Transform
import javafx.scene.effect.BoxBlur
import javafx.scene.image.*
import neural.NeuralNetwork
import utils.DigitImage
import utils.MnistLoader
import java.io.BufferedWriter
import java.io.FileWriter
import java.io.File


class MainController {

    @FXML
    lateinit var mainCanvas: Canvas

    @FXML
    lateinit var clearButton: Button

    @FXML
    lateinit var studyButton: Button

    @FXML
    lateinit var detectButton: Button

    @FXML
    lateinit var progressBar: ProgressBar

    @FXML
    lateinit var answerLabel: Label

    lateinit var gc: GraphicsContext

    private val mnistLoader: MnistLoader = MnistLoader("/resources/mnist/train-labels.idx1-ubyte",
            "/resources/mnist/train-images.idx3-ubyte")
    private val neuralNetwork = NeuralNetwork()
    private val digitImage: List<DigitImage> = mnistLoader.loadDigitImages()

    val m = Main()

    fun initialize() {
        val blur = BoxBlur()
        blur.width = 2.0
        blur.height = 2.0
        blur.iterations = 2
        gc = mainCanvas.graphicsContext2D
        gc.lineWidth = 7.0

            mainCanvas.setOnMousePressed { event ->
            gc.setEffect(blur)
            gc.beginPath()
            gc.moveTo(event.x, event.y)
            gc.stroke()
        }

        mainCanvas.setOnMouseDragged { event ->
            gc.lineTo(event.x, event.y)
            gc.stroke()
        }

        clearButton.setOnAction {
            gc.setEffect(null)
            gc.clearRect(0.0,0.0,280.0,280.0)
            gc.setEffect(blur)
        }

        studyButton.setOnAction {
            for (item in digitImage) {
                neuralNetwork.study(item.imgData,item.label)
            }
            progressBar.progress = 1.0
        }

        detectButton.setOnAction {
            val img = imageToArray(getCanvasImage())
            val answer = neuralNetwork.getAnswer(img)
            answerLabel.text = "Input is identified as " + answer
        }
    }

    private fun getCanvasImage(): Image {
        val image = WritableImage(mainCanvas.width.toInt(),mainCanvas.height.toInt())
        val sp = SnapshotParameters()
        sp.transform = Transform.scale(1.0,1.0)
        return scale(mainCanvas.snapshot(sp,image),28,28)
    }

    private fun scale(source: Image, targetWidth: Int, targetHeight: Int): Image {
        val imageView = ImageView(source)
        imageView.fitWidth = targetWidth.toDouble()
        imageView.fitHeight = targetHeight.toDouble()
        return imageView.snapshot(null, null)
    }

    private fun imageToArray(source: Image): IntArray {
        val img = SwingFXUtils.fromFXImage(source,null)
        var result = IntArray(784)
        var counter = 0
        for (x in 0..(img.width - 1)) {
            for (y in 0..(img.height - 1)) {
                val clr = img.getRGB(y, x)
                val red = clr and 0x00ff0000 shr 16
                val green = clr and 0x0000ff00 shr 8
                val blue = clr and 0x000000ff
                if (red == 255 && green == 255 && blue == 255) {
                    result[counter] = 0
                } else {
                    result[counter] = 1
                }
                counter++
            }
        }
        return result
    }

    fun digitImageToText() {
        val file = File("test.txt")
        val fileWriter = BufferedWriter(FileWriter(file))
        for(i1 in 0..100) {
            fileWriter.write(digitImage[i1].label.toString())
            fileWriter.newLine()
            for (i in 0..783) {
                fileWriter.write(digitImage[i1].imgData[i].toString())
                if (i % 28 == 0) {
                    fileWriter.newLine()
                }
            }
            fileWriter.newLine()
            fileWriter.newLine()
        }
        fileWriter.flush()
        fileWriter.close()
    }

    fun intArrToText(input: IntArray) {
        val file = File("test.txt")
        val fileWriter = BufferedWriter(FileWriter(file))
            for (i in 0..783) {
                fileWriter.write(input[i].toString())
                if (i % 28 == 0) {
                    fileWriter.newLine()
                }
            }
            fileWriter.newLine()
            fileWriter.newLine()
        fileWriter.flush()
        fileWriter.close()
    }

}