package week3

import shared.Puzzle
import shared.ReadUtils.Companion.debug
import kotlin.math.pow

class HotAirBalloons : Puzzle(25) {
    infix fun Int.pow(exponent: Int): Long = toDouble().pow(exponent).toLong()

    override fun solveFirstPart(): Any {
        return puzzleInput.map { toDecimal(it) }
            .sum()
            .also { println(it) }
            .run { println(toSnafu(this)) }
    }

    override fun solveSecondPart(): Any {
        TODO("Not yet implemented")
    }

    private fun toSnafu(decimal: Long): String {
        return toSnafu2(decimal, listOf())
    }

    private tailrec fun toSnafu2(division: Long, acc: List<Int>): String {
        if (division == 0L) {
            return convertPentalToSnafu(acc)
        }

        val divisionNew = division / 5
        val remainder = division % 5

        return toSnafu2(divisionNew, acc.plus(remainder.toInt()))
    }

    private fun convertPentalToSnafu(pental: List<Int>): String {
        debug("Converting pental to snafu $pental")

        val snafu = mutableListOf<Char>()

        var carry = 0

        for (i in pental) {
            val conversionResult = pentalToSnafu(i + carry)
            snafu.add(conversionResult.first)
            carry = conversionResult.second

            debug("current snafu: $snafu i: $i carry: $carry")
        }

        if (carry > 0) {
            snafu.add('1')
        }

        return snafu.joinToString("").reversed()
    }

    private fun toDecimal(snafu: String) =
        toDecimal2(snafu.reversed(), 0, 0)

    private tailrec fun toDecimal2(snafu: String, acc: Long, level: Int): Long {
        if (snafu.isEmpty()) {
            return acc
        }

        val multiplier = 5 pow level
        val snafuChar = snafu.first()

        debug("converting $snafu with multiplier $multiplier")

        return toDecimal2(snafu.drop(1), acc.plus(multiplier * snafuToDecimal(snafuChar)), level.inc())
    }

    private fun snafuToDecimal(snafu: Char) =
        when (snafu) {
            '-' -> -1
            '=' -> -2
            '0' -> 0
            '1' -> 1
            '2' -> 2
            else -> throw IllegalArgumentException()
        }

    private fun pentalToSnafu(pental: Int) =
        when (pental) {
            0 -> '0' to 0
            1 -> '1' to 0
            2 -> '2' to 0
            3 -> '=' to 1
            4 -> '-' to 1
            5 -> '0' to 1
            else -> throw IllegalArgumentException()
        }
}
