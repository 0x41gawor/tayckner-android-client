package pl.gawor.android.tayckner.model

data class HabitEvent(
    val comment: String,
    val date: String,
    val habit: Habit,
    val id: Int,
    val value: Int
)