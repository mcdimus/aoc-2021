fun main() {

  fun part1(input: List<String>) = input.map { NavigationSystemLine(it) }
    .filter { it.isCorrupted() }
    .sumOf { it.getCorruptionScore() }

  fun part2(input: List<String>) = input.map { NavigationSystemLine(it) }
    .filterNot { it.isCorrupted() }
    .map { it.getAutocorrectionScore() }
    .sorted()
    .let { it[it.size / 2] }

  // test if implementation meets criteria from the description, like:
  val testInput = readInput("Day10_test")
  check(part1(testInput) == 26397)
  check(part2(testInput) == 288957L)

  val input = readInput("Day10")
  println(part1(input))
  println(part2(input))
}

class NavigationSystemLine(val value: String) {

  companion object {

    @JvmStatic
    private val CORRUPTION_SCORE_MAPPING = mapOf(
      ')' to 3,
      ']' to 57,
      '}' to 1197,
      '>' to 25137,
    )

    @JvmStatic
    private val AUTOCORRECTION_SCORE_MAPPING = mapOf(
      ')' to 1,
      ']' to 2,
      '}' to 3,
      '>' to 4,
    )

    @JvmStatic
    private val MATCHING_CHARS = mapOf(
      ')' to '(',
      ']' to '[',
      '}' to '{',
      '>' to '<',
      '(' to ')',
      '[' to ']',
      '{' to '}',
      '<' to '>',
    )

    @JvmStatic
    private val OPENING_CHARS = setOf('(', '[', '{', '<')

  }

  private var firstCorruptedChar: Char? = null

  fun isCorrupted(): Boolean {
    val stack = ArrayDeque<Char>()
    for (char in value.toCharArray()) {
      when {
        char in OPENING_CHARS -> stack.add(char)
        MATCHING_CHARS[char] == stack.last() -> stack.removeLast()
        else -> {
          firstCorruptedChar = char
          break
        }
      }
    }
    return firstCorruptedChar != null
  }

  fun getCorruptionScore() = firstCorruptedChar?.let { CORRUPTION_SCORE_MAPPING[it] } ?: 0

  fun getAutocorrectionScore(): Long {
    val stack = ArrayDeque<Char>()
    for (char in value.toCharArray()) {
      when {
        char in OPENING_CHARS -> stack.add(char)
        MATCHING_CHARS[char] == stack.last() -> stack.removeLast()
        else -> throw IllegalStateException("should not get here if line is not corrupted")
      }
    }

    return stack.reversed()
      .map { MATCHING_CHARS[it]!! }
      .map { AUTOCORRECTION_SCORE_MAPPING[it]!! }
      .fold(initial = 0L) { acc, i -> acc * 5 + i }
  }

}
