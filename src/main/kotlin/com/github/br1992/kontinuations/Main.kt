package com.github.br1992

import java.math.BigInteger
import kotlin.time.DurationUnit
import kotlin.time.ExperimentalTime
import kotlin.time.measureTimedValue

@OptIn(ExperimentalTime::class)
fun main(args: Array<String>) {
    val index = 100000
    val warmUpTimes = 5
    warmUpTimes.downTo(1).forEach {
        println("Warm up: ${it}")
        contFunc(index)
    }
    val contFuncResult = measureTimedValue {
        contFunc(index)
    }
    println("Calculated fib($index)=${contFuncResult.value} using continuations in ${contFuncResult.duration.toDouble(DurationUnit.SECONDS)}s")

    warmUpTimes.downTo(1).forEach {
        println("Warm up: ${it}")
        deepRecur(index)
    }
    val deepRecurResult = measureTimedValue {
        deepRecur(index)
    }
    println("Calculated fib($index)=${deepRecurResult.value} using deep recur in ${deepRecurResult.duration.toDouble(DurationUnit.SECONDS)}s")
}

fun dumbContFunc(index: Int): BigInteger {
    return Continuations.ContFunc<Int, BigInteger> {
        if (it <= 1) {
            BigInteger.ONE
        } else {
            callWithCont(it - 1) + callWithCont(it - 2)
        }
    }.execute(index)
}

fun dumbDeepRecur(index: Int): BigInteger {
    return DeepRecursiveFunction<Int, BigInteger> {
        if (it <= 1) {
            BigInteger.ONE
        } else {
            this.callRecursive(it - 1) + this.callRecursive(it - 2)
        }
    }.invoke(index)
}

fun contFunc(index: Int): BigInteger {
    return Continuations.ContFunc<Triple<Int, BigInteger, BigInteger>, BigInteger> {
        if (it.first < 1) it.second
        else {
            callWithCont(Triple(it.first - 1, it.third, it.second + it.third))
        }
    }.execute(Triple(index, BigInteger.ZERO, BigInteger.ONE))
}

fun deepRecur(index: Int): BigInteger {
    return DeepRecursiveFunction<Triple<Int, BigInteger, BigInteger>, BigInteger> {
        if (it.first < 1) it.second
        else {
            callRecursive(Triple(it.first - 1, it.third, it.second + it.third))
        }
    }.invoke(Triple(index, BigInteger.ZERO, BigInteger.ONE))
}

suspend fun generator() {
    val gen = Continuations.generator {
        while (true) {
            yield(1)
        }
    }

    while (true) {
        println(gen())
    }
}
