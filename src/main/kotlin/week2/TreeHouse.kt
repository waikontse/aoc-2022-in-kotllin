package week2

import shared.Puzzle
import shared.ReadUtils.Companion.toIntList
import shared.ReadUtils.Companion.transpose

class TreeHouse : Puzzle(8) {
    override fun solveFirstPart(): Any {
        val intTreesMap = puzzleInput.map { it.toIntList() }
            .drop(1)
            .dropLast(1)
        // intTreesMap.forEach { println(it) }
        intTreesMap.forEach { println(it) }

        val forwardSum = intTreesMap.mapIndexed { index, ints -> countVisibleTrees(ints, index) }.sum()
        //println(forwardSum)

        val reverseSum = intTreesMap.map { it.reversed() }.sumOf { countVisibleTrees(it) }
        //println(reverseSum)



        val transposedTree = intTreesMap.transpose()
        val transposedForwardSum = transposedTree.sumOf { countVisibleTrees(it) }
        val transposedReverseSum = transposedTree.map { it.reversed() }.sumOf { countVisibleTrees(it) }

        //return forwardSum + reverseSum + transposedForwardSum + transposedReverseSum
        return forwardSum + reverseSum + (4*intTreesMap[0].size-4)
    }

    override fun solveSecondPart(): Any {
        TODO("Not yet implemented")
    }

    private fun countVisibleTrees(trees: List<Int>, index: Int): Int {
        return countVisibleTreeLR(trees, 1, 0)
    }

    private tailrec fun countVisibleTreeLR(trees: List<Int>, pos: Int, acc: Int): Int {
        if (pos >= trees.size.dec()) {
            return acc
        }

        val canSee = if (canSeeTree(trees, pos)) 1 else 0
        return countVisibleTreeLR(trees, pos.inc(), acc.plus(canSee))
    }

    private fun canSeeTree(trees: List<Int>, pos: Int): Boolean {
        // special case
        // println("checking for pos: $pos")
        if (pos == 0) {
            return true
        }

        // Check if largest number before
        val target = trees[pos]
        val before = trees.take(pos)

        // Check if target is the largest up till that point
        val isLargest = before.max() < target

        // Check if unique number before
        return isLargest && !before.contains(target)
    }
}