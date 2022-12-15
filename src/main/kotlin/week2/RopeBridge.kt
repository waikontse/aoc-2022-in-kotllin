package week2

import shared.Puzzle
import kotlin.math.abs

data class Positions(
    var hPosition: Pair<Int, Int> = 0 to 0,
    var tPosition: Pair<Int, Int> = 0 to 0,
    val tVisitedPositions: MutableSet<Pair<Int, Int>> = mutableSetOf(0 to 0)
)

class RopeBridge: Puzzle(9) {
    override fun solveFirstPart(): Any {
        val positions = Positions()
        puzzleInput.map { splitMoveCommand(it) }
            .forEach { moveH(it.first, it.second, positions) }

        //println(positions)

        return positions.tVisitedPositions.count()
    }

    override fun solveSecondPart(): Any {
        TODO("Not yet implemented")
    }

    private fun splitMoveCommand(command: String): Pair<String, Int> {
        val spllitedCommand =  command.split(" ")

        return spllitedCommand[0] to spllitedCommand[1].toInt()
    }

    private fun moveH(direction: String, steps: Int, positions: Positions) {
        if (steps == 0) {
            return
        }

        when (direction) {
            "R" -> positions.hPosition = positions.hPosition.copy(first = positions.hPosition.first.inc())
            "L" -> positions.hPosition = positions.hPosition.copy(first = positions.hPosition.first.dec())
            "U" -> positions.hPosition = positions.hPosition.copy(second = positions.hPosition.second.inc())
            "D" -> positions.hPosition = positions.hPosition.copy(second = positions.hPosition.second.dec())
        }

        if (!isTTouching(positions)) {
            println("is not touching")
            moveTCloser(positions)

            println(positions)
        }

        moveH(direction, steps.dec(), positions)
    }

    private fun isTTouching(positions: Positions): Boolean {
        val isTouchingHorizontal = abs(positions.hPosition.first - positions.tPosition.first)
        val isTouchingVertical = abs(positions.hPosition.second - positions.tPosition.second)


     return ((isTouchingHorizontal == 0) || (isTouchingHorizontal == 1)) &&
             ((isTouchingVertical == 0) ||  (isTouchingVertical == 1))
    }

    private fun moveTCloser(positions: Positions) {
        with (positions) {
            // move left
            if (hPosition.second == tPosition.second && hPosition.first < tPosition.first) {
                tPosition = tPosition.copy(first = tPosition.first.dec())
            }

            //move right
            else if (hPosition.second == tPosition.second && hPosition.first > tPosition.first) {
                tPosition = tPosition.copy(first = tPosition.first.inc())
            }

            // move up
            else if (hPosition.first == tPosition.first && hPosition.second > tPosition.second) {
                tPosition = tPosition.copy(second = tPosition.second.inc())
            }

            // move down
            else if (hPosition.first == tPosition.first && hPosition.second < tPosition.second) {
                tPosition = tPosition.copy(second = tPosition.second.dec())
            }

            // move upper-left
            else if (hPosition.first < tPosition.first && hPosition.second > tPosition.second) {
                tPosition = tPosition.copy(first = tPosition.first.dec(), second = tPosition.second.inc())
            }

            // move upper-right
            else if (hPosition.first > tPosition.first && hPosition.second > tPosition.second) {
                tPosition = tPosition.copy(first = tPosition.first.inc(), second = tPosition.second.inc())
            }

            // move lower-left
            else if (hPosition.first < tPosition.first && hPosition.second < tPosition.second) {
                tPosition = tPosition.copy(first = tPosition.first.dec(), second = tPosition.second.dec())
            }

            // move lower right
            else if (hPosition.first > tPosition.first && hPosition.second < tPosition.second) {
                tPosition = tPosition.copy(first = tPosition.first.inc(), second = tPosition.second.dec())
            }
        }

        positions.tVisitedPositions.add(positions.tPosition)
    }
}