package week1

import shared.Puzzle

class Rucksack : Puzzle(3) {
    override fun solveFirstPart(): Any {
        return puzzleInput
            .map { splitRucksack(it) }
            .map { it.map { charArray -> charArray.toSet() } }
            .map { getMisplacedSnackType(it, 2) }
            .sumOf { snackToPoint(it) }
    }

    override fun solveSecondPart(): Any {
        return puzzleInput.chunked(3)
            .map { it.map { bag -> bag.toSet() } }
            .map { getMisplacedSnackType(it, 3) }
            .sumOf { snackToPoint(it) }
    }

    private fun splitRucksack(rucksacks: String) =
        listOf(
            rucksacks.substring(0, rucksacks.length / 2).toCharArray(),
            rucksacks.substring(rucksacks.length / 2).toCharArray()
        )

    private fun getMisplacedSnackType(rucksacks: List<Set<Char>>, snackCount: Int): Char =
        rucksacks.map { it.toCharArray() }
            .reduce { acc, arr -> acc + arr }
            .groupBy { it }
            .filterValues { it.size == snackCount }
            .keys
            .first()

    private fun snackToPoint(snack: Char) =
        if (snack.isUpperCase()) snack.code - 38 else snack.code - 96
}
