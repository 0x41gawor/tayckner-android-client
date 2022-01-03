package pl.gawor.android.tayckner.habit_tracker.model

data class Habit(
    var id: Long,
    var name: String,
    var color: String,
    var user: User?
)