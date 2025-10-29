package com.chikegam.henwoldir.fergok.data.utils

import android.util.Log
import com.chikegam.henwoldir.fergok.presentation.app.ChickHenWorldApp
import com.google.firebase.messaging.FirebaseMessaging
import java.lang.Exception
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class ChickHenWorldPushToken {

    suspend fun chickHenWorldGetToken(): String = suspendCoroutine { continuation ->
        try {
            FirebaseMessaging.getInstance().token.addOnCompleteListener {
                if (!it.isSuccessful) {
                    continuation.resume(it.result)
                    Log.d(ChickHenWorldApp.CHICK_HEN_WORLD_MAIN_TAG, "Token error: ${it.exception}")
                } else {
                    continuation.resume(it.result)
                }
            }
        } catch (e: Exception) {
            Log.d(ChickHenWorldApp.CHICK_HEN_WORLD_MAIN_TAG, "FirebaseMessagingPushToken = null")
            continuation.resume("")
        }
    }


}