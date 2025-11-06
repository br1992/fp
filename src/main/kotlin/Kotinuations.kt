object Kotinuations {

    fun test() {
        println("Start")
        Kontext.startContinuation<Int>(
            { kontext1, returns0 ->
            println("Start First level")

            kontext1.createKontinuation<Int> { kontext2, returns1 ->
                println("Start Second level")

                kontext2.createKontinuation<Int> { _, returns2 ->
                    println("Start Third level")
                    returns2(3)
                }.procedure(kontext2, {
                    println("End Third level")

                    returns1(it - 1)
                })

                returns0(1)
            }.procedure(kontext1, {
                println("End Second level")
            })
        }, {
            println("End First level")
        })
        println("End")


    }


}

fun main() {
    Kotinuations.test()
}
