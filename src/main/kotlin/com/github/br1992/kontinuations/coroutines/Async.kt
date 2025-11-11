package com.github.br1992.coroutines

import com.github.br1992.coroutines.Parallelism.longThing1
import com.github.br1992.coroutines.Parallelism.longThing2
import com.github.br1992.coroutines.Parallelism.longThingFail
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withContext
import kotlin.reflect.jvm.javaMethod

object Parallelism {

    suspend fun longThing1() {
        println("started long thing 1")
        delay(5000)
        println("finished long thing 1")
    }

    suspend fun longThing2(): Int {
        println("started long thing 2")
        delay(3000)
        println("finished long thing 2")

        return 2
    }

    suspend fun longThingFail() {
        println("started long thing fail")
        delay(500)
        throw IllegalStateException("Not again!")
    }

}

suspend fun main() {
    Parallelism::longThing1.javaMethod
    val suspendMethod = Parallelism::longThing1.javaMethod!!
    println("${suspendMethod.name} has params (${suspendMethod.parameterTypes.joinToString { it.name }})")

    val scope = CoroutineScope(Dispatchers.IO)
    withContext(scope.coroutineContext) {
        supervisorScope {
            launch { longThing1() }
            val expectedInt = async { longThing2() }
            launch { longThingFail() }

            println("received: ${expectedInt.await()}")
        }
//        launch { longThing1() }
//        val expectedInt = async { longThing2() }
//        launch { longThingFail() }
//
//        println("received: ${expectedInt.await()}")
    }

    println("completed execution")
}
