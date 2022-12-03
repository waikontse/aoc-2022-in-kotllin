package week1

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

internal class CaloriesTest : FunSpec({
    test("Part 1 should give the correct answer") {
        val puzzle = Calories()
        puzzle.solveFirstPart() shouldBe 71502
    }
    test("Part 2 should give the correct answer") {
        val puzzle = Calories()
        puzzle.solveSecondPart() shouldBe 208191
    }
})
