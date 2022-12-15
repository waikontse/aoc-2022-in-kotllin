package week2

import shared.Puzzle
import kotlin.math.abs

data class Positions(
    var ropePosition: MutableList<Pair<Int, Int>> = MutableList(10) { 0 to 0 },
    val tVisitedPositions: MutableSet<Pair<Int, Int>> = mutableSetOf(0 to 0)
)

class RopeBridge: Puzzle(9) {
    override fun solveFirstPart(): Any {
        val positions = Positions()
        puzzleInput.map { splitMoveCommand(it) }
            .forEach { moveH(it.first, it.second, positions, 1) }

        return positions.tVisitedPositions.count()
    }

    override fun solveSecondPart(): Any {
        val positions = Positions()
        puzzleInput.map { splitMoveCommand(it) }
            .forEach { moveH(it.first, it.second, positions, 9) }

        return positions.tVisitedPositions.count()
    }

    private fun splitMoveCommand(command: String): Pair<String, Int> {
        val splitCommand =  command.split(" ")

        return splitCommand[0] to splitCommand[1].toInt()
    }

    private fun moveH(direction: String, steps: Int, positions: Positions, ropeLength: Int) {
        if (steps == 0) {
            return
        }

        when (direction) {
            "R" -> positions.ropePosition[0] = positions.ropePosition[0].copy(first = positions.ropePosition[0].first.inc())
            "L" -> positions.ropePosition[0] = positions.ropePosition[0].copy(first = positions.ropePosition[0].first.dec())
            "U" -> positions.ropePosition[0] = positions.ropePosition[0].copy(second = positions.ropePosition[0].second.inc())
            "D" -> positions.ropePosition[0] = positions.ropePosition[0].copy(second = positions.ropePosition[0].second.dec())
        }

        for (i in 1..ropeLength) {
            if (!isTTouching(positions, i)) {
                moveTCloser(positions, i, ropeLength)
            }
        }

        moveH(direction, steps.dec(), positions, ropeLength)
    }

    private fun isTTouching(positions: Positions, tailPosition: Int): Boolean {
        val isTouchingHorizontal = abs(positions.ropePosition[tailPosition.dec()].first - positions.ropePosition[tailPosition].first)
        val isTouchingVertical = abs(positions.ropePosition[tailPosition.dec()].second - positions.ropePosition[tailPosition].second)


     return ((isTouchingHorizontal == 0) || (isTouchingHorizontal == 1)) &&
             ((isTouchingVertical == 0) ||  (isTouchingVertical == 1))
    }

    private fun moveTCloser(positions: Positions, tailPosition: Int, lastTail: Int) {
        with (positions) {
            val previousRope = ropePosition[tailPosition.dec()]
            val currentRope = ropePosition[tailPosition]

            // move left
            if (previousRope.second == currentRope.second && previousRope.first < currentRope.first) {
                ropePosition[tailPosition] = currentRope.copy(first = currentRope.first.dec())
            }

            //move right
            else if (previousRope.second == currentRope.second && previousRope.first > currentRope.first) {
                ropePosition[tailPosition] = currentRope.copy(first = currentRope.first.inc())
            }

            // move up
            else if (previousRope.first == currentRope.first && previousRope.second > currentRope.second) {
                ropePosition[tailPosition] = currentRope.copy(second = currentRope.second.inc())
            }

            // move down
            else if (previousRope.first == currentRope.first && previousRope.second < currentRope.second) {
                ropePosition[tailPosition] = currentRope.copy(second = currentRope.second.dec())
            }

            // move upper-left
            else if (previousRope.first < currentRope.first && previousRope.second > currentRope.second) {
                ropePosition[tailPosition] = currentRope.copy(first = currentRope.first.dec(), second = currentRope.second.inc())
            }

            // move upper-right
            else if (previousRope.first > currentRope.first && previousRope.second > currentRope.second) {
                ropePosition[tailPosition] = currentRope.copy(first = currentRope.first.inc(), second = currentRope.second.inc())
            }

            // move lower-left
            else if (previousRope.first < currentRope.first && previousRope.second < currentRope.second) {
                ropePosition[tailPosition] = currentRope.copy(first = currentRope.first.dec(), second = currentRope.second.dec())
            }

            // move lower right
            else if (previousRope.first > currentRope.first && previousRope.second < currentRope.second) {
                ropePosition[tailPosition] = currentRope.copy(first = currentRope.first.inc(), second = currentRope.second.dec())
            }
        }

        if (tailPosition == lastTail) {
            positions.tVisitedPositions.add(positions.ropePosition[tailPosition])
        }
    }
}