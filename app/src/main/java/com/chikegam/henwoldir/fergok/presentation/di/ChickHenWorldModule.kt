package com.chikegam.henwoldir.fergok.presentation.di

import com.chikegam.henwoldir.fergok.data.repo.ChickHenWorldRepository
import com.chikegam.henwoldir.fergok.data.shar.ChickHenWorldSharedPreference
import com.chikegam.henwoldir.fergok.data.utils.ChickHenWorldPushToken
import com.chikegam.henwoldir.fergok.data.utils.ChickHenWorldSystemService
import com.chikegam.henwoldir.fergok.domain.usecases.ChickHenWorldGetAllUseCase
import com.chikegam.henwoldir.fergok.presentation.pushhandler.ChickHenWorldPushHandler
import com.chikegam.henwoldir.fergok.presentation.ui.load.ChickHenWorldLoadViewModel
import com.chikegam.henwoldir.fergok.presentation.ui.view.ChickHenWorldViFun
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val chickHenWorldModule = module {
    factory {
        ChickHenWorldPushHandler()
    }
    single {
        ChickHenWorldRepository()
    }
    single {
        ChickHenWorldSharedPreference(get())
    }
    factory {
        ChickHenWorldPushToken()
    }
    factory {
        ChickHenWorldSystemService(get())
    }
    factory {
        ChickHenWorldGetAllUseCase(
            get(), get(), get()
        )
    }
    factory {
        ChickHenWorldViFun(get())
    }
    viewModel {
        ChickHenWorldLoadViewModel(get(), get(), get())
    }
}