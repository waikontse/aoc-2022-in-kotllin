package week2

import shared.Algorithms.Companion.dijkstra
import shared.Algorithms.Edge
import shared.Algorithms.Graph
import shared.Algorithms.GraphSize
import shared.Node
import shared.Puzzle

class HillClimbing : Puzzle(12) {
    override fun solveFirstPart(): Any {
        val input = puzzleInput
        val cleanedInput = input.map { it.replace("S", "a") }
            .map { it.replace("E", "z") }

        val graph = parseMap(cleanedInput)

        val shortestPaths = dijkstra(findStartingPosition(input), graph)

        return shortestPaths.first[findEndingPosition(input)]
    }

    override fun solveSecondPart(): Any {
        val input = puzzleInput
        val cleanedInput = input.map { it.replace("S", "a") }
            .map { it.replace("E", "z") }

        val graph = parseMap(cleanedInput)

        return cleanedInput.joinToString("")
            .mapIndexed { index, c -> if (c == 'a') index else -1 }
            .filterNot { i -> i == -1 }
            .map { dijkstra(it, graph) }
            .map { it.first[findEndingPosition(input)] }
            .min()
    }

    private fun parseMap(map: List<String>): Graph {
        val width = map[0].length
        val height = map.size
        val size = GraphSize(width, height)
        val graph = Graph(size)

        return parseGraph2(map.joinToString(separator = ""), size, 0, graph)
    }

    private tailrec fun parseGraph2(
        map: String,
        size: GraphSize,
        currPos: Node,
        graph: Graph
    ): Graph {
        if (currPos >= size.width * size.height) {
            return graph
        }

        val edges = getConnectedEdges(map, size, currPos)
        graph.addEdges(currPos, edges)

        return parseGraph2(map, size, currPos.inc(), graph)
    }

    private fun getConnectedEdges(map: String, size: GraphSize, currPos: Node): List<Edge> {
        val edges = mutableListOf<Edge>()

        if (isConnectedTop(map, size, currPos)) {
            edges.add(Edge(currPos.minus(size.width), 1))
        }
        if (isConnectedBottom(map, size, currPos)) {
            edges.add(Edge(currPos.plus(size.width), 1))
        }
        if (isConnectedLeft(map, size, currPos)) {
            edges.add(Edge(currPos.dec(), 1))
        }
        if (isConnectedRight(map, size, currPos)) {
            edges.add(Edge(currPos.inc(), 1))
        }

        return edges
    }

    private fun isConnectedTop(map: String, size: GraphSize, pos: Int): Boolean {
        return isConnected(map, size, pos, pos.minus(size.width))
    }

    private fun isConnectedBottom(map: String, size: GraphSize, pos: Int): Boolean {
        return isConnected(map, size, pos, pos.plus(size.width))
    }

    private fun isConnectedLeft(map: String, size: GraphSize, pos: Int): Boolean {
        return isConnected(map, size, pos, pos.dec())
    }

    private fun isConnectedRight(map: String, size: GraphSize, pos: Int): Boolean {
        return isConnected(map, size, pos, pos.inc())
    }

    private fun isConnected(map: String, size: GraphSize, from: Int, to: Int): Boolean {
        if (to < 0) {
            return false
        } else if (to >= size.width * size.height) {
            return false
        }

        return (map[from].code - map[to].code) >= -1
    }

    private fun findStartingPosition(map: List<String>): Int {
        return findPositionOfChar(map.joinToString(separator = ""), 'S', 0)
    }

    private fun findEndingPosition(map: List<String>): Int {
        return findPositionOfChar(map.joinToString(separator = ""), 'E', 0)
    }

    private tailrec fun findPositionOfChar(map: String, target: Char, currPos: Int): Int {
        if (map[currPos] == target) {
            return currPos
        }

        return findPositionOfChar(map, target, currPos.inc())
    }
}
