package com.metalichecky.amonguseditor.ui.fragment

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import com.metalichecky.amonguseditor.BuildConfig
import com.metalichecky.amonguseditor.R
import com.metalichecky.amonguseditor.adapter.LanguageAdapter
import com.metalichecky.amonguseditor.di.Injectable
import com.metalichecky.amonguseditor.ui.MessageDialog
import com.metalichecky.amonguseditor.util.*
import com.metalichecky.amonguseditor.vm.SettingsViewModel
import com.tbruyelle.rxpermissions3.Permission
import com.tbruyelle.rxpermissions3.RxPermissions
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File
import javax.inject.Inject

class MainFragment : BaseFragment(), Injectable {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var settingsViewModel: SettingsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activity = activity ?: return
        val vmp = ViewModelProvider(activity, viewModelFactory)
        settingsViewModel = vmp.get(SettingsViewModel::class.java)

        setupButtons()
    }


    private fun setupButtons() {
        tvAppName.setCustomTypeface(TypefaceUtils.TypeFaces.AMATIC_BOLD)
        btnOpenEditor.setCustomTypeface(TypefaceUtils.TypeFaces.AMATIC_BOLD)
        btnOpenGame.setCustomTypeface(TypefaceUtils.TypeFaces.AMATIC_BOLD)
        btnOpenAbout.setCustomTypeface(TypefaceUtils.TypeFaces.AMATIC_BOLD)
        tvAppVersion.setCustomTypeface(TypefaceUtils.TypeFaces.OSWALD_REGULAR)
        tvAppVersion.setText(getString(R.string.app_version, BuildConfig.VERSION_NAME))

        btnOpenEditor.setOnClickListener {
            openEditor()
        }

        btnOpenGame.setOnClickListener {
            DeeplinkUtils.openAmongUsGame()
        }

        btnOpenAbout.setOnClickListener {
            openAbout()
        }

        val languages = settingsViewModel.languages
        val position = languages.indexOfFirst {
            it.ordinal == SettingsViewModel.currentLanguage.ordinal
        }
        val languageAdapter = LanguageAdapter(languages)
        spLanguage.adapter = languageAdapter
        spLanguage.setSelection(position)
        spLanguage.onItemSelectedListener = object : OnSelectionChanged() {
            override fun onSelectionChanged(idx: Int) {
                val language = languageAdapter.languages.get(idx)
                settingsViewModel.setLanguage(language)
            }
        }
    }

    override fun onStart() {
        super.onStart()
    }

    private fun openAbout() {
        findNavController().navigate(R.id.action_fragmentMain_to_aboutFragment)
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

abstract class OnSelectionChanged : AdapterView.OnItemSelectedListener {
    var currentSelectionIdx: Int = -1
    override fun onNothingSelected(parent: AdapterView<*>?) {}

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        Timber.d("onItemSelected() ${position}")
        if (currentSelectionIdx < 0) {
            currentSelectionIdx = position
        } else if (currentSelectionIdx != position) {
            currentSelectionIdx = position
            onSelectionChanged(position)
        }
    }

    abstract fun onSelectionChanged(idx: Int)
}