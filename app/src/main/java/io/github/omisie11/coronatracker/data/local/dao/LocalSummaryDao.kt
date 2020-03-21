package io.github.omisie11.coronatracker.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import io.github.omisie11.coronatracker.data.local.model.LocalSummary
import kotlinx.coroutines.flow.Flow

@Dao
interface LocalSummaryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(data: LocalSummary)

    @Transaction
    fun replace(data: LocalSummary) {
        delete()
        insert(data)
    }

    @Query("select * from local_summary_table limit 1")
    fun getLocalSummaryFlow(): Flow<LocalSummary>

    @Query("delete from local_summary_table")
    fun delete()
}
