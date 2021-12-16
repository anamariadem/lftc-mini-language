import dto.Grammar
import dto.output.Tree

fun main() {
    val grammar = Grammar()

    grammar.readFromFile("assets/g2.txt")
//    println(grammar)

    grammar.validateStartingSymbol()
    grammar.validateProductions()

//    println(grammar.getProductions("program"))
//
//    println(grammar.isContextFree())

    val parser = Parser(grammar)
    println(parser.first)
    println(parser.follow)
//    println(parser.table)
//    val result = parser.evaluate("a a b")
//    Tree(grammar, result).print()
//    Tree(grammar, result).print()
}
