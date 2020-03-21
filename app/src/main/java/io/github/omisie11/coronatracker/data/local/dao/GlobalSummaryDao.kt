package io.github.omisie11.coronatracker.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import io.github.omisie11.coronatracker.data.model.GlobalSummary
import kotlinx.coroutines.flow.Flow

@Dao
interface GlobalSummaryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(data: GlobalSummary)

    @Transaction
    fun replace(data: GlobalSummary) {
        delete()
        insert(data)
    }

    @Query("select * from global_summary_table limit 1")
    fun getGlobalSummaryFlow(): Flow<GlobalSummary>

    @Query("delete from global_summary_table")
    fun delete()
}
