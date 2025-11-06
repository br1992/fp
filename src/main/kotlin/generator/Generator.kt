package generator

import simple.Kont

data class Generator<T>(
    val block: (GeneratorScope<T>) -> Unit
) {

    private var kurrentKont: Kont<Unit>? = null
    private lateinit var genScope: InternalGenScope<T>

    fun next(resultKont: Kont<GeneratorResult<T>>) {
        if (kurrentKont == null) {
            genScope = InternalGenScope(resultKont)
            block.invoke(genScope)
        } else {
            kurrentKont!!.resume(Unit)
        }

        if (!genScope.wasCalled) {
            resultKont.resume(End)
        }

        genScope.clearWasCalled()
    }

    inner class InternalGenScope<T>(
        val resultKont: Kont<GeneratorResult<T>>,
        var wasCalled: Boolean = false,
    ): GeneratorScope<T> {

        fun clearWasCalled() {
            wasCalled = false
        }

        override fun yield(t: T, genKont: Kont<Unit>) {
            wasCalled = true
            kurrentKont = genKont
            resultKont.resume(Result(t))
        }

    }

}

interface GeneratorScope<T> {
    fun yield(t: T, genKont: Kont<Unit>)
}

sealed interface GeneratorResult<out T>

data class Result<out T>(val value: T) : GeneratorResult<T>
object End : GeneratorResult<Nothing>
