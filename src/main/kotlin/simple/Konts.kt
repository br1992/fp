package simple

object Konts {

    fun test() {
        println("Start")

        SimpleKont<Int> { level1 ->
            println("""Start level $level1""")

            SimpleKont<Int> { level2 ->
                println("""Start level $level2""")

                SimpleKont<Int> { level3 ->
                    println("""Start level $level3""")
                    println("End level $level3")
                }.resume(level2 + 1)

                println("End level $level2")
            }.resume(level1 + 1)

            println("""End level $level1""")
        }.resume(1)

        println("End")
    }


}

fun main() {
    Konts.test()
}
