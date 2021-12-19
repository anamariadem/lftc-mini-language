package dto.table

enum class Operation(val value: String) {
    POP("!POP!"),
    ACCEPT("!ACCEPT!");

    companion object {
        fun from(value: String) = when (value) {
            POP.value -> POP
            ACCEPT.value -> ACCEPT
            else -> error("$value cannot be mapped to an Operation enum")
        }
    }

    override fun toString() = value
}