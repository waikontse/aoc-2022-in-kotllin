package week1

import shared.Day
import shared.Puzzle
import shared.ReadUtils.Companion.readPuzzleInput

@Day(2)
class RockScissorPaper : Puzzle {
    private val exampleInput = readPuzzleInput("day2_example.txt")
    private val puzzleInput = readPuzzleInput("day2.txt")
    private val scoreMapperPart1: Map<String, Int> = mapOf(
        "A X" to 4,
        "A Y" to 8,
        "A Z" to 3,

        "B X" to 1,
        "B Y" to 5,
        "B Z" to 9,

        "C X" to 7,
        "C Y" to 2,
        "C Z" to 6
    )

    private val scoreMapperPart2: Map<String, Int> = mapOf(
        "A X" to 3,
        "A Y" to 4,
        "A Z" to 8,

        "B X" to 1,
        "B Y" to 5,
        "B Z" to 9,

        "C X" to 2,
        "C Y" to 6,
        "C Z" to 7
    )

    override fun solveFirstPart() = puzzleInput.sumOf { mapGameToPoints(it, scoreMapperPart1)!! }

    override fun solveSecondPart() = puzzleInput.sumOf { mapGameToPoints(it, scoreMapperPart2)!! }

    private fun mapGameToPoints(
        game: String,
        scoreMapper: Map<String, Int>
    ) = scoreMapper[game]
}
