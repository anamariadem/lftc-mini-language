fun main() {
    val grammar = Grammar()

    grammar.readFromFile("assets/g3.txt")
//    println(grammar)
//
//    grammar.validateStartingSymbol()
//    grammar.validateProductions()
//
//    println(grammar.getProductions("S"))
//
//    println(grammar.isContextFree())
    val parser = Parser(grammar)
    println(parser.firstMap)
    println(parser.followMap)
}