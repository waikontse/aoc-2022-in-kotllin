package shared

import java.util.*

typealias AdjacencyMatrix = Array<IntArray>
typealias Node = Int

class Algorithms {
    data class Edge(val endPoint: Node, val cost: Int)
    data class GraphSize(val width: Int, val height: Int)

    class Graph(val size: GraphSize) {
        private val graph: MutableMap<Node, MutableList<Edge>> = mutableMapOf()

        init {
            (0 until (size.height * size.width)).onEach { graph[it] = mutableListOf() }
        }

        fun addEdges(node: Node, edges: List<Edge>) {
            graph[node]?.addAll(edges)
        }

        fun getEdges(node: Node): List<Edge> {
            return graph[node]?.toList() ?: listOf()
        }

        fun vertices(): Set<Node> = graph.keys

        fun verticesCount(): Int = graph.keys.size

        fun print() = graph.keys.onEach { println("From: $it with edges: ${getEdges(it)}") }
    }

    companion object {

        fun dijkstra(firstNode: Node, graph: Graph): Pair<IntArray, Map<Node, Edge>> {
            val distances = IntArray(graph.verticesCount()) { if (it == firstNode) 0 else Int.MAX_VALUE }
            val visited = BooleanArray(graph.verticesCount()) { it == firstNode }
            val pathMap = mutableMapOf<Node, Edge>()
            val pq = PriorityQueue<Edge> { left, right -> left.cost - right.cost }

            pq.offer(Edge(firstNode, 0))

            while (pq.isNotEmpty()) {
                val u = pq.poll().endPoint
                val distU = distances[u]

                for (edge in graph.getEdges(u)) {
                    val distV = distances[edge.endPoint]
                    val pathWeight = edge.cost + distU

                    if (!visited[edge.endPoint] || (distV > pathWeight)) {
                        visited[edge.endPoint] = true
                        distances[edge.endPoint] = pathWeight
                        pathMap[edge.endPoint] = edge
                        pq.offer(Edge(edge.endPoint, pathWeight))
                    }
                }
            }

            return Pair(distances, pathMap)
        }

        /**
         * Floyd-Warshall algorithm. An all pairs shortest path algorithm.
         * Slower than Dijkstra, but is very compact code.
         * N (the number of nodes) should be <= 450 (According to competitive programming book ed. 4)
         */
        fun floydWarshall(adjacencyMatrix: AdjacencyMatrix): AdjacencyMatrix {
            val adjacencyMatrixAllPairs = adjacencyMatrix.clone()
            val v = adjacencyMatrix.size

            for (k in 0 until v) {
                for (i in 0 until v) {
                    for (j in 0 until v) {
                        adjacencyMatrixAllPairs[i][j] = Math.min(adjacencyMatrixAllPairs[i][j], adjacencyMatrixAllPairs[i][k] + adjacencyMatrixAllPairs[k][j])
                    }
                }
            }

            return adjacencyMatrixAllPairs
        }
    }
}