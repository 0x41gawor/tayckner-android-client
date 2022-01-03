package pl.gawor.android.tayckner.common.model

data class User(
    val id: Long,
    val username: String,
    val password: String,
    val firstName: String,
    val lastname: String,
    val email: String
)