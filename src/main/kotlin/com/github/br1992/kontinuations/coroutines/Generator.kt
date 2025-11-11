package com.github.br1992.coroutines

fun main() {
    val seq = sequence {
        (1..5).forEach {
            println("yield $it")
            yield(it)
        }
    }

    seq.iterator().forEach {
        println("consume $it")
    }
}
