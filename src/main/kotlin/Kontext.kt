import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Future
import kotlin.reflect.KClass

data class Kontext(
    private var kont: Kont<*>?,
//    private var returns: Returns<*>,
    private val previous: Kontext?,
//    val exceptionHandlers: PersistentMap<KClass<Any>, PersistentList<Any>>
) {

    fun <T> createKontinuation(
        procedure: (Kontext, Returns<T>) -> Unit
    ): Kont<T> {
        return createKont {
            Kontinuation(
                procedure = procedure,
                kontext = it
            )
        }
    }

    fun <T> createKatching(
        procedure: (Kontext, Returns<T>) -> Unit,
        handlers: Map<KClass<out Error>, (Error) -> T>,
    ): Kont<T> {
        return createKont {
            Katching(
                procedure = procedure,
                kontext = it,
                handlers = handlers
            )
        }
    }

    private fun <T> createKont(builder: (Kontext) -> Kont<T>): Kont<T> {
        val nextKontext = Kontext(null, this)

        val kont = builder.invoke(nextKontext)

        nextKontext.kont = kont

        return kont
    }

    fun chuck(error: Error): Unit {
        if (previous == null || kont == null) {
            throw IllegalStateException("Uncaught error: ${error.message}")
        }

        when (kont) {
            is Katching<*> -> {
                val errorClass = error::class
                val katching = kont as Katching<*>

                if (katching.handlers.containsKey(errorClass)) {
                    katching.handlers[errorClass]!!.invoke(error)
                } else {
                    previous.chuck(error)
                }
            }

            else -> previous.chuck(error)
        }
    }

    companion object {

        val Empty: Kontext = Kontext(null, null)

        fun <T> startContinuation(
            procedure: (Kontext, Returns<T>) -> Unit,
            returns: Returns<T>
        ): Unit {
            procedure.invoke(Kontext.Empty, returns)
        }

    }

}
