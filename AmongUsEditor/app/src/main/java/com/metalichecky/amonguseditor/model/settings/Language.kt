package com.metalichecky.amonguseditor.model.settings

import com.metalichecky.amonguseditor.R
import java.util.*

enum class Language(val imageResId: Int, val nameResId: Int, val locale: Locale) {
    RUSSIAN(R.drawable.ic_flag_russia, R.string.language_russian, Locale("ru")),
    ENGLISH(R.drawable.ic_flag_united_kingdom, R.string.language_english, Locale("en"))
}