fun main() {

  fun part1(input: List<String>) = input.toCaves()
    .single { it.value == "start" }
    .findAllPathsTo("end")
    .count()

  fun part2(input: List<String>) = input.toCaves()
    .single { it.value == "start" }
    .findAllPathsTo2("end")
    .count()

  // test if implementation meets criteria from the description, like:
  val testInput = readInput("Day12_test")
  check(part1(testInput) == 19)
  check(part2(testInput) == 103)

  val input = readInput("Day12")
  println(part1(input))
  println(part2(input))
}

fun List<String>.toCaves(): List<Cave> {
  val caves = mutableListOf<Cave>()

  map { it.split('-') }.map { (caveValue1, caveValue2) ->
    val cave1 = caves.singleOrNull { it.value == caveValue1 } ?: Cave(caveValue1).also { caves.add(it) }
    val cave2 = caves.singleOrNull { it.value == caveValue2 } ?: Cave(caveValue2).also { caves.add(it) }

    cave1.adjacentCaves.add(cave2)
    cave2.adjacentCaves.add(cave1)
  }

  return caves
}

class Cave(val value: String) {

  val isSmall = value == value.lowercase()
  val adjacentCaves = mutableListOf<Cave>()

  override fun toString() = value

  fun findAllPathsTo(target: String, currentPath: List<String> = emptyList()): List<List<String>> {
    if (target == value) return listOf(currentPath + value)

    val cavesToVisit = adjacentCaves.filterNot { it.isSmall && (it.value in currentPath) }
    if (cavesToVisit.isEmpty()) return emptyList()
    return cavesToVisit.flatMap { it.findAllPathsTo(target, currentPath = currentPath + value) }
  }

  fun findAllPathsTo2(target: String, currentPath: List<String> = emptyList()): List<List<String>> {
    if (target == value) return listOf(currentPath + value)

    val cavesToVisit = adjacentCaves.filter { isAllowedToVisit(it.value, currentPath + value) }
    if (cavesToVisit.isEmpty()) return emptyList()
    return cavesToVisit.flatMap { it.findAllPathsTo2(target, currentPath = currentPath + value) }
  }

  private fun isAllowedToVisit(value: String, currentPath: List<String>): Boolean {
    val smallCavesCount = currentPath.filter { it == it.lowercase() }.groupingBy { it }.eachCount()

    return when {
      value == "start" && smallCavesCount["start"] == 1 -> false
      value == "end" && smallCavesCount["end"] == 1 -> false
      value == value.lowercase() && (smallCavesCount[value] ?: 0) >= 1 && smallCavesCount.values.contains(2) -> false
      else -> true
    }
  }

}
