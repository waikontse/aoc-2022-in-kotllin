package week3

import shared.Puzzle
import week3.PyroFlow.Tunnel

class PyroFlow : Puzzle(17) {
    companion object {
        const val BLANK = '.'
        const val BLOCK = '@'
        const val STUCK = '#'
        val EMPTY_ROW = charArrayOf('.', '.', '.', '.', '.', '.', '.')
        val `3_EMPTY_ROWS` = arrayOf(EMPTY_ROW, EMPTY_ROW, EMPTY_ROW)
    }

    enum class Block(val config: Array<CharArray>) {
        SLEEPING_I(
            arrayOf(
                charArrayOf('.', '.', '@', '@', '@', '@', '.')
            )
        ),
        CROSS(
            arrayOf(
                charArrayOf('.', '.', '.', '@', '.', '.', '.'),
                charArrayOf('.', '.', '@', '@', '@', '.', '.'),
                charArrayOf('.', '.', '.', '@', '.', '.', '.'),
            )
        ),
        REVERSE_L(
            arrayOf(
                charArrayOf('.', '.', '.', '.', '@', '.', '.'),
                charArrayOf('.', '.', '.', '.', '@', '.', '.'),
                charArrayOf('.', '.', '@', '@', '@', '.', '.'),
            )
        ),
        I(
            arrayOf(
                charArrayOf('.', '.', '@', '.', '.', '.', '.'),
                charArrayOf('.', '.', '@', '.', '.', '.', '.'),
                charArrayOf('.', '.', '@', '.', '.', '.', '.'),
                charArrayOf('.', '.', '@', '.', '.', '.', '.'),
            )
        ),
        SQUARE(
            arrayOf(
                charArrayOf('.', '.', '@', '@', '.', '.', '.'),
                charArrayOf('.', '.', '@', '@', '.', '.', '.'),
            )
        ),
    }

    data class Tunnel(val hotGasConfig: String, val width: Int) {
        val tunnel: MutableList<CharArray>
        init {
            tunnel = MutableList(1) { _ -> CharArray(7) }
        }

        fun dropBlock(block: Block, blockNumber: Int = 0) {
            // Extend the tunnel with 3 high
            tunnel += `3_EMPTY_ROWS`

            // Add the block to the tunnel starting position
            tunnel += block.config

            // Get the config for the flow
            getNextJetFlows(blockNumber)

            // try to drop the block as much as possible
        }

        fun getNextJetFlows(blockNumber: Int): List<Char> =
            (blockNumber.times(5)until blockNumber.plus(1).times(5))
                .map { hotGasConfig[it%hotGasConfig.length] }
    }


    override fun solveFirstPart(): Any {
        val tunnel = Tunnel(exampleInput[0], 7)

        println(exampleInput)
        println(exampleInput[0].length)

        // print some flows
        for (i in 0..9) {
            println("$i: ${tunnel.getNextJetFlows(i)}")
        }

        return 0
    }

    override fun solveSecondPart(): Any {
        TODO("Not yet implemented")
    }
}
