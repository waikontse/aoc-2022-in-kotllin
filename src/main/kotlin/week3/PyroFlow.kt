package week3

import shared.Puzzle

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
            tunnel = MutableList(1) { _ -> CharArray(7) { BLANK } }
        }

        fun dropBlock(block: Block, blockNumber: Int = 0) {
            prepareTunnelForNewBlock(block)

            // Get the config for the flow
            getNextJetFlows(blockNumber)

            // try to drop the block as much as possible
        }

        private fun prepareTunnelForNewBlock(block: Block) {
            // Extend the tunnel with 3 high
            tunnel += `3_EMPTY_ROWS`

            // Add the block to the tunnel starting position
            tunnel += block.config
        }

        fun canDrop(block: Block, lowestRow: Int): Boolean {
            return false
        }

        fun canShiftLeft(block: BlockConfig): Boolean {
            return false
        }

        fun canShiftRight(block: BlockConfig): Boolean {

        }

        fun shiftLeft(block: BlockConfig) {

        }

        fun shiftRight(config: BlockConfig) {

        }

        fun getNextJetFlows(blockNumber: Int): List<Char> =
            (blockNumber.times(5)until blockNumber.plus(1).times(5))
                .map { hotGasConfig[it%hotGasConfig.length] }

        fun printTunnel() =
            tunnel.forEach { println(it.joinToString(separator = "")) }
    }


    override fun solveFirstPart(): Any {
        val tunnel = Tunnel(exampleInput[0], 7)

        println(exampleInput)
        println(exampleInput[0].length)

        // print some flows
        for (i in 0..9) {
            println("$i: ${tunnel.getNextJetFlows(i)}")
        }

        tunnel.printTunnel()

        return 0
    }

    override fun solveSecondPart(): Any {
        TODO("Not yet implemented")
    }
}

data class BlockConfig(
    val leftMost: Int,
    val rightMost: Int,
    val currentRow: Int
)
