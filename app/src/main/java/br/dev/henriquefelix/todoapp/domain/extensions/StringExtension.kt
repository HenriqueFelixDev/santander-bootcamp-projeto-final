package br.dev.henriquefelix.todoapp.domain.extensions

import java.text.SimpleDateFormat
import java.util.*

fun String.toDate(pattern : String = "dd/MM/yyyy HH:mm") : Date? {
    try {
        return SimpleDateFormat(pattern).parse(this)
    } catch (e : Exception) {
        return null
    }
}