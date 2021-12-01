fun main() {
  fun part1(input: List<String>): Int {
    val measurements = input.map { it.toInt() }
    var counterOfIncreases = 0
    for (i in measurements.indices) {
      if (measurements.getOrElse(i - 1) { Int.MAX_VALUE } < measurements[i]) counterOfIncreases++
    }
    return counterOfIncreases
  }

  fun part2(input: List<String>): Int {
    val measurements = input.map { it.toInt() }
    val windowSize = 3
    var counterOfIncreases = 0

    var prevSum = Int.MAX_VALUE
    var currentSum: Int
    for (i in 0..(measurements.size - measurements.size % windowSize)) {
      currentSum = measurements.drop(i).take(windowSize).sum()
      if (prevSum < currentSum) counterOfIncreases++
      prevSum = currentSum
    }
    return counterOfIncreases
  }

  // test if implementation meets criteria from the description, like:
  val testInput = readInput("Day01_test")
  check(part1(testInput) == 7)
  check(part2(testInput) == 5)

  val input = readInput("Day01")
  println(part1(input))
  println(part2(input))
}
