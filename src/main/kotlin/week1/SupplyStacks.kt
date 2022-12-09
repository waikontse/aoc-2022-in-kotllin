package week1

import shared.Puzzle

class SupplyStacks : Puzzle(5) {
    override fun solveFirstPart(): Any {
        val sections = splitSections(puzzleInput)

        val startStacks = sections[0].map { parseStack(it) }
            .map { it.filter { pair -> pair.second != ' '} }
            .let { createStacks(it) }

        sections[2].map { parseMove(it) }
            .forEach { moveStacks(it[0], it[1], it[2], { chars -> chars.reversed() } , startStacks) }

        return startStacks.filter { it.isNotEmpty() }
            .map { it.last() }
            .joinToString("")
    }

    override fun solveSecondPart(): Any {
        val sections = splitSections(puzzleInput)

        val startStacks = sections[0].map { parseStack(it) }
            .map { it.filter { pair -> pair.second != ' '} }
            .let { createStacks(it) }


        sections[2].map { parseMove(it) }
            .forEach { moveStacks(it[0], it[1], it[2], { chars -> chars } , startStacks) }

        return startStacks.filter { it.isNotEmpty() }
            .map { it.last() }
            .joinToString("")
    }

    private fun splitSections(puzzleInput: List<String>): List<List<String>> {
        val indexSplit = puzzleInput.indexOfFirst { it.isBlank() }

        return listOf(
            puzzleInput.subList(0, indexSplit - 1),
            puzzleInput.subList(indexSplit - 1, indexSplit),
            puzzleInput.subList(indexSplit + 1, puzzleInput.size)
        )
    }

    private fun parseStack(row: String): List<Pair<Int, Char>> {
        return parseStackIndividual(emptyList(), row, 1)
    }

    private tailrec fun parseStackIndividual(
        acc: List<Pair<Int, Char>>,
        row: String ,
        currentPos: Int
    ): List<Pair<Int, Char>> {
        if (row.isEmpty()) {
            return acc
        }

        return parseStackIndividual(
            acc + (currentPos to row[1]),
            row.drop(4),
            currentPos.inc()
        )
    }

    private fun createStacks(stackLocations: List<List<Pair<Int, Char>>>): MutableList<MutableList<Char>> {
        val stacks = MutableList(10) { mutableListOf<Char>() }
        stackLocations
            .map { it.forEach { locations -> stacks[locations.first].add(0, locations.second) } }

        return stacks
    }

    private fun parseMove(move: String): List<Int> {
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
        action: (f: List<Char>) -> List<Char>,
        stacks: MutableList<MutableList<Char>>
    ) {
        val newMovableStack = action(stacks[from].takeLast(count))
        val newFromStack = stacks[from].dropLast(count).toMutableList()
        stacks[from] = newFromStack

        stacks[to].addAll(newMovableStack)
    }
}
