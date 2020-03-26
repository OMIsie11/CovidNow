package io.github.omisie11.coronatracker.data.local.dao

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import io.github.omisie11.coronatracker.data.local.AppDatabase
import io.github.omisie11.coronatracker.data.local.model.GlobalSummary
import io.github.omisie11.coronatracker.testGlobalSummary
import java.util.concurrent.Executors
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class GlobalSummaryDaoTest {

    private lateinit var database: AppDatabase
    private lateinit var globalSummaryDao: GlobalSummaryDao

    private val testDispatcher = TestCoroutineDispatcher()

    private val testGlobalData = testGlobalSummary

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
        globalSummaryDao = database.globalSummaryDao()
    }

    @After
    fun cleanup() {
        database.close()

        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun testInsertAndGetSummary() = runBlocking {
        globalSummaryDao.insert(testGlobalData)

        val result = globalSummaryDao.getGlobalSummaryFlow().take(1).toList()[0]

        assertThat(result.confirmed, equalTo(testGlobalData.confirmed))
        assertThat(result.recovered, equalTo(testGlobalData.recovered))
        assertThat(result.deaths, equalTo(testGlobalData.deaths))
        assertThat(result.imageUrl, equalTo(testGlobalData.imageUrl))
        assertThat(result.lastUpdate, equalTo(testGlobalData.lastUpdate))
    }

    @Test
    fun testReplaceSummary() = runBlocking {
        // Replace 2 times to check if there will be no duplicated data
        globalSummaryDao.replace(testGlobalData)
        globalSummaryDao.replace(testGlobalData)

        val result: GlobalSummary = globalSummaryDao.getGlobalSummaryFlow().take(1).toList()[0]
        assertThat(result.confirmed, equalTo(testGlobalData.confirmed))
        assertThat(result.recovered, equalTo(testGlobalData.recovered))
        assertThat(result.deaths, equalTo(testGlobalData.deaths))
        assertThat(result.imageUrl, equalTo(testGlobalData.imageUrl))
        assertThat(result.lastUpdate, equalTo(testGlobalData.lastUpdate))
    }

    @Test
    fun testDeleteCountries() = runBlocking {
        globalSummaryDao.replace(testGlobalData)
        globalSummaryDao.delete()

        val result = globalSummaryDao.getGlobalSummaryFlow().take(1).toList()
        assert(result.isEmpty())
    }
}
