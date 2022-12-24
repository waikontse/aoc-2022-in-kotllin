package week2

import shared.Puzzle

class DistressSignal: Puzzle(13) {
    override fun solveFirstPart(): Any {
        exampleInput.filter { it.isNotBlank() }
            .chunked(2)
            .map { it.joinToString(separator = " * ") }
            .forEach { println(it) }

        return 0
    }

    override fun solveSecondPart(): Any {
        TODO("Not yet implemented")
    }
}