package com.runt9.namelessAnomalies.util.ext

fun <T : Any> List<T>.removeIf(predicate: (T) -> Boolean): List<T> {
    val mutList = toMutableList()
    mutList.removeIf(predicate)
    return mutList.toList()
}
