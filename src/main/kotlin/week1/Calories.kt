package week1

import shared.Day
import shared.Puzzle

private const val SEPARATOR = ","

@Day(1)
class Calories : Puzzle(1) {

    override fun solveFirstPart() = puzzleInput
        .joinToString(SEPARATOR)
        .split("$SEPARATOR$SEPARATOR")
        .maxOfOrNull { sumAllNumbers(it) }!!

    override fun solveSecondPart() = puzzleInput
        .joinToString(SEPARATOR)
        .split("$SEPARATOR$SEPARATOR")
        .map { sumAllNumbers(it) }
        .sortedDescending()
        .take(3)
        .sum()

    private fun sumAllNumbers(calories: String) = calories.split(SEPARATOR).sumOf { it.toInt() }
}
