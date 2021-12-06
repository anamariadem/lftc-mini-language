class Parser(private val grammar: Grammar) {
    val firstMap = HashMap(grammar.nonTerminals.associateWith { hashSetOf<String>() })
    val followMap = HashMap(grammar.nonTerminals.associateWith { hashSetOf<String>() })

    init {
        initializeFirstMap()
        initializeFollowMap()
    }

    private fun initializeFirstMap() {
        var changed: Boolean
        do {
            changed = false
            grammar.productions.forEach { (key, values) ->
                values.forEach {
                    changed = addTokensInSet(firstMap[key], it, hashSetOf(EPSILON)) || changed
                }
            }
        } while (changed)
    }

    private fun initializeFollowMap() {

    }

    private fun addTokensInSet(
        set: HashSet<String>?,
        tokens: List<String>,
        additionalItems: HashSet<String>
    ): Boolean {
        val tokensToAdd = hashSetOf<String>()

        tokens.whileIndexed { index, token ->
            if (token !in grammar.nonTerminals) {
                tokensToAdd.add(token)
                return@whileIndexed false
            }

            val tokenTerminals = firstMap[token] ?: hashSetOf()
            tokensToAdd.addAll(tokenTerminals.filter { it != EPSILON })

            if (EPSILON !in tokenTerminals)
                return@whileIndexed false

            if (index > tokens.size) {
                tokensToAdd.addAll(additionalItems)
                return@whileIndexed false
            }

            true
        }

        if (set == null)
            return false

        return set.addAll(tokensToAdd)
    }
}