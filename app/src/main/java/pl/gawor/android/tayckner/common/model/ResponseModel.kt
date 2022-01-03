package pl.gawor.android.tayckner.common.model

data class ResponseModel<T>(
    val code: String,
    val message: String,
    val content: T
)
