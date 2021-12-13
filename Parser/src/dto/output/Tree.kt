package dto.output

import EPSILON
import dto.Grammar

class Tree(private val grammar: Grammar, private val sequence: List<Int>) {
    lateinit var root: Node
    private var depthIndex = 1

    init {
        build()
    }

    fun print() {
        depthIndex = 1
        breadthFirstSearch(root)
    }

    private fun build() {
        val (key, tokenList) = grammar.getProduction(sequence.first())
        root = Node(key, buildRecursive(tokenList))
    }

    private fun buildRecursive(tokenList: List<String>, index: Int = 1): Node? {
        val isLastEpsilon = index == sequence.size && tokenList.lastOrNull() == EPSILON
        val isEmptyOrExceededIndex = tokenList.isEmpty() || index >= sequence.size

        if (!isLastEpsilon && isEmptyOrExceededIndex)
            return null

        return when (val currentSymbol = tokenList.first()) {
            in grammar.terminals -> Node(currentSymbol, sibling = buildRecursive(tokenList.drop(1), index))
            in grammar.nonTerminals -> {
                val transitionNumber = sequence[index]
                val (_, tokens) = grammar.getProduction(transitionNumber)

                Node(
                    currentSymbol,
                    buildRecursive(tokens, index + 1),
                    buildRecursive(tokenList.drop(1), index + 1)
                )
            }
            else -> Node(EPSILON)
        }
    }

    private fun breadthFirstSearch(
        node: Node?,
        fatherIndex: Int? = null,
        siblingIndex: Int? = null
    ): List<Pair<Node?, Int>> {
        node ?: return listOf()
        println("$depthIndex: ${node.value} $fatherIndex $siblingIndex")

        val index = depthIndex++

        val result = breadthFirstSearch(node.sibling, fatherIndex, index) +
                listOf(node.child to index)

        siblingIndex?.let {
            return result
        }

        result.forEach { (childNode, nodeIndex) ->
            breadthFirstSearch(childNode, nodeIndex)
        }

        return emptyList()
    }

    override fun toString() = root.toString()
}