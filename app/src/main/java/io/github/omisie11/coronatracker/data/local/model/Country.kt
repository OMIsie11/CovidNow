package io.github.omisie11.coronatracker.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "countries_list_table")
data class Country(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "_id") val _id: Int? = null,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "iso2") val iso2: String?,
    @ColumnInfo(name = "iso3") val iso3: String?
)
