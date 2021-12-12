import dto.FirstFunction
import dto.FollowFunction
import dto.Grammar

class Parser(grammar: Grammar) {
    val first = FirstFunction(grammar)
    val follow = FollowFunction(grammar, first)
}