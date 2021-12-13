fun main() {

  fun part1(input: List<String>): Int {
    val grid = IntGrid(input)

    var flashes = 0
    for (i in 0 until 100) {
      for (dataPoint in grid) dataPoint.data++
      val flashablePoints = grid.filter { it.data == 10 }.toMutableList()
      // do flash
      while (flashablePoints.isNotEmpty()) {
        flashes++
        val flashablePoint = flashablePoints.removeFirst()
        grid.getSurroundingPoints(flashablePoint)
          .onEach { it.data++ }
          .filter { it.data == 10 }
          .forEach(flashablePoints::add)
      }
      grid.filter { it.data >= 10 }.forEach { it.data = 0 }
    }
    return flashes
  }

  fun part2(input: List<String>): Int {
    val grid = IntGrid(input)

    var step = 0
    while (true) {
      step++
      for (dataPoint in grid) dataPoint.data++
      val flashablePoints = grid.filter { it.data == 10 }.toMutableList()
      var maxFlashablePoints = flashablePoints.size
      // do flash
      while (flashablePoints.isNotEmpty()) {
        val flashablePoint = flashablePoints.removeFirst()
        val curFlashablePoints = grid.getSurroundingPoints(flashablePoint)
          .onEach { it.data++ }
          .filter { it.data == 10 }
        maxFlashablePoints += curFlashablePoints.count()
        curFlashablePoints.forEach(flashablePoints::add)
      }
      if (maxFlashablePoints == grid.size) break
      grid.filter { it.data >= 10 }.forEach { it.data = 0 }
    }
    return step
  }

  // test if implementation meets criteria from the description, like:
  val testInput = readInput("Day11_test")
  check(part1(testInput) == 1656)
  check(part2(testInput) == 195)

  val input = readInput("Day11")
  println(part1(input))
  println(part2(input))
}
