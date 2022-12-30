package week3

import shared.Puzzle

typealias AdjacencyMatrix = Array<IntArray>

class Volcano : Puzzle(16) {
    override fun solveFirstPart(): Any {
        val cleanedLines = exampleInput.map { it.replace("Valve ", "") }
            .sorted()

        val translationMap = generateTranslationMap(cleanedLines)

        cleanedLines.forEach { println(it) }
        println(translationMap)

        return 0
    }

    override fun solveSecondPart(): Any {
        TODO("Not yet implemented")
    }


    fun generateTranslationMap(lines: List<String>): Map<String, Int> {
        val translationMap = mutableMapOf<String, Int>()
        var currentCount = 0

        for (line in lines) {
            translationMap.put(line.substring(0..1), currentCount++)
        }

        return translationMap
    }

    fun generateValveFlowList(caves: List<String>): List<Pair<String, Int>> {
        for (cave in caves) {
            val caveName = cave.substring(0..1)
            val flowRate = cave.split(";")
        }
    }



    fun generateAdjacencyMatrix(): AdjacencyMatrix {
        return Array(0) { IntArray(0) }
    }


}