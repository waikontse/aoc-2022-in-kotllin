package week1

import shared.Day
import shared.Puzzle
import shared.ReadUtils.Companion.readPuzzleInput
import java.lang.IllegalArgumentException

@Day(5)
class SupplyStacks : Puzzle {
    private val exampleInput = readPuzzleInput("day5_example.txt")
    private val puzzleInput = readPuzzleInput("day5.txt")


    override fun solveFirstPart(): Any {
        exampleInput
            .let { splitSections(it) }
            .forEach { println(it) }

        return 0
    }

    override fun solveSecondPart(): Any {
        TODO("Not yet implemented")
    }

    private fun splitSections(puzzleInput: List<String>): List<List<String>> {
        val indexSplit = puzzleInput.indexOfFirst { it.isBlank() }

        return listOf(
            puzzleInput.subList(0, indexSplit - 1),
            puzzleInput.subList(indexSplit-1, indexSplit),
            puzzleInput.subList(indexSplit+1, puzzleInput.size)
        )
    }

    private fun parseMove(move: String) : List<Int> {
        // move 1 from 2 to 1
        val (count, from, to) = """move (\d+) from (\d+) to (\d+)""".toRegex()
            .matchEntire(move)
            ?.destructured
            ?: throw IllegalArgumentException("Illegal move: $move")

        return listOf(count.toInt(), from.toInt(), to.toInt())
    }

    private fun moveStacks(
        count: Int,
        from: Int,
        to: Int,
        stacks: MutableList<MutableList<Int>>
    ) {
        val newMovableStack = stacks[from].takeLast(count).reversed()
        val newFromStack = stacks[from].dropLast(count).toMutableList()
        stacks[from] = newFromStack

        stacks[to].addAll(newMovableStack)
    }
}