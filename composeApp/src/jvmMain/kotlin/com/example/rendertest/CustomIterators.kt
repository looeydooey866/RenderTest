package com.example.rendertest

fun<T> List<T>.forEachPair(onPair: (T, T) -> Unit){
    for (i in 0..lastIndex){
        for (j in i + 1..lastIndex){
            onPair(this[i], this[j])
        }
    }
}