package com.github.br1992.generator

import com.github.br1992.simple.SimpleKont

object Generators {

    val generator = Generator<Int> { scope ->
        fun intRange(start: Int, end: Int, scope: GeneratorScope<Int>) {
            if (start <= end) {
                println("Yielding: $start")
                scope.yield(start, SimpleKont {
                    intRange(start + 1, end, scope)
                })
            }
        }
        intRange(1, 5, scope)
    }

    val resultKont = SimpleKont<GeneratorResult<Int>> { num ->
        println("Consuming $num")
    }

}

fun main() {
    repeat(9, {
        Generators.generator.next(Generators.resultKont)
    })
}
