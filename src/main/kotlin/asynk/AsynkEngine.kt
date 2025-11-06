package asynk

import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import simple.Kont
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ExecutorService
import java.util.concurrent.Future

data class AsynkEngine(
    val executorService: ExecutorService
) {
    private val awaitList = ConcurrentHashMap<Eventual<*>, PersistentList<Kont<*>>>()

    fun <T> fromFuture(future: Future<T>): Eventual<T> {
        return SimpleEventual<T>(this).also { se ->
            executorService.submit {
                val value = future.get()
                se.resolve(value)
            }
        }
    }

    fun <T> await(awaitee: Eventual<T>, kont: Kont<T>) {
        if (awaitee.isFinished()) {
            kont.resume(awaitee.value())
        }

        awaitList.compute(awaitee, { _, ks ->
            if (ks != null) {
                ks.add(kont)
            } else {
                persistentListOf(kont)
            }
        })
    }

    internal fun <T> resolve(eventual: Eventual<T>) {
        val konts = awaitList.remove(eventual)

        konts?.forEach { k ->
            executorService.submit { (k as Kont<Any>).resume(eventual.value() as Any) }
        }
    }

}
