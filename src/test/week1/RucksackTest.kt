package week1

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class RucksackTest : FunSpec({

    test("solveFirstPart should return the correct value") {
        val puzzle = Rucksack()
        puzzle.solveFirstPart() shouldBe 7811
    }

    test("solveSecondPart should return the correct value") {
        val puzzle = Rucksack()
        puzzle.solveSecondPart() shouldBe 2639
    }
})
