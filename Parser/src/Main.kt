fun main() {
    val grammar = Grammar()

    grammar.readFromFile("assets/g1.txt")
    println(grammar)

    grammar.validateStartingSymbol()
    grammar.validateProductions()

    println(grammar.getProductions("S"))

    println(grammar.isContextFree())
}