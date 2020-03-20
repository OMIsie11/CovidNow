package io.github.omisie11.coronatracker.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import io.github.omisie11.coronatracker.data.local.dao.GlobalSummaryDao
import io.github.omisie11.coronatracker.data.model.GlobalSummary

@Database(entities = [GlobalSummary::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun globalSummaryDao(): GlobalSummaryDao
}
