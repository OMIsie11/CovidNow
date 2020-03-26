package io.github.omisie11.coronatracker.data.repository

import android.content.SharedPreferences
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import io.github.omisie11.coronatracker.data.local.dao.GlobalSummaryDao
import io.github.omisie11.coronatracker.data.local.model.GlobalSummary
import io.github.omisie11.coronatracker.data.remote.ApiService
import io.github.omisie11.coronatracker.utils.testGlobalSummary
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class GlobalSummaryRepositoryTest {

    private val testGlobalSummaryLocal = testGlobalSummary

    private val testDispatcher = TestCoroutineDispatcher()

    @Mock
    private lateinit var apiService: ApiService

    @Mock
    private lateinit var globalSummaryDao: GlobalSummaryDao

    @Mock
    private lateinit var sharedPrefs: SharedPreferences

    // Class under test
    private lateinit var globalSummaryRepository: GlobalSummaryRepository

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        MockitoAnnotations.initMocks(this)

        globalSummaryRepository = GlobalSummaryRepository(apiService, globalSummaryDao, sharedPrefs)
    }

    @After
    fun cleanup() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun getCountriesNamesFlowTest() = kotlinx.coroutines.runBlocking {
        val globalSummaryFlow = flowOf(testGlobalSummaryLocal)

        Mockito.`when`(globalSummaryDao.getGlobalSummaryFlow()).thenAnswer {
            return@thenAnswer globalSummaryFlow
        }

        val result: GlobalSummary =
            globalSummaryRepository.getGlobalSummaryFlow().take(1).toList()[0]

        verify(globalSummaryDao, times(1)).getGlobalSummaryFlow()
        assertEquals(result, testGlobalSummaryLocal)
    }
}
