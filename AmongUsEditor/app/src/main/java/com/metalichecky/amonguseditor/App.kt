package com.metalichecky.amonguseditor

import android.app.Activity
import android.app.Application
import android.app.Service
import android.graphics.Typeface
import android.util.Log
import com.google.firebase.FirebaseApp
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.crashlytics.internal.common.CrashlyticsCore
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.ktx.messaging
import com.metalichecky.amonguseditor.di.AppInjector
import com.metalichecky.amonguseditor.util.TypefaceUtils
import com.tbruyelle.rxpermissions3.BuildConfig
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import timber.log.Timber
import javax.inject.Inject

class App: Application(), HasActivityInjector {
    companion object {
        lateinit var instance: App

    }
    private val customTypefaces: MutableMap<TypefaceUtils.TypeFaces, Typeface> = mutableMapOf()


    @Inject
    lateinit var dispatchingAndroidActivityInjector: DispatchingAndroidInjector<Activity>
    override fun activityInjector() = dispatchingAndroidActivityInjector

    override fun onCreate() {
        instance = this
        super.onCreate()
        AppInjector.init(this)

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