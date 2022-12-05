package week1

import shared.Day
import shared.Puzzle
import shared.ReadUtils.Companion.readPuzzleInput
import java.lang.IllegalArgumentException

@Day(4)
class CampCleanup : Puzzle {
    private val exampleInput = readPuzzleInput("day4_example.txt")
    private val puzzleInput = readPuzzleInput("day4.txt")
    private val lineRegex = """(\d+)-(\d+),(\d+)-(\d+)""".toRegex()


    override fun solveFirstPart(): Any {
        return puzzleInput.map { parseLine(it) }
            .map { it.sortedBy { section -> section.size } }
            .count { (it[0] - it[1]).isEmpty()}
    }

    override fun solveSecondPart(): Any {
        return puzzleInput
            .map { parseLine(it) }
            .count { (it[0] - it[1]).size < it[0].size}
    }

    private fun parseLine(sections: String): List<Set<Int>> {
        val (leftX1, leftX2, rightX1, rightX2) = lineRegex.matchEntire(sections)
            ?.destructured
            ?: throw IllegalArgumentException("Illegal argument: $sections")

        return listOf(
            (leftX1.toInt() to leftX2.toInt()).toSections(),
            (rightX1.toInt() to rightX2.toInt()).toSections()
        )
    }

    private fun Pair<Int,Int>.toSections() = (this.first..this.second).toSet()
}