//https://github.com/vivin/DigitRecognizingNeuralNetwork
package utils

import java.io.IOException
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer
import java.util.ArrayList


class MnistLoader(private val labelFileName: String, private val imageFileName: String) {

    private val OFFSET_SIZE = 4 //in bytes

    private val LABEL_MAGIC = 2049
    private val IMAGE_MAGIC = 2051

    private val NUMBER_ITEMS_OFFSET = 4
    private val ITEMS_SIZE = 4

    private val NUMBER_OF_ROWS_OFFSET = 8
    private val ROWS_SIZE = 4
    val ROWS = 28

    private val NUMBER_OF_COLUMNS_OFFSET = 12
    private val COLUMNS_SIZE = 4
    val COLUMNS = 28

    private val IMAGE_OFFSET = 16
    private val IMAGE_SIZE = ROWS * COLUMNS

    @Throws(IOException::class)
    fun loadDigitImages(): List<DigitImage> {
        val images = ArrayList<DigitImage>()

        val labelBuffer = ByteArrayOutputStream()
        val imageBuffer = ByteArrayOutputStream()

        val labelInputStream = this.javaClass.getResourceAsStream(labelFileName)
        val imageInputStream = this.javaClass.getResourceAsStream(imageFileName)

        var read: Int
        val buffer = ByteArray(16384)

        do {
            read = labelInputStream.read(buffer, 0, buffer.size)
            if (read == -1)
                break
            labelBuffer.write(buffer, 0, read)
        } while (true)
        labelBuffer.flush()

        do {
            read = imageInputStream.read(buffer, 0, buffer.size)
            if (read == -1)
                break
            imageBuffer.write(buffer, 0, read)
        } while (true)
        imageBuffer.flush()

        val labelBytes = labelBuffer.toByteArray()
        val imageBytes = imageBuffer.toByteArray()

        val labelMagic = labelBytes.copyOfRange(0, OFFSET_SIZE)
        val imageMagic = imageBytes.copyOfRange(0, OFFSET_SIZE)

        if (ByteBuffer.wrap(labelMagic).int != LABEL_MAGIC) {
            throw IOException("Bad magic number in label file!")
        }

        if (ByteBuffer.wrap(imageMagic).int != IMAGE_MAGIC) {
            throw IOException("Bad magic number in image file!")
        }

        val numberOfLabels = ByteBuffer.wrap(labelBytes.copyOfRange(NUMBER_ITEMS_OFFSET, NUMBER_ITEMS_OFFSET + ITEMS_SIZE)).getInt()
        val numberOfImages = ByteBuffer.wrap(imageBytes.copyOfRange(NUMBER_ITEMS_OFFSET, NUMBER_ITEMS_OFFSET + ITEMS_SIZE)).getInt()

        if (numberOfImages != numberOfLabels) {
            throw IOException("The number of labels and images do not match!")
        }

        val numRows = ByteBuffer.wrap(imageBytes.copyOfRange(NUMBER_OF_ROWS_OFFSET, NUMBER_OF_ROWS_OFFSET + ROWS_SIZE)).getInt()
        val numCols = ByteBuffer.wrap(imageBytes.copyOfRange(NUMBER_OF_COLUMNS_OFFSET, NUMBER_OF_COLUMNS_OFFSET + COLUMNS_SIZE)).getInt()

        if (numRows != ROWS && numCols != COLUMNS) {
            throw IOException("Bad image. Rows and columns do not equal " + ROWS + "x" + COLUMNS)
        }

        for (i in 0 until numberOfLabels) {
            val label = labelBytes[OFFSET_SIZE + ITEMS_SIZE + i].toInt()
            val imageData: ByteArray = imageBytes.copyOfRange(i * IMAGE_SIZE + IMAGE_OFFSET, i * IMAGE_SIZE + IMAGE_OFFSET + IMAGE_SIZE)

            images.add(DigitImage(label, imageData))
        }

        return images
    }

}