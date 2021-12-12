package dto

import EPSILON

class FirstFunction(private val grammar: Grammar) {
    private val map = HashMap(grammar.nonTerminals.associateWith { hashSetOf<String>() })

    init {
        initialize()
    }

    private fun initialize() = grammar.productions.keys.forEach {
        getOrCreate(it)
    }

    private fun getOrCreate(key: String): HashSet<String> {
        /** Get */
        get(key).let { it.isNotEmpty() && return it }

        /** Create */
        with(grammar.productions[key] ?: error("Key $key not found in productions!")) {
            forEach {
                add(key, computeConcatenationOfOne(key, it))
            }
        }

        return get(key)
    }

    private fun computeConcatenationOfOne(key: String, tokenSequence: List<String>): HashSet<String> {
        if (tokenSequence.isEmpty())
            return hashSetOf()

        val firstToken = tokenSequence.first()
        if (tokenSequence.size < 2) {
            return analyzeToken(key, firstToken)
        }

        val tokenValues = getOrCreate(firstToken)
        if (EPSILON !in tokenValues)
            return tokenValues

        tokenValues.remove(EPSILON)
        return tokenValues.apply { addAll(computeConcatenationOfOne(firstToken, tokenSequence.drop(1))) }
    }

    private fun analyzeToken(key: String, token: String): HashSet<String> {
        mapTerminalOrEpsilon(key, token)?.let {
            return hashSetOf(it)
        }

        if (key == token)
            return hashSetOf()

        return getOrCreate(token)
    }

    private fun mapTerminalOrEpsilon(key: String, token: String) =
        if (token == EPSILON || token in grammar.terminals)
            add(key, token)
        else
            null

    private fun add(key: String, token: String): String {
        map[key]?.add(token)

        return token
    }

    private fun add(key: String, tokens: HashSet<String>): HashSet<String> {
        map[key]?.addAll(tokens)

        return tokens
    }

    override fun toString() = map.toString()

    operator fun get(key: String) = if (key in grammar.terminals)
        hashSetOf(key)
    else
        map[key] ?: error("Key $key not present in non-terminals")

    operator fun get(tokens: List<String>) = tokens.flatMap { get(it) }.toHashSet()
}