package pl.gawor.android.tayckner.day_tracker.model

data class Activity(
    val breaks: Int,
    val duration: Int,
    val endTime: String,
    val id: Int,
    val name: String,
    val startTime: String,
    val category: Category
)