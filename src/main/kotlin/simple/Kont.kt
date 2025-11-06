package simple

interface Kont<in T> {
    fun resume(value: T): Unit
}

data class SimpleKont<in T>(
    val block: (T) -> Unit
): Kont<T> {
    override fun resume(value: T) {
        block.invoke(value)
    }
}
