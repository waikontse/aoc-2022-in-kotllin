package week1

import shared.Day
import shared.Puzzle
import shared.ReadUtils.Companion.readPuzzleInput

private const val SEPARATOR = ","

@Day(1)
class Calories : Puzzle {
    private var lines: List<String> = readPuzzleInput("day1.txt")
        .joinToString(SEPARATOR)
        .split("$SEPARATOR$SEPARATOR")

    override fun solveFirstPart() = lines.maxOfOrNull { sumAllNumbers(it) }!!

    override fun solveSecondPart() = lines.map { sumAllNumbers(it) }
        .sortedDescending()
        .take(3)
        .sum()

    private fun sumAllNumbers(calories: String) = calories.split(SEPARATOR).sumOf { it.toInt() }
}
