class Table {
    private val data = hashMapOf<Pair<String, String>, List<String>>()

    operator fun set(rowSymbol: String, columnSymbol: String, value: List<String>) {
        data[rowSymbol to columnSymbol] = value
    }

    fun hasKey(rowSymbol: String, columnSymbol: String) = rowSymbol to columnSymbol in data.keys

    override fun toString() = data.toString()
}