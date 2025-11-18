package com.weylus.android.util

object Constants {
    // Network
    const val DEFAULT_PORT = 1701
    const val WEBSOCKET_TIMEOUT_MS = 10_000L
    const val RECONNECT_DELAY_MS = 2_000L
    const val MAX_RECONNECT_ATTEMPTS = 5

    // Video
    const val DEFAULT_MAX_WIDTH = 1920
    const val DEFAULT_MAX_HEIGHT = 1080
    const val DEFAULT_FRAME_RATE = 60
    const val DEFAULT_BITRATE = 10_000_000 // 10 Mbps
    const val MAX_FRAME_BUFFER_SIZE = 5

    // Input
    const val PRESSURE_CURVE_POINTS = 100
    const val DEFAULT_PALM_REJECTION_SIZE_THRESHOLD = 20f // mm

    // Preferences
    const val PREF_SERVER_URL = "server_url"
    const val PREF_ACCESS_CODE = "access_code"
    const val PREF_MAX_WIDTH = "max_width"
    const val PREF_MAX_HEIGHT = "max_height"
    const val PREF_FRAME_RATE = "frame_rate"
    const val PREF_VIDEO_QUALITY = "video_quality"
    const val PREF_LOW_LATENCY_MODE = "low_latency_mode"
    const val PREF_PALM_REJECTION_ENABLED = "palm_rejection_enabled"
    const val PREF_PALM_REJECTION_SENSITIVITY = "palm_rejection_sensitivity"
    const val PREF_SHOW_PERFORMANCE = "show_performance"

    // Service
    const val NOTIFICATION_CHANNEL_ID = "weylus_connection"
    const val NOTIFICATION_ID = 1001
    const val ACTION_DISCONNECT = "com.weylus.android.ACTION_DISCONNECT"

    // Client info
    const val CLIENT_NAME = "Weylus Android"
}
