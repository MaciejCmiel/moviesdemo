package com.demo.movies.common

import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import timber.log.Timber
import java.util.concurrent.atomic.AtomicBoolean

/**
 * A lifecycle-aware observable that sends only new updates after subscription, used for events like
 * navigation and Snackbar messages.
 * <p>
 * This avoids a common problem with events: on configuration change (like rotation) an update
 * can be emitted if the observer is active. This LiveData only calls the observable if there's an
 * explicit call to setValue() or call().
 * <p>
 * Note that only one observer is going to be notified of changes.
 *
 * source: https://gist.github.com/rozag/b0672d49e75c9dd173d55448986d2bd8
 */
@Suppress("unused")
class SingleLiveEvent<T> : MutableLiveData<T>() {

    private val pending = AtomicBoolean(false)

    fun observe(owner: LifecycleOwner, observer: (T) -> Unit) {
        observe(owner, Observer {
            if (it != null) {
                observer(it)
            }
        })
    }

    @MainThread
    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        if (hasActiveObservers()) {
            Timber.w("Multiple observers registered but only one will be notified of changes.")
        } else {
            // Observe the internal MutableLiveData
            super.observe(owner, Observer { t ->
                if (pending.compareAndSet(true, false)) {
                    observer.onChanged(t)
                }
            })
        }
    }

    override fun setValue(value: T?) {
        pending.set(true)
        super.setValue(value)
    }

    /**
     * Used for cases where T is Void, to make calls cleaner.
     */
    @MainThread
    fun call() {
        value = null
    }

    interface NonNullObserver<T> {
        /**
         * Called when the data is changed.
         * @param t  The new data
         */
        fun onChanged(t: T)
    }
}