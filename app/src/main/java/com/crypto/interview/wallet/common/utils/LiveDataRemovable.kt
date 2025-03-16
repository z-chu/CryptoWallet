package com.crypto.interview.wallet.common.utils

import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

private class RemovableImpl<T>(
    private val liveData: LiveData<T>,
    private val observer: Observer<T>
) : Removable {

    override fun removeObserver() {
        liveData.removeObserver(observer)
    }

}

/**
 * 新的 watch 方法返回 Removable 接口，使您可以有效地删除观察者。
 */
@MainThread
fun <T> LiveData<T>.watch(owner: LifecycleOwner, observer: Observer<T>): Removable {
    val liveDataRemovable = RemovableImpl(this, observer)
    this.observe(owner, observer)
    return liveDataRemovable
}

/**
 * 新的 watch 方法返回 Removable 接口，使您可以有效地删除观察者。
 */
@MainThread
inline fun <T> LiveData<T>.watch(
    owner: LifecycleOwner,
    crossinline onChanged: (T) -> Unit
): Removable {
    val wrappedObserver = Observer<T> { t -> onChanged.invoke(t) }
    return watch(owner, wrappedObserver)
}


/**
 * 新的 watchForever 方法返回 Removable 接口，使您可以有效地删除观察者。
 */
@MainThread
fun <T> LiveData<T>.watchForever(observer: Observer<T>): Removable {
    val liveDataRemovable = RemovableImpl(this, observer)
    this.observeForever(observer)
    return liveDataRemovable
}

/**
 * 新的 watchForever 方法返回 Removable 接口，使您可以有效地删除观察者。
 */
@MainThread
inline fun <T> LiveData<T>.watchForever(crossinline onChanged: (T) -> Unit): Removable {
    val wrappedObserver = Observer<T> { t -> onChanged.invoke(t) }
    return watchForever(wrappedObserver)
}


