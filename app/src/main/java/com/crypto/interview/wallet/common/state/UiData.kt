package com.crypto.interview.wallet.common.state

data class UiData<R, U>(val raw: R, val uiModel: U) {
    companion object {

        fun <R, U> from(raw: R, transform: (R) -> U): UiData<R, U> {
            return UiData(raw = raw, uiModel = transform(raw))
        }
    }

    fun <NewU> transformUiModel(transform: (U) -> NewU): UiData<R, NewU> {
        return UiData(raw = raw, uiModel = transform(uiModel))
    }

    fun <T> map(transform: (R, U) -> T): T {
        return transform(raw, uiModel)
    }
}

fun <R, U> List<UiData<R, U>>.raws(): List<R> {
    return map { it.raw }
}

fun <R, U> List<UiData<R, U>>.uiModels(): List<U> {
    return map { it.uiModel }
}
