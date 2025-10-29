package com.chikegam.henwoldir.fergok.presentation.ui.view

import android.annotation.SuppressLint
import android.widget.FrameLayout
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class ChickHenWorldDataStore : ViewModel(){
    val chickHenWorldViList: MutableList<ChickHenWorldVi> = mutableListOf()
    private val _chickHenWorldIsFirstFinishPage: MutableStateFlow<Boolean> = MutableStateFlow(true)
    var chickHenWorldIsFirstCreate = true
    @SuppressLint("StaticFieldLeak")
    lateinit var chickHenWorldContainerView: FrameLayout
    @SuppressLint("StaticFieldLeak")
    lateinit var chickHenWorldView: ChickHenWorldVi
    
    fun chickHenWorldSetIsFirstFinishPage() {
        _chickHenWorldIsFirstFinishPage.value = false
    }
}