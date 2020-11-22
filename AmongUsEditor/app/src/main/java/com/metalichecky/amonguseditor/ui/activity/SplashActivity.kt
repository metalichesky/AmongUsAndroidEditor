package com.metalichecky.amonguseditor.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.metalichecky.amonguseditor.R
import com.metalichecky.amonguseditor.di.Injectable
import com.metalichecky.amonguseditor.vm.SettingsViewModel
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

//import com.metalichecky.amonguseditor.util.SingletonViewModelFactory

class SplashActivity : AppCompatActivity(), Injectable {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

//    @Inject
//    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

//    override fun supportFragmentInjector() = dispatchingAndroidInjector

    lateinit var settingsViewModel: SettingsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        settingsViewModel = ViewModelProvider(this, viewModelFactory).get(
            SettingsViewModel::class.java
        )
        settingsViewModel.settingsUpdated.observe(this, Observer {
            if (it != null) {
                lifecycleScope.launch {
                    settingsViewModel.settingsUpdated.postValue(null)
                    delay(500)
                    openMainActivity()
                }
            }
        })
    }

    private fun openMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }


}