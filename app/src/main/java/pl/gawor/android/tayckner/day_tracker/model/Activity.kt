package pl.gawor.android.tayckner.day_tracker.model

import java.time.LocalDate

data class Activity(
    val id: Int,
    val name: String,
    val startTime: String,
    val endTime: String,
    val date: LocalDate,
    val duration: Int,
    val breaks: Int,
    val category: Category
)