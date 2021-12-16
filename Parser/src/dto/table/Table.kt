package dto.table

import END_TERMINAL
import EPSILON
import dto.FirstFunction
import dto.FollowFunction
import dto.Grammar

class Table(
    private val grammar: Grammar,
    private val first: FirstFunction,
    private val follow: FollowFunction
) {
    private val data = hashMapOf<Pair<String, String>, Pair<List<String>, Int>>()

    init {
        initialize()
    }

    private fun initialize() {
        grammar.getProductionsInOrder().flatMap { (rowSymbol, tokenList) -> tokenList.map { rowSymbol to it } }
            .flatMapIndexed { index, (rowSymbol, tokens) -> mapToEntries(index + 1, rowSymbol, tokens) }
            .forEach { plusAssign(it) }

        fillPop()
        setAccept()
    }

    private fun mapToEntries(index: Int, rowSymbol: String, tokens: List<String>) =
        if (tokens.first() == EPSILON)
            follow[rowSymbol].map { TableData(rowSymbol, it, tokens, index) }
        else
            first.getConcatenationOfOne(tokens).map { TableData(rowSymbol, it, tokens, index) }

    private fun fillPop() = grammar.terminals.forEach {
        set(it, it, listOf(Operation.POP.value) to -1)
    }

    private fun setAccept() = set(END_TERMINAL, END_TERMINAL, listOf(Operation.ACCEPT.value) to -1)

    operator fun plusAssign(data: TableData) = with(data) {
        set(rowSymbol, columnSymbol, tokens to index)
    }

    operator fun set(rowSymbol: String, columnSymbol: String, value: Pair<List<String>, Int>) {
        val key = rowSymbol to columnSymbol

        if (key in data.keys)
            error("($rowSymbol, $columnSymbol) already in table, grammar is not LL(1)!\nFailed to insert $value")

        data[key] = value
    }

    operator fun get(row: String, column: String) =
        data[row to column] ?: error("Sequence not accepted! ($row, $column) not in parsing table!")

    override fun toString() = data.entries.joinToString("\n")
}
