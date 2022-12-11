package week2

import shared.Puzzle

class Cpu {
    var currentCycle: Int = 0
    var registerX: Int = 1
}

class CathodeRayTube : Puzzle(10) {
    override fun solveFirstPart(): Any {
        TODO("Not yet implemented")
    }

    override fun solveSecondPart(): Any {
        TODO("Not yet implemented")
    }

    private fun runProgram(cpu: Cpu, )

    private fun noop(cpu: Cpu) {
        cpu.currentCycle.inc()
    }

    private fun addX(cpu: Cpu, command: String) {
        incrementCycle(cpu, listOf())
        incrementCycle(cpu, listOf())
        cpu.registerX = cpu.registerX.plus(other)
    }

    private fun incrementCycle(cpu: Cpu, cycleList: List<Int>) {

    }
}