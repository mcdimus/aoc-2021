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
