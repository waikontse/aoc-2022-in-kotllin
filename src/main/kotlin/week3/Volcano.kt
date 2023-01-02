package week3

import shared.Puzzle
import shared.ReadUtils.Companion.debug
import java.lang.Math.min

typealias AdjacencyMatrix = Array<IntArray>

class Volcano : Puzzle(16) {
    override fun solveFirstPart(): Any {
        val cleanedLines = exampleInput
            .map { it.replace("Valve ", "") }
            .map { StringBuilder(it) }
            .map { it.insert(2, ',') }
            .map { it.toString() }
            .sorted()

        val translationMap = generateTranslationMap(cleanedLines)
        val caveInfos = generateValveFlowList(cleanedLines)
        val caveInfosWithFlows = caveInfos.filter { it.value > 0}

        println("Cave with flows count: ${caveInfosWithFlows.size}")

        val adjacencyMatrix = floydWarshall(generateAdjacencyMatrix(cleanedLines, translationMap))


        println("generating connections ${caveInfosWithFlows.size}")
        val comboSet = generateSet(caveInfosWithFlows.map { it.key }.toSet(), 4)
        println(comboSet.size)

        val bestRoute = calculateBestRouteForCave("AA", 30, adjacencyMatrix, translationMap, caveInfosWithFlows)

        println(bestRoute)

        return bestRoute.second
    }

    override fun solveSecondPart(): Any {
        val cleanedLines = exampleInput
            .map { it.replace("Valve ", "") }
            .map { StringBuilder(it) }
            .map { it.insert(2, ',') }
            .map { it.toString() }
            .sorted()

        val translationMap = generateTranslationMap(cleanedLines)
        val caveInfos = generateValveFlowList(cleanedLines)
        val caveInfosWithFlows = caveInfos.filter { it.value > 0}
        val maxRunningMinutes = 26

        println("Cave with flows count: ${caveInfosWithFlows.size}")

        val adjacencyMatrix = floydWarshall(generateAdjacencyMatrix(cleanedLines, translationMap))

        val bestRoute = calculateBestRouteForCave("AA", maxRunningMinutes, adjacencyMatrix, translationMap, caveInfosWithFlows)

        println(bestRoute)

        return bestRoute.second
    }


    fun generateTranslationMap(lines: List<String>): Map<String, Int> {
        val translationMap = mutableMapOf<String, Int>()
        var currentCount = 0

        for (line in lines) {
            translationMap[line.substring(0..1)] = currentCount++
        }

        return translationMap
    }

    fun generateValveFlowList(caves: List<String>): Map<String, Int> {
        val caveInfo = mutableMapOf<String, Int>()

        for (cave in caves) {
            val caveName = cave.substring(0..1)
            val flowRate = cave.split(";")[0]
                .split("=")[1].toInt()

            caveInfo.put(caveName, flowRate)
        }

        return caveInfo
    }

    private fun generateAdjacencyMatrix(
        caves: List<String>,
        caveToNumberMap: Map<String, Int>
    ): AdjacencyMatrix {
        val adjacencyMatrix = Array(caves.size) { IntArray(caves.size) { 999991 } }

        val edges = caves.map { it.replace(""" has flow rate=\d+; tunnels? leads? to valves? """.toRegex(), "") }
        for (edge in edges) {
            val connections = edge.split(",").map { it.trim() }
            val from = caveToNumberMap[connections.first()]!!

            for (connection in connections.drop(1)) {
                debug("Checking connection: $connection")
                val to = caveToNumberMap[connection]!!
                adjacencyMatrix[from][to] = 1
            }
        }

        return adjacencyMatrix

    }

    private fun floydWarshall(adjacencyMatrix: AdjacencyMatrix): AdjacencyMatrix {
        val adjacencyMatrixAllPairs = adjacencyMatrix.clone()
        val v = adjacencyMatrix.size

        for (k in 0 until v) {
            for (i in 0 until v) {
                for (j in 0 until v) {
                    adjacencyMatrixAllPairs[i][j] = Math.min(adjacencyMatrixAllPairs[i][j], adjacencyMatrixAllPairs[i][k] + adjacencyMatrixAllPairs[k][j])
                }
            }
        }

        return adjacencyMatrixAllPairs
    }

    private fun generateSet(initialSet: Set<String>, maxDepth: Int): List<List<String>> {
        return generateSet(initialSet, 0, maxDepth, mutableListOf(), listOf())
    }

    private fun generateSet(
        currentSet: Set<String>,
        currentDepth: Int,
        maxDepth: Int,
        acc: MutableList<List<String>>,
        currentCombo: List<String>
    ): List<List<String>> {
        if (currentDepth == maxDepth) {
            acc.add(currentCombo)

            return acc
        }

        for (str in currentSet) {
            generateSet(currentSet.minus(str), currentDepth.inc(), maxDepth, acc, currentCombo.plus(str))
        }

        return acc
    }

    private fun calculateBestRouteForCave(
        startingCave: String,
        maxMinutes: Int,
        adjacencyMatrix: AdjacencyMatrix,
        translationMap: Map<String, Int>,
        caveToFlowMap: Map<String, Int>
    ): Pair<List<String>, Int> {
        var unopenedValvesCount = 6//caveToFlowMap.size
        val maxSizeToGenerate = 6
        var currentBestRoute = listOf<String>()
        var currentLastCave = startingCave
        val unopenedValves = caveToFlowMap.map { it.key }.toMutableSet()

        while (unopenedValvesCount > 0) {
            println("UnopenedValves count: $unopenedValvesCount")
            println("UnopenedValves: $unopenedValves")

            // Generate the list of combinations with a limited size
            // find the max of the generated list

            // save the list
            val comboSet = generateSet(unopenedValves, maxSizeToGenerate.coerceAtMost(unopenedValvesCount))
            val bestRoute = comboSet.map {
                it to calculatePointsForRoute(currentLastCave, maxMinutes, adjacencyMatrix, translationMap, caveToFlowMap, it)
            }.sortedByDescending { it.second }
                .first()
            currentBestRoute = currentBestRoute + bestRoute.first

            // remember the last cave
            // update the list of active caves
            // generate the new list with new starting cave, range and active caves
            currentLastCave = bestRoute.first.last()
            unopenedValves.removeAll(bestRoute.first.toSet())
            unopenedValvesCount -= maxSizeToGenerate

        }

        return currentBestRoute to calculatePointsForRoute(startingCave, maxMinutes, adjacencyMatrix, translationMap, caveToFlowMap, currentBestRoute)
    }

    private fun calculatePointsForRoute(
        startingCave: String,
        maxMinutes: Int,
        adjacencyMatrix: AdjacencyMatrix,
        translationMap: Map<String, Int>,
        caveToFlowMap: Map<String, Int>,
        route: List<String>
    ): Int {
        var currentCave = startingCave
        var accumulatedRoutePoints = 0
        var currentMinute = 0

        for (toCave in route) {
            // find route cost from current cave to target cave
            val from = translationMap[currentCave]!!
            val to = translationMap[toCave]!!
            val dist = adjacencyMatrix[from][to]
            currentMinute += dist

            // increment currentMinute to activate
            currentMinute += 1

            // calculate the points by multiplying remaining minutes with flow
            val flowRate = caveToFlowMap[toCave]!!
            accumulatedRoutePoints += (maxMinutes - currentMinute) * flowRate

            // update current cave
            currentCave = toCave
        }

       // println("Total points for route: $route -> $accumulatedRoutePoints")

        // safety check
        if (currentMinute > maxMinutes) {
            // throw IllegalArgumentException("Route calculation passed 30 minutes. $currentMinute")
            accumulatedRoutePoints = 0
        }

        // println("Route calculation used $currentMinute minutes. score: $accumulatedRoutePoints")


        return accumulatedRoutePoints
    }
}