package io.github.omisie11.coronatracker.data.repository

import android.content.SharedPreferences
import com.github.mikephil.charting.data.PieEntry
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.github.omisie11.coronatracker.data.local.dao.LocalSummaryDao
import io.github.omisie11.coronatracker.data.local.model.LocalSummary
import io.github.omisie11.coronatracker.data.remote.ApiService
import io.github.omisie11.coronatracker.data.remote.BASE_COUNTRY_URL
import io.github.omisie11.coronatracker.data.remote.model.LocalSummaryRemote
import io.github.omisie11.coronatracker.utils.testLocalSummary
import io.github.omisie11.coronatracker.utils.testLocalSummaryPieChartData
import io.github.omisie11.coronatracker.utils.testLocalSummaryRemote
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
class LocalSummaryRepositoryTest {

    private val testLocalSummaryLocal = testLocalSummary
    private val testLocalChartData = testLocalSummaryPieChartData
    private val testCountryUrl = BASE_COUNTRY_URL + "Poland"

    private val testDispatcher = TestCoroutineDispatcher()

    @Mock
    private lateinit var apiService: ApiService

    @Mock
    private lateinit var localSummaryDao: LocalSummaryDao

    @Mock
    private lateinit var sharedPrefs: SharedPreferences

    // Class under test
    private lateinit var localSummaryRepository: LocalSummaryRepository

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        MockitoAnnotations.initMocks(this)

        localSummaryRepository = LocalSummaryRepository(apiService, localSummaryDao, sharedPrefs)
    }

    @After
    fun cleanup() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun getLocalSummaryFlowTest() = runBlocking {
        whenever(localSummaryDao.getLocalSummaryFlow()).thenReturn(flowOf(testLocalSummaryLocal))

        val result: LocalSummary = localSummaryRepository.getLocalSummaryFlow().first()

        verify(localSummaryDao, times(1)).getLocalSummaryFlow()
        assertEquals(result, testLocalSummaryLocal)
    }

    @Test
    fun getLocalSummaryPieChartDataFlow_validData() = runBlocking {
        whenever(localSummaryDao.getLocalSummaryFlow()).thenReturn(flowOf(testLocalSummaryLocal))

        val expected: List<PieEntry> = testLocalChartData

        val result = localSummaryRepository
            .getLocalSummaryPieChartDataFlow()
            .first()

        verify(localSummaryDao, times(1)).getLocalSummaryFlow()
        for (i in result.indices) {
            assert(expected[i].value == result[i].value)
        }
    }

    @Test
    fun refreshData_force_validResponse_VerifyDataSaved() = runBlocking {
        val successResponse = Response.success(testLocalSummaryRemote)
        whenever(apiService.getLocalSummary(testCountryUrl)).thenReturn(successResponse)

        localSummaryRepository.refreshData(forceRefresh = true)

        verify(apiService, times(1)).getLocalSummary(testCountryUrl)
        verify(localSummaryDao, times(1)).replace(testLocalSummaryLocal)
    }

    @Test
    fun refreshData_force_errorResponse_VerifyNotDataSaved() = runBlocking {
        val errorResponse: Response<LocalSummaryRemote> = Response.error(
            403,
            ResponseBody.create(
                MediaType.parse("application/json"), "Bad Request"
            )
        )
        whenever(apiService.getLocalSummary(testCountryUrl)).thenReturn(errorResponse)

        localSummaryRepository.refreshData(forceRefresh = true)

        verify(apiService, times(1)).getLocalSummary(testCountryUrl)
        verify(localSummaryDao, times(0)).replace(testLocalSummaryLocal)
    }
}
