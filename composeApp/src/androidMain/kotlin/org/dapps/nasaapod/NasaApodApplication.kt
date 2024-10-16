package org.dapps.nasaapod

import android.app.Application
import org.dapps.nasaapod.di.initKoin
import org.koin.android.ext.koin.androidContext

class NasaApodApplication: Application(){
    override fun onCreate() {
        initKoin {
            androidContext(this@NasaApodApplication)
        }
        super.onCreate()
    }
}