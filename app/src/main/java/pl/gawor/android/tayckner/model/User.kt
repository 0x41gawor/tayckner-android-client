package pl.gawor.android.tayckner.model

data class User(
    val id: Long,
    val username: String,
    val password: String,
    val firstName: String,
    val lastname: String,
    val email: String
)