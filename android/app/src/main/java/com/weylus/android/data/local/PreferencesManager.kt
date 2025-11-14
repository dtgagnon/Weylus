package com.weylus.android.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.weylus.android.data.model.VideoQuality
import com.weylus.android.util.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferencesManager @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    // Preference keys
    private object PreferencesKeys {
        val SERVER_URL = stringPreferencesKey(Constants.PREF_SERVER_URL)
        val ACCESS_CODE = stringPreferencesKey(Constants.PREF_ACCESS_CODE)
        val MAX_WIDTH = intPreferencesKey(Constants.PREF_MAX_WIDTH)
        val MAX_HEIGHT = intPreferencesKey(Constants.PREF_MAX_HEIGHT)
        val FRAME_RATE = intPreferencesKey(Constants.PREF_FRAME_RATE)
        val VIDEO_QUALITY = stringPreferencesKey(Constants.PREF_VIDEO_QUALITY)
        val LOW_LATENCY_MODE = booleanPreferencesKey(Constants.PREF_LOW_LATENCY_MODE)
        val PALM_REJECTION_ENABLED = booleanPreferencesKey(Constants.PREF_PALM_REJECTION_ENABLED)
        val PALM_REJECTION_SENSITIVITY = intPreferencesKey(Constants.PREF_PALM_REJECTION_SENSITIVITY)
        val SHOW_PERFORMANCE = booleanPreferencesKey(Constants.PREF_SHOW_PERFORMANCE)
    }

    // Server settings
    val serverUrl: Flow<String?> = dataStore.data.map { it[PreferencesKeys.SERVER_URL] }
    val accessCode: Flow<String?> = dataStore.data.map { it[PreferencesKeys.ACCESS_CODE] }

    suspend fun saveServerUrl(url: String) {
        dataStore.edit { it[PreferencesKeys.SERVER_URL] = url }
    }

    suspend fun saveAccessCode(code: String?) {
        dataStore.edit {
            if (code != null) {
                it[PreferencesKeys.ACCESS_CODE] = code
            } else {
                it.remove(PreferencesKeys.ACCESS_CODE)
            }
        }
    }

    // Video settings
    val maxWidth: Flow<Int> = dataStore.data.map {
        it[PreferencesKeys.MAX_WIDTH] ?: Constants.DEFAULT_MAX_WIDTH
    }

    val maxHeight: Flow<Int> = dataStore.data.map {
        it[PreferencesKeys.MAX_HEIGHT] ?: Constants.DEFAULT_MAX_HEIGHT
    }

    val frameRate: Flow<Int> = dataStore.data.map {
        it[PreferencesKeys.FRAME_RATE] ?: Constants.DEFAULT_FRAME_RATE
    }

    val videoQuality: Flow<VideoQuality> = dataStore.data.map {
        val qualityString = it[PreferencesKeys.VIDEO_QUALITY] ?: VideoQuality.HIGH.name
        try {
            VideoQuality.valueOf(qualityString)
        } catch (e: IllegalArgumentException) {
            VideoQuality.HIGH
        }
    }

    val lowLatencyMode: Flow<Boolean> = dataStore.data.map {
        it[PreferencesKeys.LOW_LATENCY_MODE] ?: false
    }

    suspend fun saveVideoSettings(
        maxWidth: Int,
        maxHeight: Int,
        frameRate: Int,
        quality: VideoQuality,
        lowLatencyMode: Boolean
    ) {
        dataStore.edit { prefs ->
            prefs[PreferencesKeys.MAX_WIDTH] = maxWidth
            prefs[PreferencesKeys.MAX_HEIGHT] = maxHeight
            prefs[PreferencesKeys.FRAME_RATE] = frameRate
            prefs[PreferencesKeys.VIDEO_QUALITY] = quality.name
            prefs[PreferencesKeys.LOW_LATENCY_MODE] = lowLatencyMode
        }
    }

    // Input settings
    val palmRejectionEnabled: Flow<Boolean> = dataStore.data.map {
        it[PreferencesKeys.PALM_REJECTION_ENABLED] ?: true
    }

    val palmRejectionSensitivity: Flow<Int> = dataStore.data.map {
        it[PreferencesKeys.PALM_REJECTION_SENSITIVITY] ?: 50
    }

    suspend fun savePalmRejectionSettings(enabled: Boolean, sensitivity: Int) {
        dataStore.edit { prefs ->
            prefs[PreferencesKeys.PALM_REJECTION_ENABLED] = enabled
            prefs[PreferencesKeys.PALM_REJECTION_SENSITIVITY] = sensitivity
        }
    }

    // Display settings
    val showPerformance: Flow<Boolean> = dataStore.data.map {
        it[PreferencesKeys.SHOW_PERFORMANCE] ?: false
    }

    suspend fun setShowPerformance(show: Boolean) {
        dataStore.edit { it[PreferencesKeys.SHOW_PERFORMANCE] = show }
    }

    // Clear all preferences
    suspend fun clearAll() {
        dataStore.edit { it.clear() }
    }
}
