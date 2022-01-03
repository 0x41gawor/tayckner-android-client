package pl.gawor.android.tayckner.day_planner.model

import pl.gawor.android.tayckner.common.model.User

data class Schedule(
    val duration: Int,
    val endTime: String,
    val id: Int,
    val name: String,
    val startTime: String,
    val user: User
)