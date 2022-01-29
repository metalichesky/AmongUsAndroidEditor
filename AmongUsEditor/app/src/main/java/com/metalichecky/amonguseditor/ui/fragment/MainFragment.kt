package com.metalichecky.amonguseditor.ui.fragment

import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.metalichecky.amonguseditor.BuildConfig
import com.metalichecky.amonguseditor.R
import com.metalichecky.amonguseditor.adapter.LanguageAdapter
import com.metalichecky.amonguseditor.di.Injectable
import com.metalichecky.amonguseditor.model.NavigateType
import com.metalichecky.amonguseditor.model.Screen
import com.metalichecky.amonguseditor.ui.MessageDialog
import com.metalichecky.amonguseditor.util.*
import com.metalichecky.amonguseditor.vm.FirebaseMessageViewModel
import com.metalichecky.amonguseditor.vm.SettingsViewModel
import com.tbruyelle.rxpermissions3.RxPermissions
import kotlinx.android.synthetic.main.fragment_main.*
import timber.log.Timber
import javax.inject.Inject

class MainFragment : BaseFragment(), Injectable {
    override val screen: Screen? = Screen("Main")

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var settingsViewModel: SettingsViewModel
    lateinit var firebaseMessageViewModel: FirebaseMessageViewModel

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
        firebaseMessageViewModel = vmp.get(FirebaseMessageViewModel::class.java)

        firebaseMessageViewModel.needShowMessage.observe(viewLifecycleOwner, Observer {
            it?.let { message ->
                showMessage(message.title, message.text, object : MessageDialog.Listener {
                    override fun onClosed() {
                        firebaseMessageViewModel.setMessageShown(message)
                    }
                })
            }
        })

        setupViews()
    }


    private fun setupViews() {
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
            openGame()
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

    private fun openAbout() {
        findNavController().navigate(R.id.action_fragmentMain_to_aboutFragment)
    }

    private fun openGame() {
        FirebaseLogger.logNavigate(Screen("AmongUsGame"), NavigateType.OPEN)
        if (!IntentUtils.amongUsGameExists()) {
            showMessage(
                getString(R.string.title_error_game_not_found),
                getString(R.string.text_error_game_not_found)
            )
        }
        IntentUtils.openAmongUsGame()
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
        } else if (isNeedManageExternalStorage()) {
            showMessage(
                getString(R.string.title_permissions_request),
                getString(R.string.message_permissions_request),
                object : MessageDialog.Listener {
                    override fun onClosed() {
                        requestManageExternalStorage()
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

    private fun isNeedManageExternalStorage(): Boolean {
        val result = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            !Environment.isExternalStorageManager()
        } else {
            false
        }
        Timber.d("isNeedManageExternalStorage() ${result}")
        return result
    }

    private fun requestManageExternalStorage() {
        val activity = activity ?: return
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            IntentUtils.openManageAllFilesSettings(activity)
        }
    }

    private fun openPermissionSettings() {
        val activity = activity ?: return
        showMessage(
            getString(R.string.title_permissions_dont_ask_again),
            getString(R.string.message_permissions_dont_ask_again),
            object : MessageDialog.Listener {
                override fun onClosed() {
                    IntentUtils.openAppPermissionSettings(activity)
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