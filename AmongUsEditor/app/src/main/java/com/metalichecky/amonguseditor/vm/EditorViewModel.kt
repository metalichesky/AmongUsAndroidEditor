package com.metalichecky.amonguseditor.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.metalichecky.amonguseditor.model.EditorError
import com.metalichecky.amonguseditor.model.ProgressData
import com.metalichecky.amonguseditor.model.ProgressMessage
import com.metalichecky.amonguseditor.model.gameprefs.GamePrefs
import com.metalichecky.amonguseditor.model.item.Hat
import com.metalichecky.amonguseditor.model.item.Pet
import com.metalichecky.amonguseditor.model.item.Skin
import com.metalichecky.amonguseditor.repo.GamePrefsRepo
import com.metalichecky.amonguseditor.repo.ItemsRepo
import com.metalichecky.amonguseditor.util.FirebaseLogger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class EditorViewModel @Inject constructor(): ViewModel() {
    private lateinit var gamePrefs: GamePrefs

    var error = MutableLiveData<EditorError>()
    var progress = MutableLiveData<ProgressData>()
    val hats = MutableLiveData<List<Hat>>()
    val selectedHat = MutableLiveData<Hat>()
    val pets = MutableLiveData<List<Pet>>()
    val selectedPet = MutableLiveData<Pet>()
    val skins = MutableLiveData<List<Skin>>()
    val selectedSkin = MutableLiveData<Skin>()

    private var updatingJob: Job? = null

    init {
        updatePrefs()
    }

    fun updatePrefs() {
        if (updatingJob?.isActive == true) return
        updatingJob = viewModelScope.launch(Dispatchers.IO) {
            Timber.d("updatePrefs() started")
            progress.postValue(ProgressData(true, null, ProgressMessage.EDITOR_UPDATING_PREFS))
            val newGamePrefs = GamePrefsRepo.getGamePrefs()
            if (newGamePrefs == null) {
                error.postValue(EditorError.GAME_PREFS_FILE_NOT_EXISTS)
            }
            gamePrefs = newGamePrefs ?: GamePrefs()
            updateHats()
            updateSkins()
            updatePets()
            progress.postValue(ProgressData(false))
            Timber.d("updatePrefs() finished")
        }
    }

    fun savePrefs() {
        viewModelScope.launch(Dispatchers.IO) {
            GamePrefsRepo.saveGamePrefs(gamePrefs)
        }
    }

    fun checkPrefsExists(onResult: (Boolean)->Unit) {
        viewModelScope.launch {
            val result = GamePrefsRepo.isGamePrefsExists()
            onResult.invoke(result)
        }
    }

    fun updateHats() {
        val hats = ItemsRepo.getHats()
        val selectedHatId = gamePrefs.lastHat
        var selectedHat: Hat? = null
        hats.forEach {
            it.selected = it.id == selectedHatId
            selectedHat = it
        }
        this.selectedHat.postValue(selectedHat)
        this.hats.postValue(hats)
    }

    fun selectHat(hat: Hat) {
        FirebaseLogger.logSelectItem(hat)
        gamePrefs.lastHat = hat.id
        updateHats()
        savePrefs()
    }


    fun updateSkins() {
        val skins = ItemsRepo.getSkins()
        val selectedSkinId = gamePrefs.lastSkin
        var selectedSkin: Skin? = null
        skins.forEach {
            it.selected = it.id == selectedSkinId
            selectedSkin = it
        }
        this.selectedSkin.postValue(selectedSkin)
        this.skins.postValue(skins)
    }

    fun selectSkin(skin: Skin) {
        FirebaseLogger.logSelectItem(skin)
        gamePrefs.lastSkin = skin.id
        updateSkins()
        savePrefs()
    }

    fun updatePets() {
        val pets = ItemsRepo.getPets()
        val selectedPetId = gamePrefs.lastPet
        var selectedPet: Pet? = null
        pets.forEach {
            it.selected = it.id == selectedPetId
            selectedPet = it
        }
        this.selectedPet.postValue(selectedPet)
        this.pets.postValue(pets)
    }

    fun selectPet(pet: Pet) {
        FirebaseLogger.logSelectItem(pet)
        gamePrefs.lastPet = pet.id
        updatePets()
        savePrefs()
    }

    fun updateColors() {
        gamePrefs.colorConfig
    }

}