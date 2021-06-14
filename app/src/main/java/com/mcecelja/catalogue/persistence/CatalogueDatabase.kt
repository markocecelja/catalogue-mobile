package com.mcecelja.catalogue.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mcecelja.catalogue.model.Organization
import com.mcecelja.catalogue.model.OrganizationProduct
import com.mcecelja.catalogue.model.Product

@Database(entities = [Organization::class, Product::class, OrganizationProduct::class], version = 1)
abstract class CatalogueDatabase : RoomDatabase() {

    abstract fun catalogue(): CatalogueDao

    companion object {
        const val NAME = "catalogueDb"
    }
}