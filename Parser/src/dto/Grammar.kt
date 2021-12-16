package dto

import EPSILON
import once
import readFileIndexed

class Grammar {
    val terminals = arrayListOf<String>()
    val nonTerminals = arrayListOf<String>()
    var startingSymbol = ""
        private set
    val productions = hashMapOf<String, ArrayList<List<String>>>()
    private val productionKeyOrder = arrayListOf<String>()

    fun readFromFile(fileName: String) = readFileIndexed(fileName) { index, line ->
        when (index) {
            0 -> terminals += line.trim().split(" ")
            1 -> nonTerminals += line.trim().split(" ")
            2 -> startingSymbol = line.trim()
            else -> readProduction(line)
        }
    }

    fun validateStartingSymbol() {
        if (startingSymbol !in nonTerminals)
            error("Invalid starting symbol '$startingSymbol' not found in non-terminals list!")
    }

    fun validateProductions() = productions.forEach { (key, values) ->
        if (key !in nonTerminals)
            error("Invalid production '$key' not found in non-terminals list!")
        values.flatten().firstOrNull { it !in terminals && it !in nonTerminals && it != EPSILON }?.let {
            error("Invalid production '$it' not found in terminals or non-terminals list!")
        }
    }

    fun getProductions(nonTerminal: String) =
        productions[nonTerminal] ?: error("Invalid non-terminal '$nonTerminal' not found in list")

    fun isContextFree() = productions.keys.all { key -> nonTerminals.once { it == key } }

    fun getProductionsInOrder() = productionKeyOrder.map { it to productions[it]!! }

    fun getProduction(index: Int) = getProductionsInOrder()
        .flatMap { (key, tokens) -> tokens.map { key to it } }
        .let {
            if (index in 1..it.size)
                it[index - 1]
            else
                errorProductionByIndex(index)
        }

    private fun errorProductionByIndex(index: Int): Nothing =
        error("Index $index not in range of productions (1..${productionKeyOrder.size})")

    private fun readProduction(line: String) {
        val (key, values) = line.split(" -> ")
        val valuesList = values.split(" | ")
        val tokens = valuesList.map { it.split(" ") }

        if (productions[key] == null)
            productions[key] = ArrayList()

        productionKeyOrder.add(key)
        productions[key]?.addAll(tokens)
    }

    override fun toString() = "Terminals: $terminals\n" +
            "Non-terminals: $nonTerminals\n" +
            "Starting symbol: $startingSymbol\n" +
            "Productions: $productions"
}
