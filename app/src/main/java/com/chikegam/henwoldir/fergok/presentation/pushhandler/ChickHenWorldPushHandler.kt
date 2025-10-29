package com.chikegam.henwoldir.fergok.presentation.pushhandler

import android.os.Bundle
import android.util.Log
import com.chikegam.henwoldir.fergok.presentation.app.ChickHenWorldApp

class ChickHenWorldPushHandler() {
    fun chickHenWorldHandlePush(extras: Bundle?) {
        Log.d(ChickHenWorldApp.CHICK_HEN_WORLD_MAIN_TAG, "Extras from Push = ${extras?.keySet()}")
        if (extras != null) {
            val map = chickHenWorldBundleToMap(extras)
            Log.d(ChickHenWorldApp.CHICK_HEN_WORLD_MAIN_TAG, "Map from Push = $map")
            map?.let {
                if (map.containsKey("url")) {
                    ChickHenWorldApp.CHICK_HEN_WORLD_FB_LI = map["url"]
                    Log.d(ChickHenWorldApp.CHICK_HEN_WORLD_MAIN_TAG, "UrlFromActivity = $map")
                }
            }
        } else {
            Log.d(ChickHenWorldApp.CHICK_HEN_WORLD_MAIN_TAG, "Push data no!")
        }
    }

    private fun chickHenWorldBundleToMap(extras: Bundle): Map<String, String?>? {
        val map: MutableMap<String, String?> = HashMap()
        val ks = extras.keySet()
        val iterator: Iterator<String> = ks.iterator()
        while (iterator.hasNext()) {
            val key = iterator.next()
            map[key] = extras.getString(key)
        }
        return map
    }

}