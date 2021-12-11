import dto.FirstFunction
import dto.FollowFunction
import dto.Grammar
    val table = Table()
        initializeTable()

class Parser(grammar: Grammar) {
    val first = FirstFunction(grammar)
    val follow = FollowFunction(grammar, first)
    private fun initializeTable() {
        val terminalsWithEpsilon = grammar.terminals + arrayListOf(EPSILON)

        grammar.productions.forEach { (rowSymbol, tokenList) ->
            tokenList.forEach { tokens ->
                terminalsWithEpsilon.forEach {
                    applyFirstRule(tokens, rowSymbol, it)
                }
            }
        }

        applySecondRule()
        applyThirdRule()
    }

    private fun applyFirstRule(tokens: List<String>, rowSymbol: String, columnSymbol: String) {
        val firstToken = tokens.first()

        /** Part one */
        if (firstToken == columnSymbol && columnSymbol != EPSILON) {
            table[rowSymbol, columnSymbol] = tokens
            return
        }

        if (firstToken in grammar.nonTerminals && columnSymbol in (firstMap[firstToken] ?: return)) {
            if (table.hasKey(rowSymbol, columnSymbol))
                error("($rowSymbol, $columnSymbol) found in table, grammar is not LL(1)!")

            table[rowSymbol, columnSymbol] = tokens
            return
        }

        if (firstToken == EPSILON) {
            followMap[rowSymbol]?.forEach {
                table[rowSymbol, if (it == EPSILON) END_TERMINAL else it] = tokens
            }
            return
        }

        /** Part two */
        val firsts = grammar.productions[rowSymbol]
            ?.flatten()
            ?.filter { it in grammar.nonTerminals }
            ?.flatMapNotNull { firstMap[it] }
            ?.toSet()
            ?: return

        if (EPSILON !in firsts)
            return

        followMap[rowSymbol]?.map { if (it == EPSILON) END_TERMINAL else it }
            ?.filter { !table.hasKey(rowSymbol, it) }
            ?.forEach { table[rowSymbol, it] = tokens }
    }

    private fun applySecondRule() = grammar.terminals.forEach {
        table[it, it] = listOf("pop")
    }

    private fun applyThirdRule() {
        table[END_TERMINAL, END_TERMINAL] = listOf("acc")
}