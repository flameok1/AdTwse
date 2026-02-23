package com.example.adtwse.utils

fun String?.safeToDouble(): Double {
    return this?.replace(",", "")?.toDoubleOrNull() ?: 0.0
}

fun String?.safeToLong(): Long {
    return this?.replace(",", "")?.toLongOrNull() ?: 0
}