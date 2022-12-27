package week2

import shared.Puzzle
import shared.ReadUtils.Companion.debug

class DistressSignal : Puzzle(13) {
    override fun solveFirstPart(): Any {
        return puzzleInput.asSequence().filter { it.isNotBlank() }
            .chunked(2)
            .map { parseEntry(it[0], 0) to parseEntry(it[1], 0) }
            .map { compareValues(it.first, it.second) }
            .mapIndexed { index, isInOrder -> if (isInOrder) index.inc() else -1 }
            .filter { it > 0 }
            .sum()
    }

    override fun solveSecondPart(): Any {
        val comparator = Comparator { left: List<*>, right: List<*> -> if (compareValues(left, right)) -1 else 1 }
        val target1 = listOf(listOf(2))
        val target2 = listOf(listOf(6))

        puzzleInput.asSequence().filter { it.isNotBlank() }
            .map { parseEntry(it, 0) }
            .plus(target1)
            .plus(target2)
            .sortedWith(comparator).toList()
            .let {
                val index1 = it.indexOf(listOf(2)).inc()
                val index2 = it.indexOf(listOf(6)).inc()

                return index1 * index2
            }
    }

    private fun parseEntry(entry: String, currPos: Int): List<Any> {
        debug("parsing $entry on pos: $currPos")

        return if (entry[currPos] == '[') {
            parseList(entry, currPos.inc()).second
        } else if (entry[currPos] == ',') {
            parseEntry(entry, currPos.inc())
        } else {
            throw IllegalArgumentException()
        }
    }

    private fun parseList(entry: String, currPos: Int): Pair<Int, List<Any>> {
        debug("Parsing list $entry on currPos: $currPos")

        val newList = mutableListOf<Any>()
        var currPosTemp = currPos

        while (entry[currPosTemp] != ']') {
            if (entry[currPosTemp] == '[') {
                val parsedList = parseList(entry, currPosTemp.inc())
                newList.add(parsedList.second)
                currPosTemp = parsedList.first
                debug("new currPosTemp: $currPosTemp")
            } else if (entry[currPosTemp] == ',') {
                debug("parsing comma")
                currPosTemp += 1
            } else {
                val parsedNumber = parseNumber(entry, currPosTemp)
                newList.add(parsedNumber.second)
                currPosTemp = parsedNumber.first
            }
        }

        return currPosTemp.inc() to newList
    }

    private fun parseNumber(entry: String, currPos: Int): Pair<Int, Int> {
        debug("Parsing number: $entry currPos: $currPos")

        var currPosTemp = currPos
        while (entry[currPosTemp].isDigit()) {
            currPosTemp += 1
        }

        val parsedInt = entry.subSequence(currPos, currPosTemp).toString().toInt()

        return currPosTemp to parsedInt
    }

    private fun compareValues(left: List<*>, right: List<*>): Boolean {
        return compareValues(left, right, isInOrder = false, shouldStop = false).first
    }

    private tailrec fun compareValues(
        left: List<*>,
        right: List<*>,
        isInOrder: Boolean,
        shouldStop: Boolean
    ): Pair<Boolean, Boolean> {
        debug("Comparing values $left * $right ** isInOrder: $isInOrder ***  shouldStop: $shouldStop")
        if (isInOrder) {
            return true to true
        } else if (shouldStop) {
            debug("returning result == $isInOrder")
            return isInOrder to true
        } else if (left.isEmpty() && right.isNotEmpty()) {
            return true to true
        } else if (left.isNotEmpty() && right.isEmpty()) {
            return false to true
        } else if (left.isEmpty() && right.isEmpty()) {
            debug("return false for left.empty right.empty")
            return isInOrder to shouldStop
        }

        val leftFirst = left.first()
        val rightFirst = right.first()
        val comparisonResult: Boolean
        val shouldStopNew: Boolean

        if (leftFirst is Int && rightFirst is Int) {
            val compareIntResult = compareInts(leftFirst, rightFirst)
            comparisonResult = compareIntResult.first
            shouldStopNew = compareIntResult.second
        } else if (leftFirst is List<*> && rightFirst is List<*>) {
            val compareListResult = compareLists(leftFirst, rightFirst)
            comparisonResult = compareListResult.first
            shouldStopNew = compareListResult.second
        } else if (leftFirst is List<*> && rightFirst is Int) {
            val compareListResult = compareLists(leftFirst, listOf(rightFirst))
            comparisonResult = compareListResult.first
            shouldStopNew = compareListResult.second
        } else if (leftFirst is Int && rightFirst is List<*>) {
            val compareListResult = compareLists(listOf(leftFirst), rightFirst)
            comparisonResult = compareListResult.first
            shouldStopNew = compareListResult.second
        } else {
            throw IllegalArgumentException()
        }

        return compareValues(left.drop(1), right.drop(1), comparisonResult, shouldStopNew)
    }

    private fun compareInts(left: Int, right: Int): Pair<Boolean, Boolean> {
        debug("Comparing ints: $left : $right")

        return (left < right) to (left != right)
    }

    private fun compareLists(left: List<*>, right: List<*>): Pair<Boolean, Boolean> {
        debug("Comparing lists: $left * $right")

        return compareValues(left, right, isInOrder = false, shouldStop = false)
    }
}
