package com.metalichecky.amonguseditor

import android.app.Application
import android.graphics.Typeface
import android.util.Log
import com.google.firebase.FirebaseApp
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.crashlytics.internal.common.CrashlyticsCore
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.ktx.messaging
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
//        FirebaseApp.initializeApp(this)
        Firebase.crashlytics.setCrashlyticsCollectionEnabled(true)//!BuildConfig.DEBUG)
//        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
//        }
        printFirebaseToken()
    }

    private fun printFirebaseToken() {
        Firebase.messaging.token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Timber.d("Fetching FCM registration token failed ${task.exception}")
            } else {
                // Get new FCM registration token
                val token = task.result
                Timber.d("Fetching FCM registration token completed, token: ${token}")
            }
        }
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