package com.demo.movies.common

import io.reactivex.Completable
import io.reactivex.Flowable

fun Completable.setSchedulers(schedulerProvider: SchedulerProvider): Completable =
    subscribeOn(schedulerProvider.backgroundThread())
        .observeOn(schedulerProvider.uiThread())

fun <T> Flowable<T>.setSchedulers(schedulerProvider: SchedulerProvider): Flowable<T> =
    subscribeOn(schedulerProvider.backgroundThread())
        .observeOn(schedulerProvider.uiThread())