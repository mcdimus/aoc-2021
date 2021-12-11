fun main() {

  fun part1(input: List<String>) = Heightmap(input)
    .findLowPoints().sumOf { it.data + 1 }

  fun part2(input: List<String>) = Heightmap(input)
    .findBasins()
    .sortedByDescending { it.size }
    .take(3)
    .map { it.size }
    .reduce { acc, i -> acc * i }

  // test if implementation meets criteria from the description, like:
  val testInput = readInput("Day09_test")
  check(part1(testInput) == 15)
  check(part2(testInput) == 1134)

  val input = readInput("Day09")
  println(part1(input))
  println(part2(input))
}

class Heightmap(val input: List<String>) {

  private val height = input.size
  private val width = input.first().length

  fun findLowPoints(): List<DataPoint<Int>> {
    val lowPoints = mutableListOf<DataPoint<Int>>()

    for (y in 0 until height) {
      for (x in 0 until width) {
        val currentValue = input[y][x].digitToInt()
        val adjacentValues = getAdjacentDataPoints(y, x)
        if (adjacentValues.all { currentValue < it.data }) {
          lowPoints.add(DataPoint(x = x, y = y, data = currentValue))
        }
      }
    }

    return lowPoints
  }

  private fun getAdjacentDataPoints(y: Int, x: Int): List<DataPoint<Int>> {
    val up = input.getOrNull(y - 1)?.get(x)?.let { DataPoint(x = x, y = y - 1, data = it.digitToInt()) }
    val down = input.getOrNull(y + 1)?.get(x)?.let { DataPoint(x = x, y = y + 1, data = it.digitToInt()) }
    val left = input[y].getOrNull(x - 1)?.let { DataPoint(x = x - 1, y = y, data = it.digitToInt()) }
    val right = input[y].getOrNull(x + 1)?.let { DataPoint(x = x + 1, y = y, data = it.digitToInt()) }
    return listOfNotNull(up, down, left, right)
  }

  fun findBasins(): List<List<DataPoint<Int>>> {
    return findLowPoints().map { findBasin(it).toList() }
  }

  private fun findBasin(lowPoint: DataPoint<Int>): Set<DataPoint<Int>> {
    val visitedPoints = mutableSetOf<DataPoint<Int>>()
    val pointsToVisit = ArrayDeque<DataPoint<Int>>().also { it.add(lowPoint) }

    while (pointsToVisit.isNotEmpty()) {
      val currentPoint = pointsToVisit.removeFirst()
      val validAdjacentPoints = getAdjacentDataPoints(x = currentPoint.x, y = currentPoint.y)
        .filter { it.data > currentPoint.data && it.data != 9 }
      pointsToVisit.addAll(validAdjacentPoints)
      visitedPoints.add(currentPoint)
    }
    return visitedPoints
  }

}

data class DataPoint<T>(val x: Int, val y: Int, val data: T)
