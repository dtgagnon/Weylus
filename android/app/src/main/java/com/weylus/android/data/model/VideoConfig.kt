package com.weylus.android.data.model

/**
 * Video configuration settings
 */
data class VideoConfig(
    val maxWidth: Int = 1920,
    val maxHeight: Int = 1080,
    val frameRate: Int = 60,
    val quality: VideoQuality = VideoQuality.HIGH,
    val lowLatencyMode: Boolean = false
)

enum class VideoQuality(val bitrate: Int) {
    LOW(5_000_000),      // 5 Mbps
    MEDIUM(10_000_000),  // 10 Mbps
    HIGH(20_000_000),    // 20 Mbps
    ULTRA(50_000_000)    // 50 Mbps
}
