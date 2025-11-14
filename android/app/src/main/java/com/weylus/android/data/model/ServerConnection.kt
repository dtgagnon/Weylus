package com.weylus.android.data.model

import kotlinx.serialization.Serializable

/**
 * Represents a saved server connection
 */
@Serializable
data class ServerConnection(
    val url: String,
    val name: String? = null,
    val accessCode: String? = null,
    val lastConnected: Long = 0L,
    val isFavorite: Boolean = false
)
