package br.dev.henriquefelix.todoapp.domain.extensions

import java.text.SimpleDateFormat
import java.util.*

fun Date.format (pattern : String = "dd/MM/yyyy HH:mm") : String {
    return SimpleDateFormat(pattern).format(this)
}