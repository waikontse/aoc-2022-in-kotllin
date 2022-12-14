package shared

import week1.DiskDrive

val debugOn = false
class ReadUtils {
    companion object {
        fun readPuzzleInput(fileName: String) =
            this::class.java.classLoader.getResourceAsStream(fileName)
                ?.bufferedReader()
                ?.readLines()!!

        fun printNodes(acc: List<DiskDrive.Node>) {
            if (acc.isEmpty()) {
                return
            }

            println(acc.first())

            return printNodes(acc.drop(1).plus(acc.first().nodes))
        }

        fun String.toIntList() = this.toCharArray().map { it.digitToInt() }

        fun List<List<Int>>.transpose(): List<List<Int>> {
            val numberOfRows = this.size
            val numberOfElements = this[0].size

            val transposedList = mutableListOf<List<Int>>()
            var transposedCol = mutableListOf<Int>()

            for (i in 0 until numberOfElements) {
                for (j in 0 until numberOfRows) {
                    transposedCol.add(this[j][i])
                }
                transposedList.add(transposedCol)
                transposedCol = mutableListOf()
            }

            return transposedList
        }

        fun debug(message: Any?) {
            if (debugOn) {
                println(message)
            }
        }
    }
}
