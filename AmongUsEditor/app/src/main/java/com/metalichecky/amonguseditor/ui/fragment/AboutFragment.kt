package com.metalichecky.amonguseditor.ui.fragment

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import com.metalichecky.amonguseditor.BuildConfig
import com.metalichecky.amonguseditor.R
import com.metalichecky.amonguseditor.ui.MessageDialog
import com.metalichecky.amonguseditor.util.*
import com.tbruyelle.rxpermissions3.Permission
import com.tbruyelle.rxpermissions3.RxPermissions
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File

class AboutFragment : BaseFragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_about, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupButtons()
    }


    private fun setupButtons() {
//        tvAbout.setCustomTypeface(TypefaceUtils.TypeFaces.OSWALD_REGULAR)
//
//        btnCancel.setOnClickListener {
//            openEditor()
//        }
    }

    override fun onStart() {
        super.onStart()
    }

    private fun openEditor() {
        if (isNeedRequestPermissions()) {
            showMessage(
                getString(R.string.title_permissions_request),
                getString(R.string.message_permissions_request),
                object : MessageDialog.Listener {
                    override fun onClosed() {
                        requestNeededPermissions()
                    }
                }
            )
        } else {
            findNavController().navigate(R.id.action_fragmentMain_to_fragmentEditor)
        }
    }

    private fun getNeededPermissions(): List<String> {
        val permissions = RxPermissions(this)
        val nonGrantedPermissions = Constants.neededPermissions.filter {
            !permissions.isGranted(it)
        }
        return nonGrantedPermissions
    }

    private fun isNeedRequestPermissions(): Boolean {
        return getNeededPermissions().isNotEmpty()
    }

    private fun requestNeededPermissions() {
        val permissions = RxPermissions(this)
        val nonGrantedPermissions = getNeededPermissions()

        if (nonGrantedPermissions.isNotEmpty()) {
            permissions.requestEachCombined(*(nonGrantedPermissions.toTypedArray()))
                .subscribe { permission ->
                    if (permission.granted) {
                        findNavController().navigate(R.id.action_fragmentMain_to_fragmentEditor)
                    } else if (!permission.shouldShowRequestPermissionRationale) {
                        openPermissionSettings()
                    } else {
                        showMessage(
                            getString(R.string.title_permissions_denied),
                            getString(R.string.message_permissions_denied)
                        )
                    }
                }
        }
    }

    private fun openPermissionSettings() {
        showMessage(
            getString(R.string.title_permissions_dont_ask_again),
            getString(R.string.message_permissions_dont_ask_again),
            object : MessageDialog.Listener {
                override fun onClosed() {
                    DeeplinkUtils.openAppPermissionSettings()
                }
            }
        )
    }
}