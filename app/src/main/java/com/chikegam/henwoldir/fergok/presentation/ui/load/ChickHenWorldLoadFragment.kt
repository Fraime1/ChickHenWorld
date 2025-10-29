package com.chikegam.henwoldir.fergok.presentation.ui.load

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.chikegam.henwoldir.MainActivity
import com.chikegam.henwoldir.R
import com.chikegam.henwoldir.databinding.FragmentLoadChickHenWorldBinding
import com.chikegam.henwoldir.fergok.data.shar.ChickHenWorldSharedPreference
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class ChickHenWorldLoadFragment : Fragment(R.layout.fragment_load_chick_hen_world) {
    private lateinit var chickHenWorldLoadBinding: FragmentLoadChickHenWorldBinding

    private val chickHenWorldLoadViewModel by viewModel<ChickHenWorldLoadViewModel>()

    private val chickHenWorldSharedPreference by inject<ChickHenWorldSharedPreference>()
    

    private val chickHenWorldRequestNotificationPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            chickHenWorldNavigateToSuccess(chickHenWorldLoadViewModel.chickHenWorldUrl)
        } else {
            if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                chickHenWorldSharedPreference.chickHenWorldNotificationRequest =
                    (System.currentTimeMillis() / 1000) + 259200
                chickHenWorldNavigateToSuccess(chickHenWorldLoadViewModel.chickHenWorldUrl)
            } else {
                chickHenWorldNavigateToSuccess(chickHenWorldLoadViewModel.chickHenWorldUrl)
            }
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        chickHenWorldLoadBinding = FragmentLoadChickHenWorldBinding.bind(view)

        chickHenWorldLoadBinding.chickHenWorldGrandButton.setOnClickListener {
            val chickHenWorldPermission = Manifest.permission.POST_NOTIFICATIONS
            chickHenWorldRequestNotificationPermission.launch(chickHenWorldPermission)
            chickHenWorldSharedPreference.chickHenWorldNotificationRequestedBefore = true
        }

        chickHenWorldLoadBinding.chickHenWorldSkipButton.setOnClickListener {
            chickHenWorldSharedPreference.chickHenWorldNotificationRequest =
                (System.currentTimeMillis() / 1000) + 259200
            chickHenWorldNavigateToSuccess(chickHenWorldLoadViewModel.chickHenWorldUrl)
        }

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                chickHenWorldLoadViewModel.chickHenWorldHomeScreenState.collect {
                    when (it) {
                        is ChickHenWorldLoadViewModel.ChickHenWorldHomeScreenState.ChickHenWorldLoading -> {

                        }

                        is ChickHenWorldLoadViewModel.ChickHenWorldHomeScreenState.ChickHenWorldError -> {
                            requireActivity().startActivity(
                                Intent(
                                    requireContext(),
                                    MainActivity::class.java
                                )
                            )
                            requireActivity().finish()
                        }

                        is ChickHenWorldLoadViewModel.ChickHenWorldHomeScreenState.ChickHenWorldSuccess -> {
                            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.S_V2) {
                                val chickHenWorldPermission = Manifest.permission.POST_NOTIFICATIONS
                                val chickHenWorldPermissionRequestedBefore = chickHenWorldSharedPreference.chickHenWorldNotificationRequestedBefore

                                if (ContextCompat.checkSelfPermission(requireContext(), chickHenWorldPermission) == PackageManager.PERMISSION_GRANTED) {
                                    chickHenWorldNavigateToSuccess(it.data)
                                } else if (!chickHenWorldPermissionRequestedBefore && (System.currentTimeMillis() / 1000 > chickHenWorldSharedPreference.chickHenWorldNotificationRequest)) {
                                    // первый раз — показываем UI для запроса
                                    chickHenWorldLoadBinding.chickHenWorldNotiGroup.visibility = View.VISIBLE
                                    chickHenWorldLoadBinding.chickHenWorldLoadingGroup.visibility = View.GONE
                                    chickHenWorldLoadViewModel.chickHenWorldUrl = it.data
                                } else if (shouldShowRequestPermissionRationale(chickHenWorldPermission)) {
                                    // временный отказ — через 3 дня можно показать
                                    if (System.currentTimeMillis() / 1000 > chickHenWorldSharedPreference.chickHenWorldNotificationRequest) {
                                        chickHenWorldLoadBinding.chickHenWorldNotiGroup.visibility = View.VISIBLE
                                        chickHenWorldLoadBinding.chickHenWorldLoadingGroup.visibility = View.GONE
                                        chickHenWorldLoadViewModel.chickHenWorldUrl = it.data
                                    } else {
                                        chickHenWorldNavigateToSuccess(it.data)
                                    }
                                } else {
                                    // навсегда отклонено — просто пропускаем
                                    chickHenWorldNavigateToSuccess(it.data)
                                }
                            } else {
                                chickHenWorldNavigateToSuccess(it.data)
                            }
                        }

                        ChickHenWorldLoadViewModel.ChickHenWorldHomeScreenState.ChickHenWorldNotInternet -> {
                            chickHenWorldLoadBinding.chickHenWorldLoadConnectionStateText.visibility = View.VISIBLE
                            chickHenWorldLoadBinding.chickHenWorldLoadingGroup.visibility = View.GONE
                        }
                    }
                }
            }
        }
    }


    private fun chickHenWorldNavigateToSuccess(data: String) {
        findNavController().navigate(
            R.id.action_chickHenWorldLoadFragment_to_chickHenWorldV,
            bundleOf(CHICK_HEN_WORLD_D to data)
        )
    }

    companion object {
        const val CHICK_HEN_WORLD_D = "chickHenWorldData"
    }
}