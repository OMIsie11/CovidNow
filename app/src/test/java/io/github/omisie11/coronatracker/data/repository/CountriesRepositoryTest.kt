package io.github.omisie11.coronatracker.data.repository

import android.content.SharedPreferences
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.github.omisie11.coronatracker.data.local.dao.CountriesDao
import io.github.omisie11.coronatracker.data.remote.ApiService
import io.github.omisie11.coronatracker.data.remote.model.CountriesRemote
import io.github.omisie11.coronatracker.utils.testCountriesLocalList
import io.github.omisie11.coronatracker.utils.testCountriesLocalNames
import io.github.omisie11.coronatracker.utils.testCountriesRemote
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
class CountriesRepositoryTest {

    private val testDispatcher = TestCoroutineDispatcher()

    @Mock
    private lateinit var apiService: ApiService

    @Mock
    private lateinit var countriesDao: CountriesDao

    @Mock
    private lateinit var sharedPrefs: SharedPreferences

    // Class under test
    private lateinit var countriesRepository: CountriesRepository

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        MockitoAnnotations.initMocks(this)

        countriesRepository = CountriesRepository(apiService, countriesDao, sharedPrefs)
    }

    @After
    fun cleanup() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun getCountriesNamesFlowTest() = runBlocking {
        whenever(countriesDao.getCountriesNamesFlow()).thenReturn(flowOf(testCountriesLocalNames))

        val result = countriesRepository.getCountriesNamesFlow().first()

        verify(countriesDao, times(1)).getCountriesNamesFlow()
        assertEquals(result, testCountriesLocalNames)
    }

    @Test
    fun refreshData_force_validResponse_VerifyDataSaved() = runBlocking {
        val successResponse = Response.success(testCountriesRemote)
        whenever(apiService.getCountries()).thenReturn(successResponse)

        countriesRepository.refreshData(forceRefresh = true)

        verify(apiService, times(1)).getCountries()
        verify(countriesDao, times(1)).replace(testCountriesLocalList)
    }

    @Test
    fun refreshData_force_errorResponse_VerifyNotDataSaved() = runBlocking {
        val errorResponse: Response<CountriesRemote> = Response.error(
            403,
            ResponseBody.create(
                MediaType.parse("application/json"), "Bad Request"
            )
        )

        whenever(apiService.getCountries()).thenReturn(errorResponse)
        countriesRepository.refreshData(forceRefresh = true)

        verify(apiService, times(1)).getCountries()
        verify(countriesDao, times(0)).replace(testCountriesLocalList)
    }
}
