package com.chikegam.henwoldir.fergok.data.shar

import android.content.Context
import androidx.core.content.edit

class ChickHenWorldSharedPreference(context: Context) {
    private val chickHenWorldPrefs = context.getSharedPreferences("chickHenWorldSharedPrefsAb", Context.MODE_PRIVATE)

    var chickHenWorldSavedUrl: String
        get() = chickHenWorldPrefs.getString(CHICK_HEN_WORLD_SAVED_URL, "") ?: ""
        set(value) = chickHenWorldPrefs.edit { putString(CHICK_HEN_WORLD_SAVED_URL, value) }

    var chickHenWorldExpired : Long
        get() = chickHenWorldPrefs.getLong(CHICK_HEN_WORLD_EXPIRED, 0L)
        set(value) = chickHenWorldPrefs.edit { putLong(CHICK_HEN_WORLD_EXPIRED, value) }

    var chickHenWorldAppState: Int
        get() = chickHenWorldPrefs.getInt(CHICK_HEN_WORLD_APPLICATION_STATE, 0)
        set(value) = chickHenWorldPrefs.edit { putInt(CHICK_HEN_WORLD_APPLICATION_STATE, value) }

    var chickHenWorldNotificationRequest: Long
        get() = chickHenWorldPrefs.getLong(CHICK_HEN_WORLD_NOTIFICAITON_REQUEST, 0L)
        set(value) = chickHenWorldPrefs.edit { putLong(CHICK_HEN_WORLD_NOTIFICAITON_REQUEST, value) }

    var chickHenWorldNotificationRequestedBefore: Boolean
        get() = chickHenWorldPrefs.getBoolean(CHICK_HEN_WORLD_NOTIFICATION_REQUEST_BEFORE, false)
        set(value) = chickHenWorldPrefs.edit { putBoolean(
            CHICK_HEN_WORLD_NOTIFICATION_REQUEST_BEFORE, value) }

    companion object {
        private const val CHICK_HEN_WORLD_SAVED_URL = "chickHenWorldSavedUrl"
        private const val CHICK_HEN_WORLD_EXPIRED = "chickHenWorldExpired"
        private const val CHICK_HEN_WORLD_APPLICATION_STATE = "chickHenWorldApplicationState"
        private const val CHICK_HEN_WORLD_NOTIFICAITON_REQUEST = "chickHenWorldNotificationRequest"
        private const val CHICK_HEN_WORLD_NOTIFICATION_REQUEST_BEFORE = "chickHenWorldNotificationRequestedBefore"
    }
}