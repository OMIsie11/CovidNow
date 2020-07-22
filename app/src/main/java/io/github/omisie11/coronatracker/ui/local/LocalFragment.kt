package io.github.omisie11.coronatracker.ui.local

import android.content.SharedPreferences
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.google.android.material.snackbar.Snackbar
import io.github.omisie11.coronatracker.R
import io.github.omisie11.coronatracker.databinding.FragmentLocalBinding
import io.github.omisie11.coronatracker.util.PREFS_KEY_CHOSEN_LOCATION
import io.github.omisie11.coronatracker.vo.FetchResult
import kotlinx.android.synthetic.main.compound_single_stat.view.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class LocalFragment : Fragment() {

    private var _binding: FragmentLocalBinding? = null
    private val binding get() = _binding!!

    private val localViewModel by sharedViewModel<LocalViewModel>()
    private val sharedPrefs: SharedPreferences by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLocalBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        styleGlobalPieChart()
        binding.textCountryTitle.text = getChosenLocation()

        localViewModel.getSummary().observe(viewLifecycleOwner, Observer { summary ->
            if (summary != null) {
                binding.statConfirmed.stat_value.text =
                    summary.confirmed?.toString() ?: getString(R.string.no_data)
                binding.statRecovered.stat_value.text =
                    summary.recovered?.toString() ?: getString(R.string.no_data)
                binding.statDeaths.stat_value.text =
                    summary.deaths?.toString() ?: getString(R.string.no_data)
            }
        })

        localViewModel.getLocalPieChartData().observe(viewLifecycleOwner, Observer { chartData ->
            setDataToPieChart(chartData)
        })

        localViewModel.getDataFetchingStatus().observe(viewLifecycleOwner, Observer {
            binding.swipeRefresh.isRefreshing = it
        })

        localViewModel.snackbar.observe(viewLifecycleOwner, Observer { fetchResult ->
            val message: String? = when (fetchResult) {
                FetchResult.SERVER_ERROR -> getString(R.string.server_error)
                FetchResult.NETWORK_ERROR -> getString(R.string.network_error)
                FetchResult.UNEXPECTED_ERROR -> getString(R.string.unexpected_error)
                else -> null
            }
            message?.let {
                showErrorSnackbar(it)
                localViewModel.onSnackbarShown()
            }
        })

        binding.swipeRefresh.setOnRefreshListener {
            localViewModel.refreshLocalSummary(forceRefresh = true)
        }

        binding.imageEditLocation.setOnClickListener {
            findNavController().navigate(R.id.action_local_dest_to_settings_dest)
        }

        binding.statConfirmed.setOnClickListener { binding.pieChartLocal.highlightValue(0f, 0) }
        binding.statRecovered.setOnClickListener { binding.pieChartLocal.highlightValue(1f, 0) }
        binding.statDeaths.setOnClickListener { binding.pieChartLocal.highlightValue(2f, 0) }
    }

    override fun onResume() {
        super.onResume()
        localViewModel.refreshLocalSummary(forceRefresh = false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setDataToPieChart(data: List<PieEntry>) {
        val dataSet = PieDataSet(data, "")
        setupDataSetStyle(dataSet)
        val pieData = PieData(dataSet)
        binding.pieChartLocal.data = pieData
        binding.pieChartLocal.invalidate()
    }

    private fun setupDataSetStyle(dataSet: PieDataSet) = dataSet.apply {
        colors = getPieChartColorsPalette()
        setDrawValues(false)
    }

    private fun styleGlobalPieChart() = binding.pieChartLocal.apply {
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
                localViewModel.refreshLocalSummary(true)
            }
            show()
        }
    }

    private fun getPieChartColorsPalette(): List<Int> = listOf(
        ContextCompat.getColor(context!!, R.color.pie_chart_yellow),
        ContextCompat.getColor(context!!, R.color.pie_chart_green),
        ContextCompat.getColor(context!!, R.color.pie_chart_red)
    )

    private fun getChosenLocation(): String =
        sharedPrefs.getString(PREFS_KEY_CHOSEN_LOCATION, "Poland") ?: "Poland"
}
