package pl.gawor.android.tayckner.model

data class HabitEvent(
    val color: String,
    val comment: String,
    val date: String,
    val id: Int,
    val name: String,
    val user: User,
    val value: Int
)