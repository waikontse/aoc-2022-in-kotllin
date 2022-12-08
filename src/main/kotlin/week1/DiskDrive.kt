package week1

import shared.Puzzle

class DiskDrive : Puzzle(7) {
    enum class NodeType {
        DIR,
        FILE
    }

    data class Node(
        val parent: Node?,
        val type: NodeType,
        var fileSize: Int,
        val name: String
    ) {
        val nodes: MutableList<Node> = mutableListOf()
        fun addDir(name: String) =
            this.apply { nodes.add(Node(this, NodeType.DIR, 0, name)) }

        fun addFile(size: Int, name: String) =
            this.apply { nodes.add(Node(this, NodeType.FILE, size, name)) }
    }

    override fun solveFirstPart(): Any {
        val rootNode = getRootNode(puzzleInput)
        return findTarget(listOf(rootNode), emptyList()) { node: Node -> node.type == NodeType.DIR && node.fileSize <= 100000 }
            .sum()
    }

    override fun solveSecondPart(): Any {
        val rootNode = getRootNode(puzzleInput)
        val totalFsSize = 70000000
        val targetFreeSize = 30000000
        val currentFreeSize = totalFsSize - rootNode.fileSize
        val neededFreeSize = targetFreeSize - currentFreeSize

        return findTarget(listOf(rootNode), emptyList()) { node: Node -> (node.type == NodeType.DIR) && (node.fileSize >= neededFreeSize) }
            .filter { it > 0 }
            .minOf { it }
    }

    private fun getRootNode(input: List<String>): Node {
        val rootNode = Node(null, NodeType.DIR, 0, "/")
        parseFolder(input, rootNode)
        calculateAndPropagateSize(listOf(rootNode))
        return rootNode
    }

    private tailrec fun parseFolder(lines: List<String>, nodes: Node) {
        if (lines.isEmpty()) {
            return
        }

        parseFolder(lines.drop(1), parseCommandLine(lines.first(), nodes))
    }

    private fun parseCommandLine(line: String, nodes: Node): Node {
        return when (line) {
            "$ cd /" -> nodes
            "$ cd .." -> nodes.parent!!
            "$ ls" -> nodes
            else -> {
                parseDirEntries(line, nodes)
            }
        }
    }

    private val cdToRegex = """\$ cd (\w+)""".toRegex()
    private val dirRegex = """dir (\w+)""".toRegex()
    private val fileRegex = """(\d+) (\p{Graph}+)""".toRegex()

    private fun parseDirEntries(line: String, nodes: Node): Node {
        return if (cdToRegex.matches(line)) {
            val (dirName) = cdToRegex.matchEntire(line)?.destructured ?: throw IllegalArgumentException()
            nodes.nodes.find { it.name == dirName }!!
        } else if (dirRegex.matches(line)) {
            val (dirName) = dirRegex.matchEntire(line)?.destructured ?: throw IllegalArgumentException()
            nodes.addDir(dirName)
        } else if (fileRegex.matches(line)) {
            val (fileSize, fileName) = fileRegex.matchEntire(line)?.destructured ?: throw IllegalArgumentException()
            nodes.addFile(fileSize.toInt(), fileName)
        } else {
            throw IllegalArgumentException("Could not parse line: $line")
        }
    }

    private fun calculateAndPropagateSize(acc: List<Node>): Int {
        if (acc.isEmpty()) {
            return 0
        }

        return acc.sumOf { calculateSize(it) }
    }

    private fun calculateSize(node: Node): Int {
        return when (node.type) {
            NodeType.FILE -> node.fileSize
            NodeType.DIR -> {
                node.fileSize = calculateAndPropagateSize(node.nodes)
                return node.fileSize
            }
        }
    }

    private tailrec fun findTarget(
        stack: List<Node>,
        acc: List<Int>,
        predicate: (Node) -> Boolean
    ): List<Int> {
        if (stack.isEmpty()) {
            return acc
        }

        val fileSize = if (predicate(stack.first())) stack.first().fileSize else 0

        return findTarget(stack.drop(1) + stack.first().nodes, acc + fileSize, predicate)
    }
}
