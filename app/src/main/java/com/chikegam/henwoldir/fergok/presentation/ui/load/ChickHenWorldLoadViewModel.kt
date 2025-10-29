package com.chikegam.henwoldir.fergok.presentation.ui.load

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chikegam.henwoldir.fergok.data.shar.ChickHenWorldSharedPreference
import com.chikegam.henwoldir.fergok.data.utils.ChickHenWorldSystemService
import com.chikegam.henwoldir.fergok.domain.usecases.ChickHenWorldGetAllUseCase
import com.chikegam.henwoldir.fergok.presentation.app.ChickHenWorldAppsFlyerState
import com.chikegam.henwoldir.fergok.presentation.app.ChickHenWorldApp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ChickHenWorldLoadViewModel(
    private val chickHenWorldGetAllUseCase: ChickHenWorldGetAllUseCase,
    private val chickHenWorldSharedPreference: ChickHenWorldSharedPreference,
    private val chickHenWorldSystemService: ChickHenWorldSystemService
) : ViewModel() {

    var chickHenWorldUrl = ""

    private val _chickHenWorldHomeScreenState: MutableStateFlow<ChickHenWorldHomeScreenState> =
        MutableStateFlow(ChickHenWorldHomeScreenState.ChickHenWorldLoading)
    val chickHenWorldHomeScreenState = _chickHenWorldHomeScreenState.asStateFlow()

    private var chickHenWorldGetApps = false


    init {
        viewModelScope.launch {
            when (chickHenWorldSharedPreference.chickHenWorldAppState) {
                0 -> {
                    if (chickHenWorldSystemService.chickHenWorldIsOnline()) {
                        ChickHenWorldApp.chickHenWorldConversionFlow.collect {
                            when(it) {
                                ChickHenWorldAppsFlyerState.ChickHenWorldDefault -> {}
                                ChickHenWorldAppsFlyerState.ChickHenWorldError -> {
                                    chickHenWorldSharedPreference.chickHenWorldAppState = 2
                                    _chickHenWorldHomeScreenState.value =
                                        ChickHenWorldHomeScreenState.ChickHenWorldError
                                    chickHenWorldGetApps = true
                                }
                                is ChickHenWorldAppsFlyerState.ChickHenWorldSuccess -> {
                                    if (!chickHenWorldGetApps) {
                                        chickHenWorldGetData(it.chickHenWorldData)
                                        chickHenWorldGetApps = true
                                    }
                                }
                            }
                        }
                    } else {
                        _chickHenWorldHomeScreenState.value =
                            ChickHenWorldHomeScreenState.ChickHenWorldNotInternet
                    }
                }
                1 -> {
                    if (chickHenWorldSystemService.chickHenWorldIsOnline()) {
                        if (ChickHenWorldApp.CHICK_HEN_WORLD_FB_LI != null) {
                            _chickHenWorldHomeScreenState.value =
                                ChickHenWorldHomeScreenState.ChickHenWorldSuccess(
                                    ChickHenWorldApp.CHICK_HEN_WORLD_FB_LI.toString()
                                )
                        } else if (System.currentTimeMillis() / 1000 > chickHenWorldSharedPreference.chickHenWorldExpired) {
                            Log.d(ChickHenWorldApp.CHICK_HEN_WORLD_MAIN_TAG, "Current time more then expired, repeat request")
                            ChickHenWorldApp.chickHenWorldConversionFlow.collect {
                                when(it) {
                                    ChickHenWorldAppsFlyerState.ChickHenWorldDefault -> {}
                                    ChickHenWorldAppsFlyerState.ChickHenWorldError -> {
                                        _chickHenWorldHomeScreenState.value =
                                            ChickHenWorldHomeScreenState.ChickHenWorldSuccess(
                                                chickHenWorldSharedPreference.chickHenWorldSavedUrl
                                            )
                                        chickHenWorldGetApps = true
                                    }
                                    is ChickHenWorldAppsFlyerState.ChickHenWorldSuccess -> {
                                        if (!chickHenWorldGetApps) {
                                            chickHenWorldGetData(it.chickHenWorldData)
                                            chickHenWorldGetApps = true
                                        }
                                    }
                                }
                            }
                        } else {
                            Log.d(ChickHenWorldApp.CHICK_HEN_WORLD_MAIN_TAG, "Current time less then expired, use saved url")
                            _chickHenWorldHomeScreenState.value =
                                ChickHenWorldHomeScreenState.ChickHenWorldSuccess(
                                    chickHenWorldSharedPreference.chickHenWorldSavedUrl
                                )
                        }
                    } else {
                        _chickHenWorldHomeScreenState.value =
                            ChickHenWorldHomeScreenState.ChickHenWorldNotInternet
                    }
                }
                2 -> {
                    _chickHenWorldHomeScreenState.value =
                        ChickHenWorldHomeScreenState.ChickHenWorldError
                }
            }
        }
    }


    private suspend fun chickHenWorldGetData(conversation: MutableMap<String, Any>?) {
        val chickHenWorldData = chickHenWorldGetAllUseCase.invoke(conversation)
        if (chickHenWorldSharedPreference.chickHenWorldAppState == 0) {
            if (chickHenWorldData == null) {
                chickHenWorldSharedPreference.chickHenWorldAppState = 2
                _chickHenWorldHomeScreenState.value =
                    ChickHenWorldHomeScreenState.ChickHenWorldError
            } else {
                chickHenWorldSharedPreference.chickHenWorldAppState = 1
                chickHenWorldSharedPreference.apply {
                    chickHenWorldExpired = chickHenWorldData.chickHenWorldExpires
                    chickHenWorldSavedUrl = chickHenWorldData.chickHenWorldUrl
                }
                _chickHenWorldHomeScreenState.value =
                    ChickHenWorldHomeScreenState.ChickHenWorldSuccess(chickHenWorldData.chickHenWorldUrl)
            }
        } else  {
            if (chickHenWorldData == null) {
                _chickHenWorldHomeScreenState.value =
                    ChickHenWorldHomeScreenState.ChickHenWorldSuccess(chickHenWorldSharedPreference.chickHenWorldSavedUrl)
            } else {
                chickHenWorldSharedPreference.apply {
                    chickHenWorldExpired = chickHenWorldData.chickHenWorldExpires
                    chickHenWorldSavedUrl = chickHenWorldData.chickHenWorldUrl
                }
                _chickHenWorldHomeScreenState.value =
                    ChickHenWorldHomeScreenState.ChickHenWorldSuccess(chickHenWorldData.chickHenWorldUrl)
            }
        }
    }


    sealed class ChickHenWorldHomeScreenState {
        data object ChickHenWorldLoading : ChickHenWorldHomeScreenState()
        data object ChickHenWorldError : ChickHenWorldHomeScreenState()
        data class ChickHenWorldSuccess(val data: String) : ChickHenWorldHomeScreenState()
        data object ChickHenWorldNotInternet: ChickHenWorldHomeScreenState()
    }
}