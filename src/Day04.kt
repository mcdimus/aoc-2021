fun main() {

  fun part1(input: List<String>): Int {
    val bingo = Bingo(input)
    val firstWinningBoard = bingo.findFirstWinningBoard()

    return bingo.getLastDrawnNumber() * (firstWinningBoard?.getScore() ?: 0)
  }

  fun part2(input: List<String>): Int {
    val bingo = Bingo(input)
    val lastWinningBoard = bingo.findLastWinningBoard()

    return bingo.getLastDrawnNumber() * (lastWinningBoard?.getScore() ?: 0)
  }

  // test if implementation meets criteria from the description, like:
  val testInput = readInput("Day04_test")
  check(part1(testInput) == 4512)
  check(part2(testInput) == 1924)

  val input = readInput("Day04")
  println(part1(input))
  println(part2(input))
}

class Bingo(input: List<String>) {

  private val numbers = input.first().split(",").map { it.toInt() }
  private var lastDrawnNumberPointer = -1

  val boards = input.asSequence()
    .filter { it.isNotBlank() }
    .drop(1)
    .windowed(size = 5, step = 5)
    .map { BingoBoard(it) }
    .toList()

  fun drawNumber() = numbers[++lastDrawnNumberPointer]

  fun getLastDrawnNumber() = numbers[lastDrawnNumberPointer]

  fun findFirstWinningBoard(): BingoBoard? {
    var firstWinningBoard: BingoBoard? = null
    while (lastDrawnNumberPointer < numbers.size && firstWinningBoard == null) {
      val drawnNumber = drawNumber()
      boards.forEach { it.markNumber(drawnNumber) }
      firstWinningBoard = boards.find { it.hasWon() }
    }
    return firstWinningBoard
  }

  fun findLastWinningBoard(): BingoBoard? {
    val remainingTickets = boards.toMutableList()
    while (lastDrawnNumberPointer < numbers.size && remainingTickets.isNotEmpty()) {
      val drawnNumber = drawNumber()
      remainingTickets.forEach { it.markNumber(drawnNumber) }
      if (remainingTickets.size == 1 && remainingTickets.single().hasWon()) break
      remainingTickets.removeAll { it.hasWon() }
    }
    return remainingTickets.singleOrNull()
  }

}

class BingoBoard(input: List<String>) {

  companion object {

    private const val SIZE = 5
  }

  val numbers: List<BingoNumber> = input.flatMap { line ->
    line.split(" ").filter { it.isNotBlank() }.map { it.toInt() }.map { BingoNumber(it) }
  }

  fun markNumber(drawnNumber: Int) {
    numbers.singleOrNull { it.value == drawnNumber }?.mark()
  }

  fun hasWon(): Boolean {
    val wonOnRow = numbers.getRows(SIZE).any { it.all(BingoNumber::isMarked) }
    val wonOnColumn = numbers.getColumns(SIZE).any { it.all(BingoNumber::isMarked) }
    return wonOnRow || wonOnColumn
  }

  fun getScore() = numbers.filterNot { it.isMarked() }.sumOf { it.value }

  override fun toString() = numbers.getRows(SIZE).joinToString("\n") { row ->
    row.joinToString(" ") { number ->
      val value = number.value.toString().padStart(2)
      if (number.isMarked()) "($value)" else " $value "
    }
  }

}

class BingoNumber(val value: Int) {

  private var marked: Boolean = false

  fun isMarked() = marked

  fun mark() {
    marked = true
  }

}
