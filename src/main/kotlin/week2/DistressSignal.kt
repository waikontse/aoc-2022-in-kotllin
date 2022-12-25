package week2

import shared.Puzzle

class DistressSignal: Puzzle(13) {
    override fun solveFirstPart(): Any {
        val cleanedInput = exampleInput.filter { it.isNotBlank() }
            .chunked(2)
            .map { it.joinToString(separator = " * ") }

        val parsed = cleanedInput.map { it.split(" * ") }
            .map { parseEntry(it[0], 0) to parseEntry(it[1], 0) }

        val comparedValue = compareValues(parsed[0].first, parsed[0].second, false, false)
        println(comparedValue)

//        val comparisonValues = parsed.map { compareValues(it.first, it.second, false, false) }
//        val result = comparisonValues
//            .mapIndexed { index, b -> if (b) index.inc() else -1 }
//            .filter { it > 0 }
//            .sum()
//
//        println(result)
//        println(comparisonValues)

        return 0
    }

    override fun solveSecondPart(): Any {
        TODO("Not yet implemented")
    }

    private fun parseEntry(entry: String, currPos: Int): List<Any> {
        println("parsing $entry on pos: $currPos")

        return if (entry[currPos] == '[') {
            parseList(entry, currPos.inc()).second
        } else if (entry[currPos] == ',') {
            parseEntry(entry, currPos.inc())
        } else {
            throw IllegalArgumentException()
        }
    }

    private fun parseList(entry: String, currPos: Int): Pair<Int, List<Any>> {
        println("Parsing list $entry on currPos: $currPos")

        val newList = mutableListOf<Any>()
        var currPosTemp = currPos

        while (entry[currPosTemp] != ']') {
            if (entry[currPosTemp] == '[') {
                val parsedList = parseList(entry, currPosTemp.inc())
                newList.add(parsedList.second)
                currPosTemp = parsedList.first
                println("new currPosTemp: $currPosTemp")
            } else if (entry[currPosTemp] == ',') {
                println("parsing comma")
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
        println("Parsing number: $entry currPos: $currPos")

        var currPosTemp = currPos
        while (entry[currPosTemp].isDigit()) {
            currPosTemp += 1
        }

        val parsedInt = entry.subSequence(currPos, currPosTemp).toString().toInt()

        return currPosTemp to parsedInt
    }

    private tailrec fun compareValues(left: List<*>, right: List<*>, isInOrder: Boolean, shouldStop: Boolean): Boolean {
        println("Comparing values $left * $right ** isInOrder: $isInOrder ***  shouldStop: $shouldStop")
        if (shouldStop) {
            println("returning result == $isInOrder")
            return isInOrder
        } else if (left.isEmpty() && right.isNotEmpty()) {
            return true
        } else if (left.isNotEmpty() && right.isEmpty()) {
            return isInOrder
        } else if (left.isEmpty() && right.isEmpty()) {
            println("return false for left.empty right.empty")
            return isInOrder
        }


        val leftFirst = left.first()
        val rightFirst = right.first()
        var comparisonResult: Boolean = false
        var shouldStopNew: Boolean = shouldStop

        if (leftFirst is Int && rightFirst is Int) {
            val compareIntResult = compareInts(leftFirst, rightFirst)
            comparisonResult = compareIntResult.first
            shouldStopNew = compareIntResult.second
        } else if (leftFirst is List<*> && rightFirst is List<*>) {
            comparisonResult = com(leftFirst, rightFirst)
        } else if (leftFirst is List<*> && rightFirst is Int) {
            comparisonResult = compareLists(leftFirst, listOf(rightFirst))
        } else if (leftFirst is Int && rightFirst is List<*>) {
            comparisonResult = compareLists(listOf(leftFirst), rightFirst)
        } else {
            throw IllegalArgumentException()
        }

        return compareValues(left.drop(1), right.drop(1), comparisonResult, shouldStopNew)
    }

    private fun compareInts(left: Int, right: Int): Pair<Boolean, Boolean> {
        println("Comparing ints: $left : $right")

        return (left < right) to (left != right)
    }

//    private fun compareLists(left: List<*>, right: List<*>): Boolean {
//        println("Comparing lists: $left * $right")
//
//        return compareValues(left, right, false, false)
//    }
}