package com.metalichecky.amonguseditor.di

import com.metalichecky.amonguseditor.ui.activity.MainActivity
import com.metalichecky.amonguseditor.ui.activity.SplashActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class SplashActivityModule {
    @ContributesAndroidInjector(modules = [])
    abstract fun contributeSplashActivity(): SplashActivity
}

