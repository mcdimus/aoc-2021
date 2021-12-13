import java.io.File
import java.math.BigInteger
import java.security.MessageDigest

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = File("src", "$name.txt").readLines()

/**
 * Converts string to md5 hash.
 */
fun String.md5(): String = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray())).toString(16)

fun <T> List<T>.getRows(size: Int): List<List<T>> = this.windowed(size = size, step = size, partialWindows = true)

fun <T> List<T>.getColumns(size: Int): List<List<T>> {
  val columns = mutableListOf<MutableList<T>>()
  for (colIndex in (0 until size)) {
    val currentColumn = mutableListOf<T>()
    for (j in (0 until size)) {
      currentColumn.add(this[colIndex + j * size])
    }
    columns.add(currentColumn)
  }
  return columns
}

data class MutableDataPoint<T>(val x: Int, val y: Int, var data: T)

class IntGrid(input: List<String>) : Iterable<MutableDataPoint<Int>> {

  val height = input.size
  val width = input.first().length
  val size = height * width
  private val values = input.mapIndexed { y, row ->
    row.toCharArray().mapIndexed { x, c -> MutableDataPoint(x = x, y = y, data = c.digitToInt()) }
  }

  override fun iterator(): Iterator<MutableDataPoint<Int>> = IteratorImpl()

  fun getAdjacentDataPoints(point: MutableDataPoint<Int>) = getAdjacentDataPoints(x = point.x, y = point.y)
  fun getAdjacentDataPoints(x: Int, y: Int): List<MutableDataPoint<Int>> {
    val up = values.getOrNull(y - 1)?.get(x)
    val down = values.getOrNull(y + 1)?.get(x)
    val left = values[y].getOrNull(x - 1)
    val right = values[y].getOrNull(x + 1)
    return listOfNotNull(up, down, left, right)
  }

  fun getSurroundingPoints(point: MutableDataPoint<Int>) = getSurroundingPoints(x = point.x, y = point.y)
  fun getSurroundingPoints(x: Int, y: Int): List<MutableDataPoint<Int>> {
    val upLeft = values.getOrNull(y - 1)?.getOrNull(x - 1)
    val upRight = values.getOrNull(y - 1)?.getOrNull(x + 1)
    val downLeft = values.getOrNull(y + 1)?.getOrNull(x - 1)
    val downRight = values.getOrNull(y + 1)?.getOrNull(x + 1)
    return getAdjacentDataPoints(x = x, y = y) + listOfNotNull(upLeft, upRight, downLeft, downRight)
  }

  private inner class IteratorImpl : Iterator<MutableDataPoint<Int>> {

    private var index = 0

    override fun hasNext(): Boolean = index < height * width

    override fun next(): MutableDataPoint<Int> {
      if (!hasNext()) throw NoSuchElementException()
      val y = index / width
      val x = index - y * width
      index++
      return values[y][x]
    }
  }

}
