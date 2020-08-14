package io.github.omisie11.coronatracker.data.repository

import android.content.SharedPreferences
import com.github.mikephil.charting.data.PieEntry
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.github.omisie11.coronatracker.data.local.dao.GlobalSummaryDao
import io.github.omisie11.coronatracker.data.remote.ApiService
import io.github.omisie11.coronatracker.data.remote.model.GlobalSummaryRemote
import io.github.omisie11.coronatracker.utils.testGlobalSummary
import io.github.omisie11.coronatracker.utils.testGlobalSummaryPieChartData
import io.github.omisie11.coronatracker.utils.testGlobalSummaryRemote
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
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
    fun `get data with global summary info`() = runBlocking {
        whenever(globalSummaryDao.getGlobalSummaryFlow()).thenReturn(flowOf(testGlobalSummaryLocal))

        val result = globalSummaryRepository.getGlobalSummaryFlow().first()

        verify(globalSummaryDao, times(1)).getGlobalSummaryFlow()
        assertEquals(result, testGlobalSummaryLocal)
    }

    @Test
    fun `get data for global summary chart`() = runBlocking {
        whenever(globalSummaryDao.getGlobalSummaryFlow()).thenReturn(flowOf(testGlobalSummaryLocal))

        val expected: List<PieEntry> = testGlobalChartData
        val result = globalSummaryRepository.getGlobalSummaryPieChartDataFlow().first()

        verify(globalSummaryDao, times(1)).getGlobalSummaryFlow()
        for (i in result.indices) {
            assert(expected[i].value == result[i].value)
        }
    }

    @Test
    fun `verify calls performed on force refresh with valid response`() = runBlocking {
        val successResponse = Response.success(testGlobalSummaryRemote)
        whenever(apiService.getGlobalSummary()).thenReturn(successResponse)

        globalSummaryRepository.refreshData(forceRefresh = true)

        verify(apiService, times(1)).getGlobalSummary()
        verify(globalSummaryDao, times(1)).replace(testGlobalSummaryLocal)
    }

    @Test
    fun `verify calls performed on force refresh with error response`() = runBlocking {
        val errorResponse: Response<GlobalSummaryRemote> = Response.error(
            403,
            ResponseBody.create(
                MediaType.parse("application/json"), "Bad Request"
            )
        )
        whenever(apiService.getGlobalSummary()).thenReturn(errorResponse)

        globalSummaryRepository.refreshData(forceRefresh = true)

        verify(apiService, times(1)).getGlobalSummary()
        verify(globalSummaryDao, times(0)).replace(testGlobalSummaryLocal)
    }
}
