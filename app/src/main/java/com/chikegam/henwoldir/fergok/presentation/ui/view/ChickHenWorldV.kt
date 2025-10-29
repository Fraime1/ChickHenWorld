package com.chikegam.henwoldir.fergok.presentation.ui.view

import android.content.DialogInterface
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.CookieManager
import android.webkit.PermissionRequest
import android.webkit.ValueCallback
import android.widget.FrameLayout
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.chikegam.henwoldir.ChickHenWorldActivity
import com.chikegam.henwoldir.fergok.presentation.ui.load.ChickHenWorldLoadFragment
import org.koin.android.ext.android.inject

class ChickHenWorldV : Fragment(){

    private val chickHenWorldDataStore by activityViewModels<ChickHenWorldDataStore>()
    lateinit var chickHenWorldRequestFromChrome: PermissionRequest


    private val chickHenWorldViFun by inject<ChickHenWorldViFun>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CookieManager.getInstance().setAcceptCookie(true)
        requireActivity().onBackPressedDispatcher.addCallback(this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (chickHenWorldDataStore.chickHenWorldView.canGoBack()) {
                        chickHenWorldDataStore.chickHenWorldView.goBack()
                    } else if (chickHenWorldDataStore.chickHenWorldViList.size > 1) {
                        chickHenWorldDataStore.chickHenWorldViList.removeAt(chickHenWorldDataStore.chickHenWorldViList.lastIndex)
                        chickHenWorldDataStore.chickHenWorldView.destroy()
                        val previousWebView = chickHenWorldDataStore.chickHenWorldViList.last()
                        attachWebViewToContainer(previousWebView)
                        chickHenWorldDataStore.chickHenWorldView = previousWebView
                    }
                }

            })
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (chickHenWorldDataStore.chickHenWorldIsFirstCreate) {
            chickHenWorldDataStore.chickHenWorldIsFirstCreate = false
            chickHenWorldDataStore.chickHenWorldContainerView = FrameLayout(requireContext()).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                id = View.generateViewId()
            }
            return chickHenWorldDataStore.chickHenWorldContainerView
        } else {
            return chickHenWorldDataStore.chickHenWorldContainerView
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (chickHenWorldDataStore.chickHenWorldViList.isEmpty()) {
            chickHenWorldDataStore.chickHenWorldView = ChickHenWorldVi(requireContext(), object :
                ChickHenWorldCallBack {
                override fun chickHenWorldHandleCreateWebWindowRequest(chickHenWorldVi: ChickHenWorldVi) {
                    chickHenWorldDataStore.chickHenWorldViList.add(chickHenWorldVi)
                    chickHenWorldDataStore.chickHenWorldView = chickHenWorldVi
                    attachWebViewToContainer(chickHenWorldVi)
                }

                override fun chickHenWorldOnPermissionRequest(chickHenWorldRequest: PermissionRequest?) {
                    if (chickHenWorldRequest != null) {
                        chickHenWorldRequestFromChrome = chickHenWorldRequest
                    }
                    chickHenWorldRequestFromChrome.grant(chickHenWorldRequestFromChrome.resources)
                }

                override fun chickHenWorldOnShowFileChooser(chickHenWorldFilePathCallback: ValueCallback<Array<Uri>>?) {
                    (requireActivity() as ChickHenWorldActivity).chickHenWorldFilePathFromChrome = chickHenWorldFilePathCallback
                    val listItems: Array<out String> =
                        arrayOf("Select from file", "To make a photo")
                    val listener = DialogInterface.OnClickListener { _, which ->
                        when (which) {
                            0 -> {
                                (requireActivity() as ChickHenWorldActivity).chickHenWorldTakeFile.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                )
                            }
                            1 -> {
                                (requireActivity() as ChickHenWorldActivity).chickHenWorldPhoto = chickHenWorldViFun.chickHenWorldSavePhoto()
                                (requireActivity() as ChickHenWorldActivity).chickHenWorldTakePhoto.launch((requireActivity() as ChickHenWorldActivity).chickHenWorldPhoto)
                            }
                        }
                    }
                    AlertDialog.Builder(requireActivity())
                        .setTitle("Choose a method")
                        .setItems(listItems, listener)
                        .setCancelable(true)
                        .setOnCancelListener {
                            chickHenWorldFilePathCallback?.onReceiveValue(arrayOf(Uri.EMPTY))
                        }
                        .create()
                        .show()
                }

                override fun chickHenWorldOnFirstPageFinished() {
                    chickHenWorldDataStore.chickHenWorldSetIsFirstFinishPage()
                }

            }, chickHenWorldWindow = requireActivity().window)
            chickHenWorldDataStore.chickHenWorldView.chickHenWorldFLoad(arguments?.getString(ChickHenWorldLoadFragment.CHICK_HEN_WORLD_D) ?: "")
//            ejvview.fLoad("www.google.com")
            chickHenWorldDataStore.chickHenWorldViList.add(chickHenWorldDataStore.chickHenWorldView)
            attachWebViewToContainer(chickHenWorldDataStore.chickHenWorldView)
        } else {
            chickHenWorldDataStore.chickHenWorldView = chickHenWorldDataStore.chickHenWorldViList.last()
            attachWebViewToContainer(chickHenWorldDataStore.chickHenWorldView)
        }
    }

    private fun attachWebViewToContainer(w: ChickHenWorldVi) {
        chickHenWorldDataStore.chickHenWorldContainerView.post {
            (w.parent as? ViewGroup)?.removeView(w)
            chickHenWorldDataStore.chickHenWorldContainerView.removeAllViews()
            chickHenWorldDataStore.chickHenWorldContainerView.addView(w)
        }
    }




}