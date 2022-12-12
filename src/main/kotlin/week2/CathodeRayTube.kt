package week2

import shared.Puzzle
import java.lang.IllegalArgumentException

data class Cpu(var currentCycle: Int = 0,
               var registerX: Int = 1,
               val crt: MutableList<Char> = MutableList(240) { '.' }
)

typealias CycleList = List<Pair<Int, Int>>

class CathodeRayTube : Puzzle(10) {
    override fun solveFirstPart(): Any {
        val cpu = Cpu()

        return runProgram(cpu, puzzleInput)
            .filter { (it.first-20).mod(40) == 0 }
            .groupBy { it.first}
            .map { it.value.first() }
            .sumOf { it.first * it.second }
    }

    override fun solveSecondPart(): Any {
        val cpu = Cpu()
        runProgram(cpu, puzzleInput)

        cpu.crt.chunked(40)
            .map { it.joinToString("") }
            .forEach { println(it) }

        return 0
    }

    private fun runProgram(cpu: Cpu, program: List<String>): CycleList {
        return runProgramRecur(cpu, program, listOf())
    }

    private tailrec fun runProgramRecur(cpu: Cpu, program: List<String>, cycleList: CycleList): CycleList {
        if (program.isEmpty()) {
            return cycleList
        }

        val command = program.first()
        val newCycleList = if (command.startsWith("noop")) {
            noop(cpu, cycleList)
        } else {
            addX(cpu, command, cycleList)
        }

        return runProgramRecur(cpu, program.drop(1), newCycleList)
    }

    private fun noop(cpu: Cpu, cycleList: CycleList) =
        incrementCycle(cpu, cycleList)


    private val addXRegex = """(\w+) (\p{Graph}+)""".toRegex()
    private fun addX(cpu: Cpu, command: String, cycleList: CycleList): CycleList {
        val (_, value) = addXRegex.matchEntire(command)?.destructured ?: throw IllegalArgumentException()

        return incrementCycle(cpu, cycleList)
            .let { incrementCycle(cpu, it) }
            .let { setRegister(cpu, cpu.registerX.plus(value.toInt()), it) }
    }

    private fun incrementCycle(cpu: Cpu, cycleList:  CycleList): CycleList {
        cpu.currentCycle = cpu.currentCycle.inc()
        drawPixel(cpu)

        return cycleList.plus(cpu.currentCycle to cpu.registerX)
    }

    private fun setRegister(cpu: Cpu, newRegisterValue: Int, cycleList: CycleList): CycleList  {
        cpu.registerX = newRegisterValue

        return cycleList.plus(cpu.currentCycle to cpu.registerX)
    }

    private fun drawPixel(cpu: Cpu) {
        cpu.crt[cpu.currentCycle-1] = getCharForCycle(cpu)
    }

    private fun getCharForCycle(cpu: Cpu): Char {
        return if (isCyleInWindow(cpu)) { '#' } else { '.' }
    }

    private fun isCyleInWindow(cpu: Cpu): Boolean {
        val crtPosition = if (cpu.currentCycle.mod(40) == 0) 40 else cpu.currentCycle.mod(40)
        val diff = crtPosition.dec().coerceAtLeast(cpu.registerX) - crtPosition.dec().coerceAtMost(cpu.registerX)

        return diff == 0 || diff == 1
    }
}