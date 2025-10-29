package com.chikegam.henwoldir.fergok

import android.app.Activity
import android.graphics.Rect
import android.view.View
import android.widget.FrameLayout
import com.chikegam.henwoldir.fergok.presentation.app.ChickHenWorldApp

class ChickHenWorldGlobalLayoutUtil {

    private var chickHenWorldMChildOfContent: View? = null
    private var chickHenWorldUsableHeightPrevious = 0

    fun chickHenWorldAssistActivity(activity: Activity) {
        val content = activity.findViewById<FrameLayout>(android.R.id.content)
        chickHenWorldMChildOfContent = content.getChildAt(0)

        chickHenWorldMChildOfContent?.viewTreeObserver?.addOnGlobalLayoutListener {
            possiblyResizeChildOfContent(activity)
        }
    }

    private fun possiblyResizeChildOfContent(activity: Activity) {
        val chickHenWorldUsableHeightNow = chickHenWorldComputeUsableHeight()
        if (chickHenWorldUsableHeightNow != chickHenWorldUsableHeightPrevious) {
            val chickHenWorldUsableHeightSansKeyboard = chickHenWorldMChildOfContent?.rootView?.height ?: 0
            val chickHenWorldHeightDifference = chickHenWorldUsableHeightSansKeyboard - chickHenWorldUsableHeightNow

            if (chickHenWorldHeightDifference > (chickHenWorldUsableHeightSansKeyboard / 4)) {
                activity.window.setSoftInputMode(ChickHenWorldApp.chickHenWorldInputMode)
            } else {
                activity.window.setSoftInputMode(ChickHenWorldApp.chickHenWorldInputMode)
            }
//            mChildOfContent?.requestLayout()
            chickHenWorldUsableHeightPrevious = chickHenWorldUsableHeightNow
        }
    }

    private fun chickHenWorldComputeUsableHeight(): Int {
        val r = Rect()
        chickHenWorldMChildOfContent?.getWindowVisibleDisplayFrame(r)
        return r.bottom - r.top  // Visible height без status bar
    }
}