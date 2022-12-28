package week2

import shared.Puzzle
import java.lang.Integer.max
import java.lang.Integer.min

typealias Point = Pair<Int, Int>
typealias Cave = Array<CharArray>
class RegolithReservoir : Puzzle(14) {
    companion object {
        const val SAND = 'o'
        const val ROCK = '#'
        const val AIR = '.'
        val initialSandDropPos = 500 to 0
    }

    override fun solveFirstPart(): Any {
        val parsedLines = puzzleInput.map { parseLines(it) }

        val deepest = getDeepestLevels(parsedLines)
        val largest = getLargestNumber(parsedLines)
        val cave = Array(largest + 1) { _ -> CharArray(deepest + 2) { AIR } }

        drawInitialCaveMap(parsedLines, cave)

        var rounds = 0
        val predicate = { p: Point, _: Boolean -> p.second < deepest }
        val stopCondition = { p: Point -> p.second >= deepest }
        while (!simulateSanddrop(cave, initialSandDropPos, deepest, predicate, stopCondition)) {
            rounds++
        }

        return rounds
    }

    override fun solveSecondPart(): Any {
        val parsedLines = puzzleInput.map { parseLines(it) }

        val deepest = getDeepestLevels(parsedLines)
        val largest = getLargestNumber(parsedLines)

        val newFloorLine: List<Point> = listOf(0 to deepest + 2, largest * 2 - 1 to deepest + 2)
        val parsedLinesWithFloor: List<List<Point>> = parsedLines.plusElement(newFloorLine)
        val cave = Array(largest * 2) { _ -> CharArray(deepest + 3) { AIR } }

        drawInitialCaveMap(parsedLinesWithFloor, cave)

        var rounds = 1
        val predicate = { p: Point, canMove: Boolean -> p.second >= 0 && canMove }
        val stopCondition = { p: Point -> p.second == 0 }
        while (!simulateSanddrop(cave, initialSandDropPos, deepest+2, predicate, stopCondition)) {
            rounds++
        }

        return rounds
    }

    private fun parseLines(paths: String): List<Point> {
        return paths.split(" -> ")
            .map { it.split(",") }
            .map { it[0].toInt() to it[1].toInt() }
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
        } else {
            drawHorizontalLine(from, to, cave)
        }
    }

    fun drawVerticalLine(from: Point, to: Point, cave: Cave) {
        val fromY = min(from.second, to.second)
        val toY = max(from.second, to.second)

        for (i in fromY..toY) {
            cave[from.first][i] = ROCK
        }
    }

    fun drawHorizontalLine(from: Point, to: Point, cave: Cave) {
        val fromX = min(from.first, to.first)
        val toX = max(from.first, to.first)

        for (i in fromX..toX) {
            cave[i][from.second] = ROCK
        }
    }

    fun simulateSanddrop(
        cave: Cave,
        currPos: Point,
        deepestLevel: Int,
        predicate: (Point, Boolean) -> Boolean,
        stopCondition: (Point) -> Boolean
    ): Boolean { // return true iff goal reached.
        // while can move and has not passed the deepest level, keep moving
        var canMove = canStillMove(cave, currPos)
        var newPos = currPos
        while (canMove && predicate(newPos, canMove)) {
            if (canGoDown(cave, newPos)) {
                // go down and update pos
                newPos = newPos.copy(second = newPos.second.inc())
            } else if (canGoLeft(cave, newPos)) {
                // go left and update pos
                newPos = newPos.copy(first = newPos.first.dec(), second = newPos.second.inc())
            } else if (canGoRight(cave, newPos)) {
                // go right and update pos
                newPos = newPos.copy(first = newPos.first.inc(), second = newPos.second.inc())
            }

            // do follow up things
            canMove = canStillMove(cave, newPos)
        }

        // Update the cave with the new sand drop iff the sand is above deepest floor.
        if (newPos.second < deepestLevel) {
            cave[newPos.first][newPos.second] = SAND
        }

        return stopCondition(newPos)
    }

    fun canStillMove(cave: Cave, currPos: Point): Boolean {
        return canGoDown(cave, currPos) || canGoLeft(cave, currPos) || canGoRight(cave, currPos)
    }

    fun canGoDown(cave: Cave, currPos: Point): Boolean {
        return !isBlocking(cave, currPos.copy(second = currPos.second.inc()))
    }

    fun canGoLeft(cave: Cave, currPos: Point): Boolean {
        return !isBlocking(cave, currPos.copy(first = currPos.first.dec(), second = currPos.second.inc()))
    }

    fun canGoRight(cave: Cave, currPos: Point): Boolean {
        return !isBlocking(cave, currPos.copy(first = currPos.first.inc(), second = currPos.second.inc()))
    }

    fun isBlocking(cave: Cave, pos: Point): Boolean {
        val charAtPos = cave[pos.first][pos.second]

        return charAtPos == SAND || charAtPos == ROCK
    }
}
