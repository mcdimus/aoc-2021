fun main() {

  fun part1(input: List<String>) = SubmarineDiagnosticReport(input).getPowerConsumption()

  fun part2(input: List<String>) = SubmarineDiagnosticReport(input).getLifeSupportRating()

  // test if implementation meets criteria from the description, like:
  val testInput = readInput("Day03_test")
  check(part1(testInput) == 198)
  check(part2(testInput) == 230)

  val input = readInput("Day03")
  println(part1(input))
  println(part2(input))
}

class SubmarineDiagnosticReport(val entries: List<String>) {

  val bitsPerEntry = entries.first().length
  val length = entries.size
  private val pivot = length / 2
  private val bitCounts = getBitCounts(entries)

  private fun getBitCounts(entries: List<String>): Array<Int> {
    val bitCounts = Array(bitsPerEntry) { 0 }
    entries.forEach { x ->
      x.forEachIndexed { index, c ->
        bitCounts[index] += c.digitToInt()
      }
    }
    return bitCounts
  }

  fun getPowerConsumption(): Int {
    val gamma = bitCounts.map { if (it > pivot) 1 else 0 }.toDecimalInt()
    val epsilon = bitCounts.map { if (it < pivot) 1 else 0 }.toDecimalInt()

    return gamma * epsilon
  }

  fun getLifeSupportRating(): Int {
    val oxygenGeneratorRating: Int = getRating { bitCount, entriesCount -> bitCount >= entriesCount - bitCount }
    val co2ScrubbingRating: Int = getRating { bitCount, entriesCount -> bitCount < entriesCount - bitCount }

    return oxygenGeneratorRating * co2ScrubbingRating
  }

  private fun getRating(predicate: (Int, Int) -> Boolean): Int {
    val entries = this.entries.toMutableList()
    var rating: Int? = null
    for (i in 0 until bitsPerEntry) {
      val x = getBitCounts(entries)[i].let { if (predicate(it, entries.size)) 1 else 0 }
      entries.removeAll { it[i].digitToInt() != x }
      if (entries.size == 1) {
        rating = entries.single().toInt(2)
        break
      }
    }
    return rating!!
  }

  private fun List<Int>.toDecimalInt() = joinToString(separator = "").toInt(2)

}
