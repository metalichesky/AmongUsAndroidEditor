package com.metalichecky.amonguseditor.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.metalichecky.amonguseditor.vm.EditorViewModel
import com.metalichecky.amonguseditor.vm.FirebaseMessageViewModel
import com.metalichecky.amonguseditor.vm.SettingsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap


@Suppress("unused")
@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(SettingsViewModel::class)
    abstract fun bindSettingsViewModel(settingsViewModel: SettingsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(EditorViewModel::class)
    abstract fun bindEditorViewModel(editorViewModel: EditorViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FirebaseMessageViewModel::class)
    abstract fun bindFirebaseMessageViewModel(firebaseMessageViewModel: FirebaseMessageViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

}