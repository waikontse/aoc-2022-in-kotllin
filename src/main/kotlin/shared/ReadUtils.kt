package shared

import java.nio.file.Files
import java.nio.file.Path

class ReadUtils {
    companion object {
        fun readPuzzleInput(fileName: String) : List<String> {
            val fileNameWithExt = """${fileName}.txt"""
            val pathOfFile = Path.of(fileNameWithExt)
            println(pathOfFile.toRealPath().toString())

            return Files.readAllLines(pathOfFile)
        }

        fun readPuzzleInput2(fileName: String) =
            this::class.java.classLoader.getResourceAsStream(fileName)
                ?.bufferedReader()
                ?.readLines()
    }

}