package shared

class ReadUtils {
    companion object {
        fun readPuzzleInput(fileName: String) =
            this::class.java.classLoader.getResourceAsStream(fileName)
                ?.bufferedReader()
                ?.readLines()!!
    }
}
