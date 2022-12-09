package shared

import week1.DiskDrive

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
    }
}
