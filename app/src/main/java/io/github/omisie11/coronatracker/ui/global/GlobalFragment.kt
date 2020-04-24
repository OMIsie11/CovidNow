package io.github.omisie11.coronatracker.ui.global

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.google.android.material.snackbar.Snackbar
import io.github.omisie11.coronatracker.R
import io.github.omisie11.coronatracker.databinding.FragmentGlobalBinding
import io.github.omisie11.coronatracker.vo.FetchResult
import kotlinx.android.synthetic.main.compound_single_stat.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class GlobalFragment : Fragment() {

    private var _binding: FragmentGlobalBinding? = null
    private val binding get() = _binding!!

    private val globalViewModel by viewModel<GlobalViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGlobalBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        styleGlobalPieChart()

        globalViewModel.getGlobalSummary().observe(viewLifecycleOwner, Observer { summary ->
            if (summary != null) {
                binding.statConfirmed.stat_value.text =
                    summary.confirmed?.toString() ?: getString(R.string.no_data)
                binding.statRecovered.stat_value.text =
                    summary.recovered?.toString() ?: getString(R.string.no_data)
                binding.statDeaths.stat_value.text =
                    summary.deaths?.toString() ?: getString(R.string.no_data)
            }
        })

        globalViewModel.getGlobalPieChartData().observe(viewLifecycleOwner, Observer { chartData ->
            setDataToPieChart(chartData)
        })

        globalViewModel.getDataFetchingStatus().observe(viewLifecycleOwner, Observer {
            binding.swipeRefresh.isRefreshing = it
        })

        globalViewModel.snackbar.observe(viewLifecycleOwner, Observer { fetchResult ->
            val message: String? = when (fetchResult) {
                FetchResult.SERVER_ERROR -> getString(R.string.server_error)
                FetchResult.NETWORK_ERROR -> getString(R.string.network_error)
                FetchResult.UNEXPECTED_ERROR -> getString(R.string.unexpected_error)
                else -> null
            }
            message?.let {
                showErrorSnackbar(it)
                globalViewModel.onSnackbarShown()
            }
        })

        binding.swipeRefresh.setOnRefreshListener {
            globalViewModel.refreshGlobalSummary(forceRefresh = true)
        }

        binding.statConfirmed.setOnClickListener { binding.pieChartGlobal.highlightValue(0f, 0) }
        binding.statRecovered.setOnClickListener { binding.pieChartGlobal.highlightValue(1f, 0) }
        binding.statDeaths.setOnClickListener { binding.pieChartGlobal.highlightValue(2f, 0) }
    }

    override fun onResume() {
        super.onResume()
        globalViewModel.refreshGlobalSummary(forceRefresh = false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setDataToPieChart(data: List<PieEntry>) {
        val dataSet = PieDataSet(data, "")
        setupDataSetStyle(dataSet)
        val pieData = PieData(dataSet)
        binding.pieChartGlobal.data = pieData
        binding.pieChartGlobal.invalidate()
    }

    private fun setupDataSetStyle(dataSet: PieDataSet) = dataSet.apply {
        colors = getPieChartColorsPalette()
        setDrawValues(false)
    }

    private fun styleGlobalPieChart() = binding.pieChartGlobal.apply {
        description.isEnabled = false
        legend.isEnabled = false
        isDrawHoleEnabled = true
        holeRadius = 60f
        setTouchEnabled(false)
        setDrawEntryLabels(false)
        transparentCircleRadius = 0f
        setHoleColor(ContextCompat.getColor(requireContext(), R.color.background))
        invalidate()
    }

    private fun showErrorSnackbar(text: String) {
        val snackbar = Snackbar.make(binding.swipeRefresh, text, Snackbar.LENGTH_LONG)
        val layoutParams = snackbar.view.layoutParams as CoordinatorLayout.LayoutParams
        layoutParams.apply {
            anchorId = R.id.bottom_navigation
            layoutParams.anchorGravity = Gravity.TOP
            layoutParams.gravity = Gravity.TOP
        }
        snackbar.apply {
            view.layoutParams = layoutParams
            setActionTextColor(ContextCompat.getColor(requireContext(), R.color.secondary))
            setAction(getString(R.string.Retry)) {
                globalViewModel.refreshGlobalSummary(true)
            }
            show()
        }
    }

    private fun getPieChartColorsPalette(): List<Int> = listOf(
        ContextCompat.getColor(context!!, R.color.pie_chart_yellow),
        ContextCompat.getColor(context!!, R.color.pie_chart_green),
        ContextCompat.getColor(context!!, R.color.pie_chart_red)
    )
}
