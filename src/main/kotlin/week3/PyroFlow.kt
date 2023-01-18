package week3

import shared.Puzzle

class PyroFlow : Puzzle(17) {
    enum class Block {
        SLEEPING_I,
        CROSS,
        REVERSE_L,
        I,
        SQUARE,
    }

    data class Tunnel(val hotGasConfig: String, val width: Int) {
        val tunnel: MutableList<List<Char>>
        init {
            tunnel = MutableList(width + 2) { mutableListOf<Char>() }
        }

        fun dropBlock(block: Block) {

        }

        private fun getNextJetFlows() {

        }
    }


    override fun solveFirstPart(): Any {
        val tunnel =

        println(exampleInput)

        return 0
    }

    override fun solveSecondPart(): Any {
        TODO("Not yet implemented")
    }
}
