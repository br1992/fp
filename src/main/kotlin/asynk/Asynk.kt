package asynk

import simple.Kont
import java.util.concurrent.ConcurrentHashMap

data class ParallelKont(
    val block: (Array<Any>) -> Unit,
    val konts: Array<Kont<Any>>
): Kont<Pair<Kont<*>, *>> {
    private val map = ConcurrentHashMap<Kont<Any>, Any>()

    override fun resume(value: Pair<Kont<*>, *>) {

    }

}
