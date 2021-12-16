package dto

import END_TERMINAL
import EPSILON
import fromExcluding

class FollowFunction(private val grammar: Grammar, private val firstFunction: FirstFunction) {
    private val map = HashMap(grammar.nonTerminals.associateWith { hashSetOf<String>() })

    init {
        initialize()
    }

    private fun initialize() {
        getOrCreate(grammar.startingSymbol)
        grammar.nonTerminals.forEach {
            getOrCreate(it)
        }
    }

    private fun getOrCreate(key: String): HashSet<String> {
        get(key).let { it.isNotEmpty() && return it }

        return analyzeToken(key)
    }

    private fun analyzeTokenSequence(key: String, token: String, tokenSequence: List<String>): HashSet<String> {
        if (tokenSequence.isEmpty())
            return hashSetOf()

        if (tokenSequence.last() == token)
            return getOrCreate(key)

        val rightOfTokenFirst = firstFunction[tokenSequence.fromExcluding(token)]

        if (EPSILON !in rightOfTokenFirst)
            return rightOfTokenFirst

        rightOfTokenFirst.remove(EPSILON)
        return rightOfTokenFirst.apply { addAll(getOrCreate(key)) }
    }

    private fun analyzeToken(token: String): HashSet<String> {
        val followTokens = if (token == grammar.startingSymbol)
            hashSetOf(add(token, END_TERMINAL))
        else
            hashSetOf()

        grammar.productions.flatMap { (key, values) -> values.map { key to it } }
            .filter { (key, sequence) -> key != token && token in sequence }
            .forEach { (key, sequence) ->
                followTokens += add(token, analyzeTokenSequence(key, token, sequence))
            }

        return followTokens
    }

    operator fun get(key: String) = map[key] ?: error("Key $key not present in non-terminals")

    private fun add(key: String, tokens: HashSet<String>): HashSet<String> {
        map[key]?.addAll(tokens)

        return tokens
    }

    private fun add(key: String, token: String): String {
        map[key]?.add(token)

        return token
    }

    override fun toString() = map.toString()
}
