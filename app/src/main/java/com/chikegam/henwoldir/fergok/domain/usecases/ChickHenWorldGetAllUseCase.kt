package com.chikegam.henwoldir.fergok.domain.usecases

import android.util.Log
import com.chikegam.henwoldir.fergok.data.repo.ChickHenWorldRepository
import com.chikegam.henwoldir.fergok.data.utils.ChickHenWorldPushToken
import com.chikegam.henwoldir.fergok.data.utils.ChickHenWorldSystemService
import com.chikegam.henwoldir.fergok.domain.model.ChickHenWorldEntity
import com.chikegam.henwoldir.fergok.domain.model.ChickHenWorldParam
import com.chikegam.henwoldir.fergok.presentation.app.ChickHenWorldApp

class ChickHenWorldGetAllUseCase(
    private val chickHenWorldRepository: ChickHenWorldRepository,
    private val chickHenWorldSystemService: ChickHenWorldSystemService,
    private val chickHenWorldPushToken: ChickHenWorldPushToken,
) {
    suspend operator fun invoke(conversion: MutableMap<String, Any>?) : ChickHenWorldEntity?{
        val params = ChickHenWorldParam(
            chickHenWorldLocale = chickHenWorldSystemService.chickHenWorldGetLocale(),
            chickHenWorldPushToken = chickHenWorldPushToken.chickHenWorldGetToken(),
            chickHenWorldAfId = chickHenWorldSystemService.chickHenWorldGetAppsflyerId()
        )
//        chickHenWorldSystemService.bubblePasswrodGetGaid()
        Log.d(ChickHenWorldApp.CHICK_HEN_WORLD_MAIN_TAG, "Params for request: $params")
        return chickHenWorldRepository.chickHenWorldGetClient(params, conversion)
    }



}