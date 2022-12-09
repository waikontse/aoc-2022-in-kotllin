package week2

import shared.Puzzle
import shared.ReadUtils.Companion.toIntList

class TreeHouse : Puzzle(8) {
    override fun solveFirstPart(): Any {
        exampleInput.map { countVisibleTrees(it.toIntList()) }
            .forEach { println(it) }

        return 0
    }

    override fun solveSecondPart(): Any {
        TODO("Not yet implemented")
    }

    private fun countVisibleTrees(trees: List<Int>): Int {
        return countVisibleTreeLR(trees, 0, 0)
    }

    private tailrec fun countVisibleTreeLR(trees: List<Int>, pos: Int, acc: Int): Int {
        if (pos == trees.size) {
            return acc
        }

        val canSee = if (canSeeTree(trees, pos)) 1 else 0
        return countVisibleTreeLR(trees, pos.inc(), acc.plus(canSee))
    }

    private fun canSeeTree(trees: List<Int>, pos: Int): Boolean {
        // special case
        println("checking for pos: $pos")
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