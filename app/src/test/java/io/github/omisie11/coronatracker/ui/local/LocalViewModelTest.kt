package io.github.omisie11.coronatracker.ui.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.github.mikephil.charting.data.PieEntry
import com.nhaarman.mockitokotlin2.timeout
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.github.omisie11.coronatracker.data.local.model.LocalSummary
import io.github.omisie11.coronatracker.data.repository.LocalSummaryRepository
import io.github.omisie11.coronatracker.utils.getValue
import io.github.omisie11.coronatracker.utils.testLocalSummary
import io.github.omisie11.coronatracker.utils.testLocalSummaryPieChartData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class LocalViewModelTest {

    private val testLocalData = testLocalSummary
    private val testLocalChartData = testLocalSummaryPieChartData

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = TestCoroutineDispatcher()

    @Mock
    private lateinit var repository: LocalSummaryRepository

    // class under test
    private lateinit var localViewModel: LocalViewModel

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun cleanup() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun getLocalSummary() {
        whenever(repository.getLocalSummaryFlow())
            .thenReturn(flowOf(testLocalData))
        whenever(repository.getLocalSummaryPieChartDataFlow())
            .thenReturn(flowOf(testLocalChartData))
        whenever(repository.getFetchingStatus())
            .thenReturn(MutableStateFlow(false))

        localViewModel = LocalViewModel(repository)
        val result: LocalSummary = getValue(localViewModel.getSummary())

        assertEquals(testLocalData, result)
    }

    @Test
    fun getLocalPieChartData() {
        whenever(repository.getLocalSummaryFlow())
            .thenReturn(flowOf(testLocalData))
        whenever(repository.getLocalSummaryPieChartDataFlow())
            .thenReturn(flowOf(testLocalChartData))
        whenever(repository.getFetchingStatus())
            .thenReturn(MutableStateFlow(false))

        localViewModel = LocalViewModel(repository)
        val result: List<PieEntry> = getValue(localViewModel.getLocalPieChartData())

        assertEquals(testLocalSummaryPieChartData, result)
    }

    @Test
    fun refreshLocalSummary_verifyCalls() = runBlocking {
        whenever(repository.getLocalSummaryFlow())
            .thenReturn(flowOf(testLocalData))
        whenever(repository.getLocalSummaryPieChartDataFlow())
            .thenReturn(flowOf(testLocalChartData))
        whenever(repository.getFetchingStatus())
            .thenReturn(MutableStateFlow(true))

        localViewModel = LocalViewModel(repository)
        localViewModel.refreshLocalSummary(forceRefresh = true)

        verify(repository, timeout(1000).times(1))
            .refreshData(Mockito.eq(true))
    }
}
