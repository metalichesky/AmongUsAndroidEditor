package com.metalichecky.amonguseditor

import android.app.Application
import android.graphics.Typeface
import com.metalichecky.amonguseditor.util.TypefaceUtils
import com.tbruyelle.rxpermissions3.BuildConfig
import timber.log.Timber

class App: Application() {
    companion object {
        lateinit var instance: App

    }

    private val customTypefaces: MutableMap<TypefaceUtils.TypeFaces, Typeface> = mutableMapOf()

    override fun onCreate() {
        instance = this
        super.onCreate()
//        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
//        }
    }


    fun getCustomTypeface(customTypeface: TypefaceUtils.TypeFaces): Typeface {
        var typeface = customTypefaces.get(customTypeface)
        if (typeface == null) {
            typeface = TypefaceUtils.loadTypeface(customTypeface, this)
            customTypefaces.put(customTypeface, typeface)
        }
        return typeface
    }
}