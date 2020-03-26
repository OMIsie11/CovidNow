package io.github.omisie11.coronatracker.data.local.dao

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import io.github.omisie11.coronatracker.data.local.AppDatabase
import io.github.omisie11.coronatracker.data.local.model.LocalSummary
import io.github.omisie11.coronatracker.testLocalSummary
import java.util.concurrent.Executors
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.hamcrest.CoreMatchers
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class LocalSummaryTest {

    private lateinit var database: AppDatabase
    private lateinit var localSummaryDao: LocalSummaryDao

    private val testDispatcher = TestCoroutineDispatcher()

    private val testLocalData = testLocalSummary

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        val context = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room
            .inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .setTransactionExecutor(Executors.newSingleThreadExecutor())
            .build()
        localSummaryDao = database.localSummaryDao()
    }

    @After
    fun cleanup() {
        database.close()

        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun testInsertAndGetSummary() = runBlocking {
        localSummaryDao.insert(testLocalData)

        val result = localSummaryDao.getLocalSummaryFlow().take(1).toList()[0]

        assertThat(result.confirmed, CoreMatchers.equalTo(testLocalData.confirmed))
        assertThat(result.recovered, CoreMatchers.equalTo(testLocalData.recovered))
        assertThat(result.deaths, CoreMatchers.equalTo(testLocalData.deaths))
        assertThat(result.lastUpdate, CoreMatchers.equalTo(testLocalData.lastUpdate))
    }

    @Test
    fun testReplaceSummary() = runBlocking {
        // Replace 2 times to check if there will be no duplicated data
        localSummaryDao.replace(testLocalData)
        localSummaryDao.replace(testLocalData)

        val result: LocalSummary = localSummaryDao.getLocalSummaryFlow().take(1).toList()[0]
        assertThat(result.confirmed, CoreMatchers.equalTo(testLocalData.confirmed))
        assertThat(result.recovered, CoreMatchers.equalTo(testLocalData.recovered))
        assertThat(result.deaths, CoreMatchers.equalTo(testLocalData.deaths))
        assertThat(result.lastUpdate, CoreMatchers.equalTo(testLocalData.lastUpdate))
    }

    @Test
    fun testDeleteCountries() = runBlocking {
        localSummaryDao.replace(testLocalData)
        localSummaryDao.delete()

        val result = localSummaryDao.getLocalSummaryFlow().take(1).toList()
        assert(result.isEmpty())
    }
}
