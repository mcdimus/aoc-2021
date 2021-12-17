import java.util.LinkedList

fun main() {

  fun part1(input: List<String>): Int {
    val polymer = input.first().mapTo(LinkedList<Char>()) { it }
    val rules = input.drop(2).associate { it.substringBefore(" -> ") to it.substringAfter(" -> ").first() }

    // straightforward solution
    for (round in 1..10) {
      val size = polymer.size - 1
      var insertions = 0
      for (i in 0 until size) {
        val a = polymer[i + insertions]
        val b = polymer[i + 1 + insertions]
        val new = rules["$a$b"]
        if (new != null) {
          polymer.add(i + 1 + insertions++, new)
        }
      }
    }
    val counts = polymer.groupingBy { it }.eachCount()
    val min = counts.values.minOrNull() ?: 0
    val max = counts.values.maxOrNull() ?: 0

    return max - min
  }

  fun part2(input: List<String>): Long {
    val polymer = input.first().mapTo(LinkedList<Char>()) { it }
    val rules = input.drop(2).associate { it.substringBefore(" -> ") to it.substringAfter(" -> ").first() }

    var pairCounts = polymer.windowed(2, transform = { it.joinToString(separator = "") })
      .groupingBy { it }
      .fold(0L) { acc, _ -> acc + 1 }

    val charCounts = polymer
      .groupingBy { it }
      .fold(0L) { acc, _ -> acc + 1 }
      .toMutableMap()

    for (round in 1..40) {
      val newPairCounts = mutableMapOf<String, Long>().also { it.putAll(pairCounts) }
      pairCounts.forEach { (pair, count) ->
        if (pair in rules) {
          val newChar = rules[pair]!!
          newPairCounts.compute("${pair[0]}$newChar") { _, v -> (v ?: 0) + count }
          newPairCounts.compute("$newChar${pair[1]}") { _, v -> (v ?: 0) + count }
          newPairCounts.computeIfPresent(pair) { _, v -> v - count }
          charCounts.compute(newChar) { _, v -> (v ?: 0) + count }
        } else newPairCounts[pair] = count
      }
      pairCounts = newPairCounts.filter { it.value > 0 }
    }
    val min = charCounts.values.minOrNull() ?: 0L
    val max = charCounts.values.maxOrNull() ?: 0L

    return max - min
  }

  // test if implementation meets criteria from the description, like:
  val testInput = readInput("Day14_test")
  check(part1(testInput) == 1588)
  check(part2(testInput) == 2188189693529)

  val input = readInput("Day14")
  println(part1(input))
  println(part2(input))
}
