import Direction.DOWN
import Direction.FORWARD
import Direction.UP

fun main() {

  fun part1(input: List<String>) = input.map(Move::of)
    .fold(initial = SimpleLocation.ZERO) { location, move -> location + move }
    .checksum()

  fun part2(input: List<String>): Int = input.map(Move::of)
    .fold(initial = AimingLocation.ZERO) { location, move -> location + move }
    .checksum()

  // test if implementation meets criteria from the description, like:
  val testInput = readInput("Day02_test")
  check(part1(testInput) == 150)
  check(part2(testInput) == 900)

  val input = readInput("Day02")
  println(part1(input))
  println(part2(input))
}

interface Location {

  val x: Int
  val y: Int

  fun checksum() = x * y
  operator fun plus(move: Move): Location

}

data class SimpleLocation(override val x: Int, override val y: Int) : Location {

  override fun plus(move: Move) = SimpleLocation(
    x = if (move.direction == FORWARD) x + move.value else x,
    y = when (move.direction) {
      UP -> y - move.value
      DOWN -> y + move.value
      else -> y
    }
  )

  companion object {
    val ZERO = SimpleLocation(x = 0, y = 0)
  }

}

data class AimingLocation(val location: SimpleLocation, val aim: Int) : Location by location {

  constructor(x: Int, y: Int, aim: Int) : this(SimpleLocation(x, y), aim)

  override fun plus(move: Move) = AimingLocation(
    x = if (move.direction == FORWARD) x + move.value else x,
    y = if (move.direction == FORWARD) y + move.value * aim else y,
    aim = when (move.direction) {
      UP -> aim - move.value
      DOWN -> aim + move.value
      else -> aim
    }
  )

  companion object {
    val ZERO = AimingLocation(x = 0, y = 0, aim = 0)
  }

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
