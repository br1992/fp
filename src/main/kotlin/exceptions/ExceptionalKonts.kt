package exceptions

import com.sun.net.httpserver.Authenticator
import kotlin.reflect.KClass
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

object ExceptionalKonts {

    @OptIn(ExperimentalTime::class)
    fun testUnhandledError() {
        katchingFloat.resume(Error(Clock.System.now()) as KontResult<Int, Any>)
    }

    fun testHandledStringError() {
        katchingFloat.resume(Error("Oops") as KontResult<Int, Any>)
    }

    fun testNoError() {
        katchingFloat.resume(Success(123))
    }

    val finalResultKont = resultKont<Int> {
        println("Final result: $it")
    }

    val katchingString = tryKatching(
        finalResultKont,
        Pair(String::class, { error, kont ->
            println("Caught String error: $error")
            val errorString = error as String
            kont.resume(Success(errorString.length))
        })
    )

    val katchingFloat = tryKatching(
        katchingString,
        Pair(Float::class, { error, kont ->
            println("Caught Float error: $error")
            val errorFloat = error as Float
            kont.resume(Success(errorFloat.toInt()))
        })
    )

    fun <T> tryKatching(kurrentKont: ExceptionalKont<T>, vararg handlers: Pair<KClass<*>, (Any, ExceptionalKont<T>) -> Unit>) = KatchingKont<T>(
        { kurrentKont.resume(Success(it)) },
        kurrentKont as ExceptionalKont<Any>,
        mapOf(*handlers)
    )

    fun <T> erroring(block: (T) -> Unit, kurrentKont: ExceptionalKont<T>) = KatchingKont<T>(
        { kurrentKont.resume(Success(it)) },
        kurrentKont as ExceptionalKont<Any>,
        mapOf()
    )

    fun <T> resultKont(block: (T) -> Unit) = KatchingKont(
        block,
        UnhandledErrorKont,
        mapOf()
    )

}

fun main() {
    ExceptionalKonts.testNoError()
}
