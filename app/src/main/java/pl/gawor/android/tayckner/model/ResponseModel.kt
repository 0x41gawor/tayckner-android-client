package pl.gawor.android.tayckner.model

data class ResponseModel<T>(val code: String, val message: String, val content: T)
