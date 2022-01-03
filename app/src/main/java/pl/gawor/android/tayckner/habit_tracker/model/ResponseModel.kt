package pl.gawor.android.tayckner.habit_tracker.model

data class ResponseModel<T>(
    val code: String,
    val message: String,
    val content: T
)
