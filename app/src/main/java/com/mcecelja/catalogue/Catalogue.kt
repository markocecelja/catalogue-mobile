package com.mcecelja.catalogue

import android.app.Application

class Catalogue : Application() {

    companion object {
        lateinit var application: Catalogue
    }

    override fun onCreate() {
        super.onCreate()
        application = this
    }
}