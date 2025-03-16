package com.crypto.interview.wallet.common.state

sealed class UiState<T> {

    val isLoading: Boolean
        get() = this is Loading
    val isSuccess: Boolean
        get() = this is Success
    val isFailure: Boolean
        get() = this is Failure
    val isInitial: Boolean
        get() = this is Initial

    abstract val fallback: T?

    companion object {
        fun <T> initial(): UiState<T> = Initial()

        fun <T> loading(canceler: (() -> Unit)? = null, fallback: T? = null): UiState<T> =
                Loading(canceler, fallback)

        fun <T> success(value: T): UiState<T> = Success(value)

        fun <T> failure(
                throwable: Throwable?,
                retry: (() -> Unit)? = null,
                fallback: T? = null
        ): UiState<T> = Failure(throwable, fallback, retry)
    }
}

class Initial<T> : UiState<T>() {
    override val fallback: T? = null
}

class Loading<T>(val canceler: (() -> Unit)? = null, override val fallback: T? = null) :
        UiState<T>() {

    fun cancel() {
        canceler?.invoke()
    }
}

data class Success<T>(val value: T) : UiState<T>() {
    override val fallback: T? = null
}

data class Failure<T>(
        val throwable: Throwable?,
        override val fallback: T? = null,
        val retry: (() -> Unit)? = null
) : UiState<T>()

val <T> UiState<T>.value: T?
    get() =
            when (this) {
                is Success -> value
                else -> null
            }

val <T> UiState<T>.valueOrFallback: T?
    get() {
        val currentValue = value
        if (currentValue == null) {
            return fallback
        }
        return currentValue
    }
