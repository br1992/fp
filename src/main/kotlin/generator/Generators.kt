package generator

import simple.SimpleKont

object Generators {

    val generator = Generator<Int> { scope ->
        fun intRange(start: Int, end: Int, scope: GeneratorScope<Int>) {
            if (start <= end) {
                scope.yield(start, SimpleKont {
                    intRange(start + 1, end, scope)
                })
            }
        }
        intRange(1, 5, scope)
    }

    val resultKont = SimpleKont<GeneratorResult<Int>> { num ->
        println(num)
    }

}

fun main() {


    repeat(9, {
        Generators.generator.next(Generators.resultKont)
    })
}
