package com.metalichecky.amonguseditor.ui.activity

import android.content.Intent
import android.os.Bundle
import android.transition.TransitionInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.metalichecky.amonguseditor.R
import com.metalichecky.amonguseditor.di.Injectable
import com.metalichecky.amonguseditor.vm.SettingsViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


class SplashActivity : AppCompatActivity(), Injectable {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var settingsViewModel: SettingsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        settingsViewModel = ViewModelProvider(this, viewModelFactory).get(
            SettingsViewModel::class.java
        )
        settingsViewModel.updateSettings(false) {
            lifecycleScope.launch {
                openMainActivity()
                finish()
            }
        }
    }

    private fun openMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        val bundle = ActivityOptionsCompat.makeCustomAnimation(
            this,
            android.R.anim.fade_in, android.R.anim.fade_out
        ).toBundle()
        startActivity(intent, bundle)
    }
}