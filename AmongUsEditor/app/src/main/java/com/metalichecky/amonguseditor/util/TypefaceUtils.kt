package com.metalichecky.amonguseditor.util

import android.content.Context
import android.graphics.Typeface
import android.widget.Button
import android.widget.TextView
import com.metalichecky.amonguseditor.App


fun TextView.setCustomTypeface(fontType: TypefaceUtils.TypeFaces) {
    this.typeface = TypefaceUtils.getTypeface(fontType)
}

fun Button.setCustomTypeface(fontType: TypefaceUtils.TypeFaces) {
    this.typeface = TypefaceUtils.getTypeface(fontType)
}

class TypefaceUtils {
    enum class TypeFaces(val assetPath: String) {
        SKINNY("fonts/the_skinny.otf"),
        SKINNY_BOLD("fonts/the_skinny_bold.otf"),
        IN_YOUR_FACE("fonts/in_your_face.ttf"),
        OSWALD_BOLD("fonts/oswald_bold.ttf"),
        OSWALD_REGULAR("fonts/oswald_regular.ttf"),
        OSWALD_MEDIUM("fonts/oswald_medium.ttf"),
        DEFAULT("")
    }

    companion object {
        fun getTypeface(fontType: TypeFaces): Typeface {
            // here you can load the Typeface from asset or use default ones
            return App.instance.getCustomTypeface(fontType)
        }

        fun loadTypeface(fontType: TypeFaces, context: Context): Typeface {
            return if (fontType.assetPath.isNotEmpty()) {
                Typeface.createFromAsset(context.assets, fontType.assetPath)
            } else {
                Typeface.DEFAULT
            }
        }
    }
}