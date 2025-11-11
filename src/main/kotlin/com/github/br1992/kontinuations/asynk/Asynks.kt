package com.github.br1992.asynk

import com.github.br1992.simple.Kont
import com.github.br1992.simple.SimpleKont
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledThreadPoolExecutor
import java.util.concurrent.TimeUnit
import kotlin.random.Random
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
object Asynks {

    val executor = Executors.newScheduledThreadPool(4).also {
        (it as ScheduledThreadPoolExecutor).removeOnCancelPolicy = true
    }
    val engine = AsynkEngine(Executors.newWorkStealingPool())

    fun getDateButSlow(kont: Kont<Instant>) {
        val eventual = SimpleEventual<Instant>(engine)

        executor.schedule({
            eventual.resolve(Clock.System.now())
            },
            5L,
            TimeUnit.SECONDS
        )

        engine.await(eventual, kont)
    }

    fun getIntButSlow(kont: Kont<Int>) {
        val eventual = SimpleEventual<Int>(engine)

        executor.schedule({
            eventual.resolve(Random.nextInt())
        },
            7L,
            TimeUnit.SECONDS
        )

        engine.await(eventual, kont)
    }

}

@OptIn(ExperimentalTime::class)
fun main() {
    println("Starting Asynk")

    val skopeEventual = AsynkSkope.withScope(Asynks.engine) {
        val slowDate = launch { k ->
            println("Fetching slow date")
            Asynks.getDateButSlow(SimpleKont { v ->
                println("Got slow date: $v")

                k.resume(v)
            })
        }

        val slowInt = launch { k ->
            println("Fetching slow int")
            Asynks.getIntButSlow(SimpleKont { v ->
                println("Got slow int: $v")

                k.resume(v)
            })
        }
    }

    Asynks.engine.await(skopeEventual, SimpleKont {
        println("Finished execution")
        Asynks.executor.shutdown()
    })

    println("Ending Asynk")
}
