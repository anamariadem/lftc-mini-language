import dto.Grammar

fun main() {
    val grammar = Grammar()

    grammar.readFromFile("assets/g4.txt")
//    println(grammar)
//
//    grammar.validateStartingSymbol()
//    grammar.validateProductions()
//
//    println(grammar.getProductions("S"))
//
//    println(grammar.isContextFree())
    val parser = Parser(grammar)
    println(parser.first)
    println(parser.follow)
    println(parser.table)
    println(parser.evaluate("a a b"))
}