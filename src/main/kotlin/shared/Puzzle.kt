package shared

import shared.ReadUtils.Companion.readPuzzleInput

abstract class Puzzle(val day: Int) {
    val exampleInput = readPuzzleInput("day${day}_example.txt")
    val puzzleInput = readPuzzleInput("day$day.txt")

    abstract fun solveFirstPart(): Any
    abstract fun solveSecondPart(): Any
}
