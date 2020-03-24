package io.github.omisie11.coronatracker.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import io.github.omisie11.coronatracker.data.local.dao.CountriesDao
import io.github.omisie11.coronatracker.data.local.dao.GlobalSummaryDao
import io.github.omisie11.coronatracker.data.local.dao.LocalSummaryDao
import io.github.omisie11.coronatracker.data.local.model.Country
import io.github.omisie11.coronatracker.data.local.model.GlobalSummary
import io.github.omisie11.coronatracker.data.local.model.LocalSummary

@Database(entities = [GlobalSummary::class, LocalSummary::class, Country::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun globalSummaryDao(): GlobalSummaryDao
    abstract fun localSummaryDao(): LocalSummaryDao
    abstract fun countriesDao(): CountriesDao
}
