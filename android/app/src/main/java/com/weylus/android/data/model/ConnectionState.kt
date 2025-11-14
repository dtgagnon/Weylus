package com.weylus.android.data.model

/**
 * Represents the state of the WebSocket connection
 */
sealed class ConnectionState {
    data object Disconnected : ConnectionState()
    data object Connecting : ConnectionState()
    data class Connected(val serverUrl: String) : ConnectionState()
    data object Disconnecting : ConnectionState()
    data class Error(val message: String, val throwable: Throwable? = null) : ConnectionState()

    val isConnected: Boolean
        get() = this is Connected

    val isConnecting: Boolean
        get() = this is Connecting

    val isDisconnected: Boolean
        get() = this is Disconnected
}
