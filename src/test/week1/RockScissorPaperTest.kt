package week1

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

internal class RockScissorPaperTest : FunSpec({
    test("solve part 1") {
        val puzzle = RockScissorPaper()
        puzzle.solveFirstPart() shouldBe 10816
    }
    test("solve part 2") {
        val puzzle = RockScissorPaper()
        puzzle.solveSecondPart() shouldBe 11657
    }
})
