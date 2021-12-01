fun main() {

  fun part1(input: List<String>) = countIncrements(measurements = input.map { it.toInt() })

  fun part2(input: List<String>) = countIncrements(measurements = input.map { it.toInt() }, windowSize = 3)

  // test if implementation meets criteria from the description, like:
  val testInput = readInput("Day01_test")
  check(part1(testInput) == 7)
  check(part2(testInput) == 5)

  val input = readInput("Day01")
  println(part1(input))
  println(part2(input))
}

private fun countIncrements(measurements: List<Int>, windowSize: Int = 1) =
  measurements.dropLast(windowSize).withIndex()
    .count { (index, measurement) -> measurement < measurements[index + windowSize] }
