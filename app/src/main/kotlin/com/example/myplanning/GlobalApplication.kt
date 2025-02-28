package com.example.myplanning

import android.app.Application
import android.util.Log
import com.example.data.BuildConfig
import com.google.firebase.FirebaseApp
import com.kakao.sdk.common.KakaoSdk
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class GlobalApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        Log.d("HiltInit", "Hilt initialized in GlobalApplication")
        KakaoSdk.init(this, BuildConfig.KAKAO_API_KEY)
        Log.d("해시", KakaoSdk.keyHash)
    }
}