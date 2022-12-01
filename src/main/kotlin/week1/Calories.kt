package week1

import shared.Day
import shared.ReadUtils.Companion.readPuzzleInput

@Day(1)
class Calories {
    private var lines: List<String> = readPuzzleInput("day1.txt")
        .joinToString(",")
        .split(",,")

    fun solveFirstPart() = lines.maxOfOrNull { sumAllNumbers(it) }

    fun solveSecondPart() = lines.map { sumAllNumbers(it) }
        .sortedDescending()
        .take(3)
        .sum()

    private fun sumAllNumbers(calories: String) = calories.split(",").sumOf { it.toInt() }
}