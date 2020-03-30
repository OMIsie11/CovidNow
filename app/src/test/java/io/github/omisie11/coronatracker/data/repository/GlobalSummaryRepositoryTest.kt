package io.github.omisie11.coronatracker.data.repository

import android.content.SharedPreferences
import com.github.mikephil.charting.data.PieEntry
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import io.github.omisie11.coronatracker.data.local.dao.GlobalSummaryDao
import io.github.omisie11.coronatracker.data.local.model.GlobalSummary
import io.github.omisie11.coronatracker.data.remote.ApiService
import io.github.omisie11.coronatracker.data.remote.model.GlobalSummaryRemote
import io.github.omisie11.coronatracker.utils.testGlobalSummary
import io.github.omisie11.coronatracker.utils.testGlobalSummaryPieChartData
import io.github.omisie11.coronatracker.utils.testGlobalSummaryRemote
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import retrofit2.Response

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class GlobalSummaryRepositoryTest {

    private val testGlobalSummaryLocal = testGlobalSummary
    private val testGlobalChartData = testGlobalSummaryPieChartData

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
    fun getGlobalSummaryFlow() = runBlocking {
        val globalSummaryFlow = flowOf(testGlobalSummaryLocal)

        Mockito.`when`(globalSummaryDao.getGlobalSummaryFlow()).thenAnswer {
            return@thenAnswer globalSummaryFlow
        }
        val result: GlobalSummary =
            globalSummaryRepository.getGlobalSummaryFlow().take(1).toList()[0]

        verify(globalSummaryDao, times(1)).getGlobalSummaryFlow()
        assertEquals(result, testGlobalSummaryLocal)
    }

    @Test
    fun getGlobalSummaryPieChartDataFlow_validData() = runBlocking {
        val globalSummaryFlow = flowOf(testGlobalSummaryLocal)

        Mockito.`when`(globalSummaryDao.getGlobalSummaryFlow()).thenAnswer {
            return@thenAnswer globalSummaryFlow
        }

        val expected: List<PieEntry> = testGlobalChartData
        val result: List<PieEntry> = globalSummaryRepository.getGlobalSummaryPieChartDataFlow()
            .take(1).toList()[0]

        verify(globalSummaryDao, times(1)).getGlobalSummaryFlow()
        for (i in result.indices) {
            assert(expected[i].value == result[i].value)
        }
    }

    @Test
    fun refreshData_force_validResponse_VerifyDataSaved() = runBlocking {
        val networkResponse = Response.success(testGlobalSummaryRemote)
        Mockito.`when`(apiService.getGlobalSummary()).thenAnswer {
            return@thenAnswer networkResponse
        }

        globalSummaryRepository.refreshData(forceRefresh = true)

        verify(apiService, times(1)).getGlobalSummary()
        verify(globalSummaryDao, times(1)).replace(testGlobalSummaryLocal)
    }

    @Test
    fun refreshData_force_errorResponse_VerifyNotDataSaved() = runBlocking {
        val responseError: Response<GlobalSummaryRemote> = Response.error(
            403,
            ResponseBody.create(
                MediaType.parse("application/json"), "Bad Request"
            )
        )
        Mockito.`when`(apiService.getGlobalSummary()).thenAnswer {
            return@thenAnswer responseError
        }

        globalSummaryRepository.refreshData(forceRefresh = true)

        verify(apiService, times(1)).getGlobalSummary()
        verify(globalSummaryDao, times(0)).replace(testGlobalSummaryLocal)
    }
}
