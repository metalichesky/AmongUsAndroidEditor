package com.metalichecky.amonguseditor.di

import com.metalichecky.amonguseditor.ui.activity.SplashActivity
import dagger.Module
import dagger.android.AndroidInjectionModule

@Module(includes = [
    AndroidInjectionModule::class,
    MainActivityModule::class,
    SplashActivityModule::class,
    ViewModelModule::class])
class AppModule {

}
