package dto.table

data class TableData(
    val rowSymbol: String,
    val columnSymbol: String,
    val tokens: List<String>,
    val index: Int
)