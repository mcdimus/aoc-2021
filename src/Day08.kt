fun main() {

  fun part1(input: List<String>): Int {
    return input.map { it.split("|")[1] }.map { it.trim() }
      .flatMap { it.split(" ") }
      .count { it.length in setOf(2, 3, 4, 7) }
  }

  fun part2(input: List<String>): Int {
    return input.map { Display(it) }.map { it.getOutputAsInt() }.sum()
  }

  // test if implementation meets criteria from the description, like:
  val testInput = readInput("Day08_test")
  check(part1(testInput) == 26)
  check(part2(testInput) == 61229)

  val input = readInput("Day08")
  println(part1(input))
  println(part2(input))
}

class Display(input: String) {

  val patterns = input.split(" | ")[0].split(" ")
  val output = input.split(" | ")[1].split(" ")

  private val encoding = mutableMapOf<Char, Int>()
  private val mapping = mapOf(
    "012456" to 0,
    "25" to 1,
    "02346" to 2,
    "02356" to 3,
    "1235" to 4,
    "01356" to 5,
    "013456" to 6,
    "025" to 7,
    "0123456" to 8,
    "012356" to 9
  )

  fun getOutputAsInt(): Int {
    buildEncoding()

    return output.map { input -> input.toList().map { encoding[it]!! }.sorted() }
      .map { it.joinToString("") }
      .map { mapping[it] }
      .joinToString("")
      .toInt()
  }

  private fun buildEncoding() {
    val oneChars = patterns.single { it.length == 2 }.toSet()
    val fourChars = patterns.single { it.length == 4 }.toSet()
    val sevenChars = patterns.single { it.length == 3 }.toSet()
    val eightChars = patterns.single { it.length == 7 }.toSet()

    // segment 0
    sevenChars.single { it !in oneChars }.let { encoding[it] = 0 }

    // segment 6
    patterns.asSequence().filter { it.length == 6 }.map { it.toSet() }
      .map { it.filter { c -> c !in fourChars && c !in encoding } }
      .filter { it.size == 1 }.single().let { encoding[it[0]] = 6 }

    // segment 3
    patterns.asSequence().filter { it.length == 5 }.map { it.toSet() }
      .map { it.filter { c -> c !in oneChars && c !in encoding } }
      .filter { it.size == 1 }.single().let { encoding[it[0]] = 3 }

    // segment 1
    fourChars.single { it !in oneChars && it !in encoding }.let { encoding[it] = 1 }

    // segment 4
    patterns.asSequence().filter { it.length == 6 }.map { it.toSet() }
      .filter { it.containsAll(oneChars) }
      .flatten().groupBy { it }.filter { it.value.size == 1 }.flatMap { it.value }.single { it !in encoding }
      .let { encoding[it] = 4 }

    patterns.asSequence().filter { it.length == 5 }.map { it.toSet() }
      .filter { it.contains(encoding.filter { it.value == 1 }.map { it.key }.single()) }
      .flatten().filter { it !in encoding }.single().let { encoding[it] = 5 }

    eightChars.single { it !in encoding }.let { encoding[it] = 2 }
  }

}
