class Parser(private val grammar: Grammar) {
    val firstMap = HashMap(grammar.nonTerminals.associateWith { hashSetOf<String>() })
    val followMap = HashMap(grammar.nonTerminals.associateWith { hashSetOf<String>() })

    init {
        initializeFirstMap()
        initializeFollowMap()
    }

    private tailrec fun initializeFirstMap() {
        var changed = false
        grammar.productions.forEach { (key, values) ->
            values.forEach {
                changed = addTokensInSet(firstMap[key], it, hashSetOf(EPSILON)) || changed
            }
        }

        if (changed)
            initializeFirstMap()
    }

    private tailrec fun initializeFollowMap(firstCall: Boolean = true) {
        if (firstCall)
            followMap[grammar.startingSymbol]?.add(EPSILON)

        var changed = false
        grammar.productions.forEach { (key, values) ->
            values.forEach { tokens ->
                tokens.filter { it in grammar.nonTerminals }
                    .forEachIndexed { index, token ->
                        followMap[token]?.let {
                            val tokensForKey = followMap[key] ?: hashSetOf()
                            val listWasModified = if (index > tokens.size)
                                it.addAll(tokensForKey)
                            else
                                addTokensInSet(it, tokens.subList(index + 1, tokens.size), tokensForKey)

                            changed = listWasModified || changed
                        }
                    }
            }
        }

        if (changed)
            initializeFollowMap(false)
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