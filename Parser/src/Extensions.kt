import java.io.File
import java.util.*

fun readFileIndexed(
    fileName: String,
    operationBlock: (index: Int, line: String) -> Unit,
) = with(Scanner(File(fileName))) {
    var index = 0
    while (hasNextLine())
        operationBlock(index++, nextLine())
    close()
}

inline fun <T> Iterable<T>.once(predicate: (T) -> Boolean) = this.count(predicate) == 1

fun <T> List<T>.fromExcluding(element: T) = drop(indexOf(element) + 1)