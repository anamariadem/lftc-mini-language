import dto.Grammar
import dto.output.Tree

fun main() {
//    solveFirst()
    solveSecond()
}

fun solveFirst() {
    val grammar = Grammar()

    grammar.readFromFile("assets/input/g1.txt")

    grammar.validateStartingSymbol()
    grammar.validateProductions()

    val parser = Parser(grammar)
    val sequence = parseSequence()
    val evaluationResult = parser.evaluate(sequence)

    Tree(grammar, evaluationResult).writeTo("assets/output/out1.txt")
}

fun solveSecond() {
    val grammar = Grammar()

    grammar.readFromFile("assets/input/g2.txt")

    grammar.validateStartingSymbol()
    grammar.validateProductions()

    val parser = Parser(grammar)
    val sequence = parseProgramInternalForm()
    val evaluationResult = parser.evaluate(sequence)

    Tree(grammar, evaluationResult).writeTo("assets/output/out2.txt")
}

fun parseProgramInternalForm() = arrayListOf<String>().apply {
    readFileIndexed("assets/input/PIF.out") { _, line ->
        val endIndex = line.indexOf("', ")

        add(line.substring(2, endIndex))
    }
}

fun parseSequence() = arrayListOf<String>().apply {
    readFileIndexed("assets/input/seq.txt") { _, line ->
        addAll(line.split(" "))
    }
}