package io.github.omisie11.coronatracker.data.repository

import android.content.SharedPreferences
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import io.github.omisie11.coronatracker.data.local.dao.CountriesDao
import io.github.omisie11.coronatracker.data.remote.ApiService
import io.github.omisie11.coronatracker.utils.testCountry1
import io.github.omisie11.coronatracker.utils.testCountry2
import io.github.omisie11.coronatracker.utils.testCountry3
import io.github.omisie11.coronatracker.utils.testCountry4
import io.github.omisie11.coronatracker.utils.testCountry5
import io.github.omisie11.coronatracker.utils.testCountry6
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class CountriesRepositoryTest {

    private val testCountriesLocal = listOf(
        testCountry3,
        testCountry4,
        testCountry1,
        testCountry2,
        testCountry5,
        testCountry6
    )

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
        val countriesFlow = flowOf(testCountriesLocal)

        Mockito.`when`(countriesDao.getCountriesNamesFlow()).thenAnswer {
            return@thenAnswer countriesFlow
        }

        val result: List<String> =
            countriesRepository.getCountriesNamesFlow().take(1).toList()[0]

        verify(countriesDao, times(1)).getCountriesNamesFlow()
        assertEquals(result, testCountriesLocal)
    }
}
