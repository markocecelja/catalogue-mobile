package com.mcecelja.catalogue

import android.app.Application
import com.mcecelja.catalogue.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class Catalogue : Application() {

    companion object {
        lateinit var application: Catalogue
    }

    override fun onCreate() {
        super.onCreate()
        application = this
        startKoin {
            androidContext(this@Catalogue)
            modules(viewModelModule)
        }
    }
}