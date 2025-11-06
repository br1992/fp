package com.github.br1992.kontinuations.exceptions

import com.github.br1992.kontinuations.simple.Kont
import kotlin.reflect.KClass

interface ExceptionalKont<T>: Kont<KontResult<T, Any>> {}

sealed interface KontResult<out T, out E>

data class Success<out T>(val value: T) : KontResult<T, Nothing>
data class Error<out E>(val error: E) : KontResult<Any, E>

data class KatchingKont<T>(
    val block: (T) -> Unit,
    val previous: ExceptionalKont<Any>,
    val handlers: Map<KClass<*>, (Any, ExceptionalKont<T>) -> Unit>
): ExceptionalKont<T> {

    override fun resume(value: KontResult<T, Any>) {
        when (value) {
            is Success<T> -> block.invoke(value.value)
            is Error<Any> -> {
                val errorHandler = handlers[value.error::class]

                if  (errorHandler != null) {
                    errorHandler.invoke(value.error, HandlerKont(
                        block,
                        previous
                    ))
                } else {
                    previous.resume(value)
                }
            }
        }
    }

}

data object UnhandledErrorKont: ExceptionalKont<Any> {
    override fun resume(value: KontResult<Any, Any>) {
        when (value) {
            is Success<*> -> return
            is Error<*> -> {
                println("Unhandled error: $value")
            }
        }
    }

}

data class HandlerKont<T>(
    val success: (T) -> Unit,
    val failure: ExceptionalKont<Any>
): ExceptionalKont<T> {
    override fun resume(value: KontResult<T, Any>) {
        when (value) {
            is Success<T> -> success.invoke(value.value)
            is Error<*> -> failure.resume(value)
        }
    }
}


