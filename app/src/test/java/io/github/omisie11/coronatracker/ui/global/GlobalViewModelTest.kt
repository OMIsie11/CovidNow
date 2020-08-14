package io.github.omisie11.coronatracker.ui.global

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.github.mikephil.charting.data.PieEntry
import com.nhaarman.mockitokotlin2.timeout
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.github.omisie11.coronatracker.data.local.model.GlobalSummary
import io.github.omisie11.coronatracker.data.repository.GlobalSummaryRepository
import io.github.omisie11.coronatracker.utils.getValue
import io.github.omisie11.coronatracker.utils.testGlobalSummary
import io.github.omisie11.coronatracker.utils.testGlobalSummaryPieChartData
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
class GlobalViewModelTest {

    private val testGlobalData = testGlobalSummary
    private val testGlobalChartData = testGlobalSummaryPieChartData

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = TestCoroutineDispatcher()

    @Mock
    private lateinit var repository: GlobalSummaryRepository

    // class under test
    private lateinit var globalViewModel: GlobalViewModel

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
    fun `get global summary info`() {
        whenever(repository.getGlobalSummaryFlow())
            .thenReturn(flowOf(testGlobalData))
        whenever(repository.getGlobalSummaryPieChartDataFlow())
            .thenReturn(flowOf(testGlobalChartData))
        whenever(repository.getFetchingStatus())
            .thenReturn(MutableStateFlow(false))

        globalViewModel = GlobalViewModel(repository)
        val result: GlobalSummary = getValue(globalViewModel.getGlobalSummary())

        assertEquals(testGlobalData, result)
    }

    @Test
    fun `get global summary data for pie chart`() {
        whenever(repository.getGlobalSummaryFlow())
            .thenReturn(flowOf(testGlobalData))
        whenever(repository.getGlobalSummaryPieChartDataFlow())
            .thenReturn(flowOf(testGlobalChartData))
        whenever(repository.getFetchingStatus())
            .thenReturn(MutableStateFlow(false))

        globalViewModel = GlobalViewModel(repository)
        val result: List<PieEntry> = getValue(globalViewModel.getGlobalPieChartData())

        assertEquals(testGlobalSummaryPieChartData, result)
    }

    @Test
    fun `verify calls when refreshing data`() = runBlocking {
        whenever(repository.getGlobalSummaryFlow())
            .thenReturn(flowOf(testGlobalData))
        whenever(repository.getGlobalSummaryPieChartDataFlow())
            .thenReturn(flowOf(testGlobalChartData))
        whenever(repository.getFetchingStatus())
            .thenReturn(MutableStateFlow(true))

        globalViewModel = GlobalViewModel(repository)
        globalViewModel.refreshGlobalSummary(forceRefresh = true)

        verify(repository, timeout(1000).times(1))
            .refreshData(Mockito.eq(true))
    }
}
