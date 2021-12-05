import kotlin.math.max
import kotlin.math.sign

fun main() {

  fun part1(input: List<String>) = input.asSequence()
    .map { Line.of(it) }
    .filter { it.isStraight() }
    .flatMap { it.getAllPoints() }
    .groupingBy { it }
    .eachCount()
    .count { it.value > 1 }

  fun part2(input: List<String>) = input.asSequence()
    .map { Line.of(it) }
    .flatMap { it.getAllPoints() }
    .groupingBy { it }
    .eachCount()
    .count { it.value > 1 }

  // test if implementation meets criteria from the description, like:
  val testInput = readInput("Day05_test")
  check(part1(testInput) == 5)
  check(part2(testInput) == 12)

  val input = readInput("Day05")
  println(part1(input))
  println(part2(input))
}

data class Point(val x: Int, val y: Int) {

  companion object {

    fun of(input: String) = input.split(",").map { it.trim().toInt() }.let { Point(x = it[0], y = it[1]) }

  }

}

data class Line(val start: Point, val end: Point) {

  fun isHorizontal() = start.x == end.x
  fun isVertical() = start.y == end.y
  fun isStraight() = isHorizontal() || isVertical()

  fun getAllPoints(): List<Point> {
    return when {
      isHorizontal() -> IntProgression.fromClosedRange(start.y, end.y, step = (end.y - start.y).sign)
        .map { Point(x = start.x, y = it) }
      isVertical() -> IntProgression.fromClosedRange(start.x, end.x, step = (end.x - start.x).sign)
        .map { Point(x = it, y = start.y) }
      else -> {
        val xRange = IntProgression.fromClosedRange(start.x, end.x, step = (end.x - start.x).sign)
        val yRange = IntProgression.fromClosedRange(start.y, end.y, step = (end.y - start.y).sign)
        val count = max(xRange.count(), yRange.count())
        (0 until count).map { Point(x = xRange.elementAt(it), y = yRange.elementAt(it)) }
      }
    }
  }

  companion object {

    fun of(input: String) = input.split("->").map { Point.of(it) }.let { Line(start = it[0], end = it[1]) }

  }

}
