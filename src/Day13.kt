fun main() {

  fun part1(input: List<String>): Int {
    val givenPoints = input.takeWhile { it.isNotBlank() }.map { Point.of(it) }
    val foldInstruction = input.dropWhile { it.isNotBlank() }
      .map { it.removePrefix("fold along ") }
      .first { it.isNotBlank() }

    return givenPoints.map { it.fold(foldInstruction) }.toSet().count()
  }

  fun part2(input: List<String>): Int {
    val givenPoints = input.takeWhile { it.isNotBlank() }.map { Point.of(it) }
    val foldInstructions = input.dropWhile { it.isNotBlank() }
      .map { it.removePrefix("fold along ") }
      .filter { it.isNotBlank() }

    var currentPoints = givenPoints.toSet()
    for (foldInstruction in foldInstructions) {
      currentPoints = currentPoints.map { it.fold(foldInstruction) }.toSet()
    }
    draw(currentPoints)
    return currentPoints.size
  }

  // test if implementation meets criteria from the description, like:
  val testInput = readInput("Day13_test")
  check(part1(testInput) == 17)
  check(part2(testInput) == 16)

  val input = readInput("Day13")
  println(part1(input))
  println(part2(input))
}

fun draw(points: Set<Point>) {
  val height = points.map { it.y }.maxOrNull()?.plus(1) ?: 0
  val width = points.map { it.x }.maxOrNull()?.plus(1) ?: 0

  val grid = Array(height) { Array(width) { ' ' } }
  points.forEach {
    grid[it.y][it.x] = '#'
  }
  grid.forEach { row ->
    row.forEach { print(it) }
    println()
  }
}

private fun Point.fold(foldInstruction: String): Point {
  val direction = foldInstruction.first()
  val foldIndex = foldInstruction.substringAfter('=').toInt()

  return when {
    direction == 'y' && this.y > foldIndex -> Point(x = this.x, y = foldIndex - (this.y - foldIndex))
    direction == 'x' && this.x > foldIndex -> Point(x = foldIndex - (this.x - foldIndex), y = this.y)
    else -> this
  }
}
