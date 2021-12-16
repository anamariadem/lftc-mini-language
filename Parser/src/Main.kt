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
//    println(parser.first)
//    println(parser.follow)
//    println(parser.table)

    val pifSequence = parseProgramInternalForm()
    println(pifSequence)
//    val result = parser.evaluate("a a b")
    val result = parser.evaluate(pifSequence)
    Tree(grammar, result).print()
//    Tree(grammar, result).print()
}

fun parseProgramInternalForm() = arrayListOf<String>().apply {
    readFileIndexed("assets/PIF.out") { _, line ->
        val endIndex = line.indexOf("', ")

        add(line.substring(2, endIndex))
    }
}