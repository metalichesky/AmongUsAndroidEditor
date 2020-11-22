package com.metalichecky.amonguseditor.di

import com.metalichecky.amonguseditor.ui.fragment.EditorFragment
import com.metalichecky.amonguseditor.ui.fragment.MainFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class MainActivityFragmentsModule {
    @ContributesAndroidInjector
    abstract fun contributeMainFragment(): MainFragment
    @ContributesAndroidInjector
    abstract fun contributeEditorFragment(): EditorFragment
}
