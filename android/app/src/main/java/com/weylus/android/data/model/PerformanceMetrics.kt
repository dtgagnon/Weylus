package com.weylus.android.data.model

/**
 * Performance metrics for monitoring video streaming
 */
data class PerformanceMetrics(
    val fps: Int = 0,
    val latencyMs: Long = 0,
    val bitrateMbps: Float = 0f,
    val droppedFrames: Int = 0,
    val totalFrames: Int = 0
) {
    val dropRate: Float
        get() = if (totalFrames > 0) {
            (droppedFrames.toFloat() / totalFrames) * 100f
        } else {
            0f
        }
}
