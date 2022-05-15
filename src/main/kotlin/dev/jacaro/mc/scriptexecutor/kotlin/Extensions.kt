package dev.jacaro.mc.scriptexecutor.kotlin

fun String.endsWith(array: Array<Char>): Boolean {
    for (char in array)
        if (this.endsWith(char))
            return true
    return false
}

fun <T> emptyMutableList() : MutableList<T> = ArrayList(0)