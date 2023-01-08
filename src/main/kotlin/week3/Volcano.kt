package week3

import shared.AdjacencyMatrix
import shared.Algorithms.Companion.floydWarshall
import shared.Puzzle
import shared.ReadUtils.Companion.debug

class Volcano : Puzzle(16) {
    data class VolcanoConfig(
        val adjacencyMatrix: AdjacencyMatrix,
        val translationMap: Map<String, Int>,
        val caveToFlowMap: Map<String, Int>,
        val startingCave: String,
        val runningTime: Int
    )

    override fun solveFirstPart(): Any {
        val cleanedLines = puzzleInput
            .map { it.replace("Valve ", "") }
            .map { StringBuilder(it) }
            .map { it.insert(2, ',') }
            .map { it.toString() }
            .sorted()

        val translationMap = generateTranslationMap(cleanedLines)
        val caveInfos = generateValveFlowList(cleanedLines).filter { it.value > 0 }
        val adjacencyMatrix = floydWarshall(generateAdjacencyMatrix(cleanedLines, translationMap))
        val runningTime = 30
        val volcanoConfig = VolcanoConfig(adjacencyMatrix, translationMap, caveInfos, "AA", runningTime)
        val maxDepth = 6
        val avgDist = caveInfos.keys
            .map { adjacencyMatrix[translationMap["AA"]!!][translationMap[it]!!] }
            .average().toInt()

        val bestRoute = calculateBestRouteForCave(volcanoConfig, maxDepth, avgDist*2)

        println("Best route: $bestRoute")

        return bestRoute.second
    }

    override fun solveSecondPart(): Any {
        val cleanedLines = puzzleInput
            .map { it.replace("Valve ", "") }
            .map { StringBuilder(it) }
            .map { it.insert(2, ',') }
            .map { it.toString() }
            .sorted()

        val translationMap = generateTranslationMap(cleanedLines)
        val caveInfos = generateValveFlowList(cleanedLines).filter { it.value > 0}
        val maxRunningMinutes = 26
        val maxDepth = 6
        val adjacencyMatrix = floydWarshall(generateAdjacencyMatrix(cleanedLines, translationMap))
        val volcanoConfig = VolcanoConfig(adjacencyMatrix, translationMap, caveInfos, "AA", maxRunningMinutes)
        val avgDist = caveInfos.keys
            .map { adjacencyMatrix[translationMap["AA"]!!][translationMap[it]!!] }
            .average().toInt()

        val comboSet = generateSet(caveInfos.map { it.key }.toSet(), maxDepth, volcanoConfig, avgDist*2)
        val prunedComboSet = getHighestUniqueRoutes(volcanoConfig, comboSet)

        // Calculate the best complementing route for each generated starting route
        val bestRoutes = prunedComboSet
            .map {
                it to calculatePointsForRoute(volcanoConfig, it)
            }
            .map {
                val newCaveWithFlows = caveInfos - it.first.toSet()
                val newVolcanoConfig = volcanoConfig.copy(caveToFlowMap = newCaveWithFlows)
                val bestRoute = calculateBestRouteForCave(newVolcanoConfig, maxDepth-1, avgDist*2)

                it to bestRoute
        }

        val bestestRoute = bestRoutes.maxBy { it.first.second + it.second.second }
        println("Bestest route: $bestestRoute: ${bestestRoute.first.second + bestestRoute.second.second}")

        return bestestRoute.first.second + bestestRoute.second.second
    }

    private fun getHighestUniqueRoutes(config: VolcanoConfig, routes: List<List<String>>): List<List<String>> {
        // Prune the sets of routes based on
        // 1) The route scored more than 0 points
        // 2) The routes with the same set of caves, only select the highest scoring route.
        val groupedRoutes = routes.map { it.toSet() to it }
            .map { it to calculatePointsForRoute(config, it.second) }
            .filter { it.second > 0 }
            .groupBy { it.first.first }

        return groupedRoutes
            .map { it.value.maxBy { pair -> pair.second } }
            .map { it.first.second }
    }


    private fun calculateBestRouteForCave(
        config: VolcanoConfig,
        maxSizeToGenerate: Int,
        avgDistance: Int,
    ): Pair<List<String>, Int> {
        val comboSet = generateSet(config.caveToFlowMap.keys.toSet(), maxSizeToGenerate, config, avgDistance)
        if (comboSet.isEmpty()) {
            return listOf("") to 0
        }

        val bestRoute = comboSet
            .map { it to calculatePointsForRoute(config, it) }
            .maxByOrNull { it.second }!!

        return bestRoute
    }

    private fun generateTranslationMap(lines: List<String>): Map<String, Int> {
        val translationMap = mutableMapOf<String, Int>()
        var currentCount = 0

        for (line in lines) {
            translationMap[line.substring(0..1)] = currentCount++
        }

        return translationMap
    }

    private fun generateValveFlowList(caves: List<String>): Map<String, Int> {
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

    private fun generateSet(initialSet: Set<String>,
                            maxDepth: Int,
                            config: VolcanoConfig,
                            avgDistance: Int
    ) = generateSet(initialSet, 0, maxDepth, mutableListOf(), listOf(), config, avgDistance)


    private fun generateSet(
        currentSet: Set<String>,
        currentDepth: Int,
        maxDepth: Int,
        acc: MutableList<List<String>>,
        currentCombo: List<String>,
        config: VolcanoConfig,
        avgDistance: Int,
    ): List<List<String>> {
        if (currentDepth == maxDepth) {
            acc.add(currentCombo)

            return acc
        }

        for (str in currentSet) {
            // Skip if distance from <-> to cave is too big.
            val currStartingCave = if (currentCombo.isEmpty()) "AA" else currentCombo.last()
            val distanceToCave = config.adjacencyMatrix[config.translationMap[currStartingCave]!!][config.translationMap[str]!!]
            if (distanceToCave > avgDistance) {
                continue
            }

            generateSet(currentSet.minus(str), currentDepth.inc(), maxDepth, acc, currentCombo.plus(str), config, avgDistance)
        }

        return acc
    }

    private fun calculatePointsForRoute(config: VolcanoConfig, route: List<String>
    ): Int {
        var currentCave = config.startingCave
        var accumulatedRoutePoints = 0
        var currentMinute = 0

        for (toCave in route) {
            // find route cost from current cave to target cave
            val from = config.translationMap[currentCave]!!
            val to = config.translationMap[toCave]!!
            val dist = config.adjacencyMatrix[from][to]
            currentMinute += dist

            // increment currentMinute to activate
            currentMinute += 1

            // calculate the points by multiplying remaining minutes with flow
            val flowRate = config.caveToFlowMap[toCave]!!
            accumulatedRoutePoints += (config.runningTime - currentMinute) * flowRate

            // update current cave
            currentCave = toCave
        }

        // safety check
        if (currentMinute > config.runningTime) {
            accumulatedRoutePoints = 0
        }

        return accumulatedRoutePoints
    }
}