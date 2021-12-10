import kotlin.math.abs
import kotlin.math.min

fun main() {

  fun part1(input: List<String>): Int {
    val crabPositions = input.single().split(',').map { it.toInt() }.sorted()
    return if (crabPositions.size % 2 == 1) {
      val median = crabPositions.median()
      crabPositions.sumOf { abs(it - median) }
    } else {
      min(
        crabPositions.sumOf { abs(it - crabPositions[crabPositions.size / 2]) },
        crabPositions.sumOf { abs(it - crabPositions[crabPositions.size / 2 + 1]) }
      )
    }
  }

  fun part2(input: List<String>): Int {
    val crabPositions = input.single().split(',').map { it.toInt() }.sorted()
    val min = crabPositions.first()
    val max = crabPositions.last()

    val fuelConsumptionForEveryPosition = Array(max - min + 1) { 0 }

    for (crabPosition in crabPositions) {
      fuelConsumptionForEveryPosition.withIndex().forEach { (index, _) ->
        fuelConsumptionForEveryPosition[index] += calculateConsumption(from = crabPosition, to = index)
      }
    }

    return fuelConsumptionForEveryPosition.minOrNull()!!
  }

  // test if implementation meets criteria from the description, like:
  val testInput = readInput("Day07_test")
  check(part1(testInput) == 37)
  check(part2(testInput) == 168)

  val input = readInput("Day07")
  println(part1(input))
  println(part2(input))
}

fun List<Int>.median() = when {
  isEmpty() -> -1
  size == 1 -> single()
  size % 2 == 1 -> sorted()[size / 2]
  else -> sorted().let { (it[size / 2] + it[size / 2 + 1]) / 2 }
}

fun calculateConsumption(from: Int, to: Int): Int {
  return (0..abs(from - to)).fold(initial = 0) { acc, index -> acc + index }
}
