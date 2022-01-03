package pl.gawor.android.tayckner.habit_tracker.model

import pl.gawor.android.tayckner.common.model.User

data class Habit(
    var id: Long,
    var name: String,
    var color: String,
    var user: User?
)