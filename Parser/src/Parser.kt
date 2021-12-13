import dto.FirstFunction
import dto.FollowFunction
import dto.Grammar
import dto.table.Operation
import dto.table.Table

class Parser(private val grammar: Grammar) {
    val first = FirstFunction(grammar)
    val follow = FollowFunction(grammar, first)
    val table = Table(grammar, first, follow)

    fun evaluate(sequence: String): List<Int> {
        val inputStack = ArrayDeque("$sequence $END_TERMINAL".split(" "))
        val workingStack = ArrayDeque(listOf(grammar.startingSymbol, END_TERMINAL))
        val outputStack = ArrayDeque<Int>()

        while (inputStack.size > 0 || workingStack.size > 0) {
//            logStack(inputStack, workingStack, outputStack)

            val inputStackFirst = inputStack.first()
            val workingStackFirst = workingStack.first()

            if (tryExecuteAccept(inputStackFirst, workingStackFirst))
                break

            if(tryExecutePop(inputStack, workingStack))
                continue

            expandWorkingStack(inputStackFirst, workingStack, outputStack)
        }

        return outputStack.toList()
    }

    private fun tryExecuteAccept(inputStackFirst: String, workingStackFirst: String): Boolean {
        if (inputStackFirst != END_TERMINAL || workingStackFirst != END_TERMINAL)
            return false

        val (tokens) = table[workingStackFirst, inputStackFirst]
        val operation = Operation.from(tokens.first())

        return operation == Operation.ACCEPT
    }

    private fun tryExecutePop(inputStack: ArrayDeque<String>, workingStack: ArrayDeque<String>): Boolean {
        val inputStackFirst = inputStack.first()
        val workingStackFirst = workingStack.first()

        if (inputStackFirst != workingStackFirst)
            return false

        val (tokens) = table[workingStackFirst, inputStackFirst]
        val operation = Operation.from(tokens.first())

        if (operation != Operation.POP)
            return false

        inputStack.removeFirst()
        workingStack.removeFirst()
        return true
    }

    private fun expandWorkingStack(
        inputStackFirst: String,
        workingStack: ArrayDeque<String>,
        outputStack: ArrayDeque<Int>
    ) {
        val workingStackFirst = workingStack.first()
        val (tokens, index) = table[workingStackFirst, inputStackFirst]

        workingStack.removeFirst()
        workingStack.addAll(0, tokens.filter { it != EPSILON })
        outputStack.addLast(index)
    }

    private fun logStack(
        inputStack: ArrayDeque<String>,
        workingStack: ArrayDeque<String>,
        outputStack: ArrayDeque<Int>
    ) {
        println()
        println("Input: $inputStack")
        println("Working: $workingStack")
        println("Output: $outputStack")
    }
}