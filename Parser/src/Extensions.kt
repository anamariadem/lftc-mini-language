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

inline fun <T, R> Iterable<T>.flatMapNotNull(transform: (T) -> Iterable<R>?): List<R> {
    val destination = arrayListOf<R>()
    forEach { element -> transform(element)?.let { destination += it } }
    return destination
}