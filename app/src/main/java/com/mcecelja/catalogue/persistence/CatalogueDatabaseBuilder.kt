package com.mcecelja.catalogue.persistence

import androidx.room.Room
import com.mcecelja.catalogue.Catalogue

object CatalogueDatabaseBuilder {

    private var instance: CatalogueDatabase? = null

    fun getInstance(): CatalogueDatabase {
        synchronized(CatalogueDatabase::class) {
            if (instance == null) {
                instance = buildDatabase()
            }
        }
        return instance!!
    }

    private fun buildDatabase(): CatalogueDatabase {
        return Room.databaseBuilder(
            Catalogue.application, CatalogueDatabase::class.java, CatalogueDatabase.NAME
        )
            .allowMainThreadQueries()
            .build()
    }
}