fun main() {

  fun getCaves(input: List<String>): MutableList<Cave> {
    val caves = mutableListOf<Cave>()

    input.map { it.split('-') }.map { caveValues ->
      val caveValue1 = caveValues[0]
      val caveValue2 = caveValues[1]

      val cave1 = caves.singleOrNull { it.value == caveValue1 } ?: Cave(caveValue1)
      val cave2 = caves.singleOrNull { it.value == caveValue2 } ?: Cave(caveValue2)

      cave1.adjacentCaves.add(cave2)
      cave2.adjacentCaves.add(cave1)
      caves.singleOrNull { it.value == cave1.value } ?: caves.add(cave1)
      caves.singleOrNull { it.value == cave2.value } ?: caves.add(cave2)
    }
    return caves
  }

  fun part1(input: List<String>) = getCaves(input)
    .single { it.value == "start" }
    .findAllPathsTo("end")
    .count()

  fun part2(input: List<String>) = getCaves(input)
    .single { it.value == "start" }
    .findAllPathsTo2("end")
    .map { it.joinToString(",") }
    .sorted()
    .onEach { println(it) }
    .count()

  // test if implementation meets criteria from the description, like:
  val testInput = readInput("Day12_test")
  check(part1(testInput) == 19)
  check(part2(testInput) == 103)

  val input = readInput("Day12")
  println(part1(input))
  println(part2(input))
}

class Cave(val value: String) {

  private var visits = 0
  private val isLarge = value == value.uppercase()
  private val isSmall = value == value.lowercase()

  val adjacentCaves = mutableListOf<Cave>()

  fun visit() {
    visits++
  }

  override fun toString(): String {
    return value
  }

  fun findAllPathsTo(target: String, currentPath: List<String> = emptyList()): List<List<String>> {
    visit()
    if (target == value) {
      return listOf(currentPath + value)
    }

    val cavesToVisit = adjacentCaves.filterNot { it.isSmall && (it.value in currentPath) }
    if (cavesToVisit.isEmpty()) {
      return emptyList()
    }
    return cavesToVisit
      .flatMap { it.findAllPathsTo(target, currentPath = currentPath + value) }
  }

  fun findAllPathsTo2(target: String, currentPath: List<String> = emptyList()): List<List<String>> {
    visit()
    if (target == value) {
      return listOf(currentPath + value)
    }

    val cavesToVisit = adjacentCaves.filter { isAllowedToVisit(it.value, currentPath + value) }
    if (cavesToVisit.isEmpty()) {
      return emptyList()
    }
    return cavesToVisit
      .flatMap { it.findAllPathsTo2(target, currentPath = currentPath + value) }
  }

  fun isAllowedToVisit(value: String, currentPath: List<String>): Boolean {
    val smallCavesCount = currentPath.filter { it == it.lowercase() }.groupingBy { it }.eachCount()

    return when {
      value == "start" && smallCavesCount["start"] == 1 -> false
      value == "end" && smallCavesCount["end"] == 1 -> false
      value == value.lowercase() && (smallCavesCount[value] ?: 0) >= 1 && smallCavesCount.values.contains(2) -> false
      else -> true
    }
  }

}
