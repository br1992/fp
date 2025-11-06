import kotlin.reflect.KClass

typealias Returns<T> = (T) -> Unit

sealed interface Kont<T> {
    val procedure: (Kontext, Returns<T>) -> Unit
//    val returns: Returns<T>
    val kontext: Kontext
}

data class Kontinuation<T>(
    override val procedure: (Kontext, Returns<T>) -> Unit,
//    override val returns: Returns<T>,
    override val kontext: Kontext,
//    private var kontext: Kontext,
//    val previousKontext: Kontext?
): Kont<T> {


//    fun kontext(): Kontext {
//        return kontext
//    }

}

data class Katching<T>(
    override val procedure: (Kontext, Returns<T>) -> Unit,
//    override val returns: Returns<T>,
    override val kontext: Kontext,
    val handlers: Map<KClass<out Error>, (Error) -> T>,
): Kont<T>

interface KKont<T> {
    val resume: (value: T) -> Unit
}
