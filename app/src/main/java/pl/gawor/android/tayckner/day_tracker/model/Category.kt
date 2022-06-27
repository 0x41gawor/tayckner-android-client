package pl.gawor.android.tayckner.day_tracker.model

import pl.gawor.android.tayckner.common.model.User

data class Category(
    val id: Int,
    val name: String,
    val description: String,
    val color: String,
    val user: User?
)