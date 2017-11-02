package controller

import javafx.fxml.FXML
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import javafx.scene.control.Button

class MainController {

    @FXML
    lateinit var mainCanvas: Canvas

    @FXML
    lateinit var clearButton: Button

    lateinit var gc: GraphicsContext

    fun initialize() {
        gc = mainCanvas.graphicsContext2D

        mainCanvas.setOnMousePressed { event ->
            gc.beginPath()
            gc.moveTo(event.x, event.y)
            gc.stroke()
        }

        mainCanvas.setOnMouseDragged { event ->
            gc.lineTo(event.x, event.y)
            gc.stroke()
        }

        clearButton.setOnAction {
            gc.clearRect(0.0,0.0,280.0,280.0)
        }
    }

}