package com.metalichecky.amonguseditor.model

import com.metalichecky.amonguseditor.R

data class ProgressData(
    val visible: Boolean,
    val percentage: Float? = null,
    val message: ProgressMessage? = null) {

}

enum class ProgressMessage(val stringResId: Int) {
    EDITOR_UPDATING_PREFS(R.string.editor_updating_game_prefs)


}