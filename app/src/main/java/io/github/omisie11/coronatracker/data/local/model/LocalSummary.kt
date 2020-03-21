package io.github.omisie11.coronatracker.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "local_summary_table")
data class LocalSummary(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "_id") val _id: Int? = null,
    @ColumnInfo(name = "confirmed") val confirmed: Int?,
    @ColumnInfo(name = "recovered") val recovered: Int?,
    @ColumnInfo(name = "deaths") val deaths: Int?,
    @ColumnInfo(name = "lastUpdate") val lastUpdate: String
)
