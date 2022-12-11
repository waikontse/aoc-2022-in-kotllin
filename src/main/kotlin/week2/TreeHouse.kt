package week2

import shared.Puzzle

import shared.ReadUtils.Companion.toIntList
import shared.ReadUtils.Companion.transpose

typealias Pos = Pair<Int, Int>
class Trees(val treesMap: List<List<Int>>) {
    val maxX: Int = treesMap[0].size
    val maxY: Int = treesMap.size
    val transposed: List<List<Int>> = treesMap.transpose()

}
class TreeHouse : Puzzle(8) {
    override fun solveFirstPart(): Any {
        val intTreesMap = Trees(puzzleInput.map { it.toIntList() })
        val corners = (2*intTreesMap.maxX + 2*intTreesMap.maxY - 4)

        return countVisibleTrees(intTreesMap) + corners
    }

    override fun solveSecondPart(): Any {
        val trees = Trees(puzzleInput.map { it.toIntList() })

        return countScenicScore(trees, 1 to 1, listOf())
            .max()
    }

    private fun countVisibleTrees(trees: Trees): Int {
        return countVisibleTreeLR(trees, 1 to 1, 0)
    }

    private tailrec fun countVisibleTreeLR(trees: Trees, pos: Pos, acc: Int): Int {
        if (pos.first == 1 && pos.second >= trees.maxY-1) {
            return acc
        }

        val canSee = if (isTreeVisible(trees, pos)) 1 else 0

        return countVisibleTreeLR(trees, pos.inc(trees), acc.plus(canSee))
    }

    private fun Pos.inc(trees: Trees): Pos {
        return if (this.first >= trees.maxX-2) {
            this.copy(first = 1, second = this.second.inc())
        } else {
            this.copy(first = this.first.inc())
        }
    }

    private fun isTreeVisible(trees: Trees, pos: Pos): Boolean {
        return canSeeTreeFromLeft(trees, pos)
                || canSeeTreeFromRight(trees, pos)
                || canSeeTreeFromTop(trees, pos)
                || canSeeTreeFromBottom(trees, pos)
    }

    private fun canSeeTreeFromLeft(trees: Trees, pos: Pos) =
        canSeeTree(trees.treesMap[pos.second], pos.first)

    private fun canSeeTreeFromRight(trees: Trees, pos: Pos) =
        canSeeTree(trees.treesMap[pos.second].reversed(), trees.maxX-1-pos.first)

    private fun canSeeTreeFromTop(trees: Trees, pos: Pos) =
        canSeeTree(trees.transposed[pos.first], pos.second)

    private fun canSeeTreeFromBottom(trees: Trees, pos: Pos) =
        canSeeTree(trees.transposed[pos.first].reversed(), trees.maxX-1-pos.second)

    private fun canSeeTree(trees: List<Int>, pos: Int): Boolean {
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
    private tailrec fun countScenicScore(trees: Trees, pos: Pos, acc: List<Int>): List<Int> {
        if (pos.first == 1 && pos.second >= trees.maxY-1) {
            return acc
        }

        val scenicScore: Int = scenicScore(trees, pos)
        return countScenicScore(trees, pos.inc(trees), acc.plus(scenicScore))
    }

    private fun scenicScore(trees: Trees, pos: Pos): Int {
        return countScenicScoreLeft(trees.treesMap[pos.second], pos.first, pos.first.dec(), 0) *
                countScenicScoreRight(trees.treesMap[pos.second], pos.first, pos.first.inc(),0) *

                countScenicScoreTop(trees.transposed[pos.first], pos.second, pos.second.dec(),0) *
                countScenicScoreBottom(trees.transposed[pos.first], pos.second, pos.second.inc(), 0)
    }

    private fun treeIsSmaller(trees: List<Int>, from: Int, to: Int) = trees[from] <= trees[to]

    private tailrec fun countScenicScoreLeft(trees: List<Int>, from: Int, to: Int, acc: Int): Int {
        if (to == -1) {
            return acc
        } else if (treeIsSmaller(trees, from, to)) {
            return acc.inc()
        }

        return countScenicScoreLeft(trees, from, to.dec(), acc.inc())
    }

    private tailrec fun countScenicScoreRight(trees: List<Int>, from: Int, to: Int, acc: Int): Int {
        if (to == trees.size) {
            return acc
        } else if (treeIsSmaller(trees, from, to)) {
            return acc.inc()
        }

        return countScenicScoreRight(trees, from, to.inc(), acc.inc())
    }

    private fun countScenicScoreTop(trees: List<Int>, from: Int, to: Int, acc: Int) =
        countScenicScoreLeft(trees, from, to, acc)

    private fun countScenicScoreBottom(trees: List<Int>, from: Int, to: Int, acc: Int) =
        countScenicScoreRight(trees, from, to, acc)
}