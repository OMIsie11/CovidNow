package io.github.omisie11.coronatracker.data.repository

import android.content.SharedPreferences
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import io.github.omisie11.coronatracker.data.local.dao.LocalSummaryDao
import io.github.omisie11.coronatracker.data.local.model.LocalSummary
import io.github.omisie11.coronatracker.data.remote.ApiService
import io.github.omisie11.coronatracker.utils.testLocalSummary
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
class LocalSummaryRepositoryTest {

    private val testLocalSummaryLocal = testLocalSummary

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
    fun getCountriesNamesFlowTest() = kotlinx.coroutines.runBlocking {
        val localSummaryFlow = flowOf(testLocalSummaryLocal)

        Mockito.`when`(localSummaryDao.getLocalSummaryFlow()).thenAnswer {
            return@thenAnswer localSummaryFlow
        }

        val result: LocalSummary =
            localSummaryRepository.getLocalSummaryFlow().take(1).toList()[0]

        verify(localSummaryDao, times(1)).getLocalSummaryFlow()
        assertEquals(result, testLocalSummaryLocal)
    }
}
