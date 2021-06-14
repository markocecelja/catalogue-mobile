package com.mcecelja.catalogue.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "product")
data class Product(
    @PrimaryKey(autoGenerate = true) val id: Long,
    var name: String
) : Serializable