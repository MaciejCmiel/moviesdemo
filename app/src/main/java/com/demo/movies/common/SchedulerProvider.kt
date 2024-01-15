package com.demo.movies.common

import io.reactivex.Scheduler

interface SchedulerProvider {
    fun uiThread(): Scheduler
    fun backgroundThread(): Scheduler
}