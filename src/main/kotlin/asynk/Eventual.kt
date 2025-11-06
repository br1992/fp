package asynk

import simple.SimpleKont

interface Eventual<T> {

    fun resolve(value: T)

    fun value(): T

    fun isFinished(): Boolean

    fun await(engine: AsynkEngine, kont: SimpleKont<T>)

    companion object {
        enum class Status {
            IN_PROGRESS,
            FINISHED
        }
    }

}

class SimpleEventual<T>(
    private val engine: AsynkEngine
) : Eventual<T> {

    private var value: T? = null
    private var status: Eventual.Companion.Status = Eventual.Companion.Status.IN_PROGRESS

    override fun resolve(value: T) {
        this.value = value
        status = Eventual.Companion.Status.FINISHED
        engine.resolve(this)
    }

    override fun value(): T {
        return value!!
    }

    override fun isFinished(): Boolean {
        return status == Eventual.Companion.Status.FINISHED
    }

    override fun await(engine: AsynkEngine, kont: SimpleKont<T>) {
        engine.await(this, kont)
    }

}
