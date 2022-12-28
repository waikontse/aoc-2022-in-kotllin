package week2

import shared.Puzzle
import java.lang.Integer.max
import java.lang.Integer.min

typealias Point = Pair<Int, Int>
typealias Cave = Array<CharArray>
class RegolithReservoir : Puzzle(14) {
    override fun solveFirstPart(): Any {
        val parsedLines = exampleInput.map { parseLines(it) }

        parsedLines.forEach { println(it) }

        val deepest = getDeepestLevels(parsedLines)
        val largest = getLargestNumber(parsedLines)
        val smallest = getSmallestNumber(parsedLines)

        println("$deepest $largest $smallest")


        val cave = Array(largest+1) { _ -> CharArray(deepest+1) { '.'} }

        //printCave(cave, smallest, largest,deepest)
        drawInitialCaveMap(parsedLines, cave)
        printCave(cave, smallest, largest, deepest)

        return 0
    }

    fun printCave(cave: Cave,smallest: Int, largest: Int, deepest: Int) {
        for (y in 0 .. deepest) {
            for (x in smallest .. largest) {
                print(cave[x][y])
            }
            println()
        }
    }

    override fun solveSecondPart(): Any {
        TODO("Not yet implemented")
    }

    private fun parseLines(paths: String): List<Point> {
        return paths.split(" -> ")
            .map { it.split(",") }
            .map { it[0].toInt() to it[1].toInt() }
    }

    private fun getSmallestNumber(allPaths: List<List<Point>>): Int {
        return allPaths.map { points -> points.minBy { it.first } }
            .minBy { it.first }
            .first
    }

    private fun getLargestNumber(allPaths: List<List<Point>>): Int {
        return allPaths.map { points -> points.maxBy { it.first } }
            .maxBy { it.first }
            .first
    }

    private fun getDeepestLevels(allPaths: List<List<Point>>): Int {
        return allPaths.map { points -> points.maxBy { it.second } }
            .maxBy { it.second }
            .second
    }

    fun drawInitialCaveMap(allPaths: List<List<Point>>, cave: Cave) {
        allPaths.map { drawPathOnCave(it, cave) }
    }

    fun drawPathOnCave(path: List<Point>, cave: Cave) {
        path.zipWithNext()
            .map { drawLineOnCave(it.first, it.second, cave) }
    }

    fun drawLineOnCave(from: Point, to: Point, cave: Cave) {
        if (from.first == to.first) {
            drawVerticalLine(from, to, cave)
        }
        else {
            drawHorizontalLine(from, to, cave)
        }
    }

    fun drawVerticalLine(from: Point, to: Point, cave: Cave) {
        val fromY = min(from.second, to.second)
        val toY = max(from.second, to.second)

        for (i in fromY .. toY) {
            cave[from.first][i] = '#'
        }
    }

    fun drawHorizontalLine(from: Point, to: Point, cave: Cave) {
        val fromX = min(from.first, to.first)
        val toX = max(from.first, to.first)

        for (i in fromX .. toX) {
            cave[i][from.second] = '#'
        }
    }

    fun simulateSanddrop(cave: Cave) {
        TODO("implement me")
    }

    fun canGoLeft(): Boolean {
        return false
    }

    fun canGoRight(): Boolean {
        return false
    }

    fun canGoDown(): Boolean {
        return false
    }
}