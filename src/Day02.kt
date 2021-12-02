import Direction.DOWN
import Direction.FORWARD
import Direction.UP

fun main() {

  fun part1(input: List<String>): Int {
    val moves = input.map { Move.of(it) }

    val finalLocation = moves.fold(initial = Location(x = 0, y = 0)) { location, move -> location.apply(move) }

    return finalLocation.x * finalLocation.y
  }

  fun part2(input: List<String>): Int {
    val moves = input.map { Move.of(it) }

    val finalLocation = moves.fold(initial = Location(x = 0, y = 0)) { location, move -> location.applyWithAim(move) }

    return finalLocation.x * finalLocation.y
  }

  // test if implementation meets criteria from the description, like:
  val testInput = readInput("Day02_test")
  check(part1(testInput) == 150)
  check(part2(testInput) == 900)

  val input = readInput("Day02")
  println(part1(input))
  println(part2(input))
}

data class Location(val x: Int, val y: Int, val aim: Int = 0) {

  fun apply(move: Move) = Location(
    x = if (move.direction == FORWARD) x + move.value else x,
    y = when (move.direction) {
      UP -> y - move.value
      DOWN -> y + move.value
      else -> y
    }
  )

  fun applyWithAim(move: Move) = Location(
    x = if (move.direction == FORWARD) x + move.value else x,
    y = if (move.direction == FORWARD) y + move.value * aim else y,
    aim = when (move.direction) {
      UP -> aim - move.value
      DOWN -> aim + move.value
      else -> aim
    }
  )

}

enum class Direction {
  FORWARD, DOWN, UP
}

data class Move(val direction: Direction, val value: Int) {

  companion object {

    fun of(value: String) = Move(
      direction = Direction.valueOf(value.substringBefore(' ').uppercase()),
      value = value.substringAfter(' ').toInt()
    )
  }

}
