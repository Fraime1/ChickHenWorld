package com.chikegam.henwoldir.fergok.presentation.ui.view


import android.net.Uri
import android.webkit.PermissionRequest
import android.webkit.ValueCallback

interface ChickHenWorldCallBack {
    fun chickHenWorldHandleCreateWebWindowRequest(chickHenWorldVi: ChickHenWorldVi)

    fun chickHenWorldOnPermissionRequest(chickHenWorldRequest: PermissionRequest?)

    fun chickHenWorldOnShowFileChooser(chickHenWorldFilePathCallback: ValueCallback<Array<Uri>>?)

    fun chickHenWorldOnFirstPageFinished()
}