package week3

import shared.AdjacencyMatrix
import shared.Algorithms.Companion.floydWarshall
import shared.Puzzle
import shared.ReadUtils.Companion.debug
import java.lang.Math.min

class Volcano : Puzzle(16) {
    override fun solveFirstPart(): Any {
        val cleanedLines = puzzleInput
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
        println(caveInfosWithFlows)
        println("Average flows: ${caveInfosWithFlows.values.average()}")
        caveInfosWithFlows.keys
            .forEach { println("Distance to cave $it: ${adjacencyMatrix[translationMap["AA"]!!][translationMap[it]!!]}") }
        val avgDist = caveInfosWithFlows.keys
            .map { adjacencyMatrix[translationMap["AA"]!!][translationMap[it]!!] }
            .average().toInt()
        println("Avg distance: $avgDist")
        val median = caveInfosWithFlows.keys
            .map { adjacencyMatrix[translationMap["AA"]!!][translationMap[it]!!] }
            .sorted()
        println("Avg distance: $median")
        val flow = caveInfosWithFlows.values.sorted()
        println("Avg flow: $flow")

        val ignoreForStartingCave = setOf("AM", "UI", "YK", "IH", "XN")

        val comboSet = generateSet(caveInfosWithFlows.map { it.key }.toSet(), 6, 1, adjacencyMatrix,
            translationMap, avgDist, ignoreForStartingCave)
        println(comboSet.size)

        val bestRoute = calculateBestRouteForCave("AA", 26, adjacencyMatrix, translationMap,
            caveInfosWithFlows, 6, avgDist, ignoreForStartingCave)

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
        val caveInfos = generateValveFlowList(cleanedLines)
        val caveInfosWithFlows = caveInfos.filter { it.value > 0}
        val maxRunningMinutes = 26

        println("Cave with flows count: ${caveInfosWithFlows.size}")

        val adjacencyMatrix = floydWarshall(generateAdjacencyMatrix(cleanedLines, translationMap))


//        val route = calculatePointsForRoute("AA", maxRunningMinutes, adjacencyMatrix, translationMap, caveInfosWithFlows,
//            listOf("JJ", "BB", "CC"))
//
//        println(route)
        val avgDist = caveInfosWithFlows.keys
            .map { adjacencyMatrix[translationMap["AA"]!!][translationMap[it]!!] }
            .average().toInt()
        println("Avg distance: $avgDist")
        val ignoreForStartingCave = setOf("CC", "EE")

        val bestRoutes = calculateBestRouteForCaveMulti("AA", maxRunningMinutes, adjacencyMatrix, translationMap,
        caveInfosWithFlows, avgDist, ignoreForStartingCave)

        println("Best routes: $bestRoutes")



        //return bestRoute.second

        return 0
    }

    private fun calculateBestRouteForCaveMulti(
        startingCave: String,
        maxMinutes: Int,
        adjacencyMatrix: AdjacencyMatrix,
        translationMap: Map<String, Int>,
        caveToFlowMap: Map<String, Int>,
        avgDistance: Int,
        ignoreForStartingCave: Set<String>
    ): Pair<List<String>, Int> {
        val maxSizeToGenerate = 8
        val unopenedValves = caveToFlowMap.map { it.key }.toSet()

        // Generate the list of combinations with a limited size
        // find the max of the generated list

        // save the list
        val comboSet = generateSet(unopenedValves, maxSizeToGenerate, 1, adjacencyMatrix, translationMap,
            avgDistance, ignoreForStartingCave)
        println("Size of set: ${comboSet.size}")
        val bestRoute = comboSet
            .map {
                it to
                        calculatePointsForRoute(
                            startingCave,
                            maxMinutes,
                            adjacencyMatrix,
                            translationMap,
                            caveToFlowMap,
                            it.subList(0, it.size/2)
                        ).plus(
                            calculatePointsForRoute(
                                startingCave,
                                maxMinutes,
                                adjacencyMatrix,
                                translationMap,
                                caveToFlowMap,
                                it.subList(it.size/2, it.size)
                            )
                        )
            }.maxByOrNull { it.second }!!

        // remember the last cave
        // update the list of active caves
        // generate the new list with new starting cave, range and active caves

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

    private fun calculateBestRouteForCave(
        startingCave: String,
        maxMinutes: Int,
        adjacencyMatrix: AdjacencyMatrix,
        translationMap: Map<String, Int>,
        caveToFlowMap: Map<String, Int>,
        maxSizeToGenerate: Int,
        avgDistance: Int,
        ignoreForStartingCave: Set<String>
    ): Pair<List<String>, Int> {
        var currentBestRoute = listOf<String>()

        // save the list
        val comboSet = generateSet(caveToFlowMap.keys.toSet(), maxSizeToGenerate, 1,adjacencyMatrix, translationMap,
            avgDistance, ignoreForStartingCave)
        val bestRoute = comboSet.map {
            it to calculatePointsForRoute(
                startingCave,
                maxMinutes,
                adjacencyMatrix,
                translationMap,
                caveToFlowMap,
                it
            )
        }.maxByOrNull { it.second }!!
        currentBestRoute = currentBestRoute + bestRoute.first

        return currentBestRoute to calculatePointsForRoute(startingCave, maxMinutes, adjacencyMatrix, translationMap, caveToFlowMap, currentBestRoute)
    }

    private fun generateSet(initialSet: Set<String>,
                            maxDepth: Int,
                            adjacencyMatrix: AdjacencyMatrix,
                            translationMap: Map<String, Int>,
                            avgDistance: Int,
                            ignoreForStartingCave: Set<String>
    ): List<List<String>> {
        val generatedSet = generateSet(initialSet, 0, maxDepth, mutableListOf(), listOf(), adjacencyMatrix,
            translationMap, avgDistance, ignoreForStartingCave)

        println("Generated set size ${generatedSet.size} for $initialSet")

        return generatedSet
    }

    private fun generateSet(
        currentSet: Set<String>,
        currentDepth: Int,
        maxDepth: Int,
        acc: MutableList<List<String>>,
        currentCombo: List<String>,
        adjacencyMatrix: AdjacencyMatrix,
        translationMap: Map<String, Int>,
        avgDistance: Int,
        ignoreForStartingCave: Set<String> // List of cave with flows not worth exploring when starting
    ): List<List<String>> {
        if (currentDepth == maxDepth) {
            acc.add(currentCombo)

            return acc
        }

        for (str in currentSet) {
            // Skip if distance is too great
            val distanceFromStartingCave = adjacencyMatrix[translationMap["AA"]!!][translationMap[str]!!]
            if ((distanceFromStartingCave > avgDistance) && currentDepth == 0) {
                continue
            }

            // skip if cave is not worth exploring
            if (currentDepth <= maxDepth/2 && ignoreForStartingCave.contains(str)) {
                continue
            }

            generateSet(currentSet.minus(str), currentDepth.inc(), maxDepth, acc, currentCombo.plus(str),
                adjacencyMatrix, translationMap, avgDistance, ignoreForStartingCave
            )
        }

        return acc
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

       //println("Total points for route: $route -> $accumulatedRoutePoints")

        // safety check
        if (currentMinute > maxMinutes) {
            // throw IllegalArgumentException("Route calculation passed 30 minutes. $currentMinute")
            accumulatedRoutePoints = 0
        }

        return accumulatedRoutePoints
    }
}