package com.github.br1992.kontinuations.asynk

import com.github.br1992.kontinuations.simple.Kont
import java.util.concurrent.ConcurrentHashMap

data class ParallelKont(
    val block: (Array<Any>) -> Unit,
    val konts: Array<Kont<Any>>
): Kont<Pair<Kont<*>, *>> {
    private val map = ConcurrentHashMap<Kont<Any>, Any>()

    override fun resume(value: Pair<Kont<*>, *>) {

    }

}
