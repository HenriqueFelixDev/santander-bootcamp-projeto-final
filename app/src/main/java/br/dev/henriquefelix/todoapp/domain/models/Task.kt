package br.dev.henriquefelix.todoapp.domain.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.time.LocalDateTime
import java.util.*

@Parcelize
data class Task (
    var id : Long,
    val task : String,
    val start : Date?,
    val end : Date?,
    val isCompleted : Boolean
) : Parcelable