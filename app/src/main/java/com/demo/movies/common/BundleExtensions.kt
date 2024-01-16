package com.demo.movies.common

import android.os.Build
import android.os.Bundle
import android.os.Parcelable

fun <T : Parcelable> Bundle.getParcelableCompat(key: String, javaClass: Class<out T>): Parcelable? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getParcelable(key, javaClass)
    } else {
        getParcelable<T>(key)
    }
}