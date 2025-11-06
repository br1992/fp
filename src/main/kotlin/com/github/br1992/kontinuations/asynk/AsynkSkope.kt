package com.github.br1992.kontinuations.asynk

import com.github.br1992.kontinuations.simple.Kont
import com.github.br1992.kontinuations.simple.SimpleKont
import java.util.concurrent.ConcurrentHashMap

data class AsynkSkope(
    val engine: AsynkEngine
) {

//    private val kontsStatuses = mutableMapOf<SimpleKont<*>, Any?>()
//    private val outstandingKonts = 0
    private val eventuals = ConcurrentHashMap<Eventual<*>, Unit>()

    fun <T> launch(builder: (Kont<T>) -> Unit): Eventual<T> {
        val eventual = SimpleEventual<T>(engine)

        val eventualKont = SimpleKont<T> {
            eventual.resolve(it)
        }

        engine.executorService.submit {
            builder.invoke(eventualKont)
        }

        eventuals.put(eventual, Unit)
        return eventual
    }

    internal fun skopeKont(eventual: Eventual<Unit>) = SimpleKont<Eventual<*>> {
        var shouldResolve = false

        synchronized(eventuals) {
            eventuals.remove(it)

            if (eventuals.isEmpty()) {
                shouldResolve = true
            }
        }

        if (shouldResolve) eventual.resolve(Unit)
    }

    companion object {

        fun withScope(engine: AsynkEngine, builder: AsynkSkope.() -> Unit): Eventual<Unit> {
            val skope = AsynkSkope(engine)
            val eventual = SimpleEventual<Unit>(engine)
            val skopeKont = skope.skopeKont(eventual)

            builder.invoke(skope)

            skope.eventuals.keys.forEach { e ->
                val helperKont = SimpleKont<Any> { _ ->
                    skopeKont.resume(e)
                }
                engine.await(e as Eventual<Any>, helperKont)
            }

            return eventual
        }

    }

}
