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
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.google.android.material.snackbar.Snackbar
import io.github.omisie11.coronatracker.R
import io.github.omisie11.coronatracker.util.PREFS_KEY_CHOSEN_LOCATION
import io.github.omisie11.coronatracker.vo.FetchResult
import kotlinx.android.synthetic.main.fragment_local.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class LocalFragment : Fragment() {

    private val localViewModel by sharedViewModel<LocalViewModel>()
    private val sharedPrefs: SharedPreferences by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_local, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        styleGlobalPieChart()
        text_country_title.text = getChosenLocation()

        localViewModel.getSummary().observe(viewLifecycleOwner, Observer { summary ->
            if (summary != null) {
                text_confirmed.text = summary.confirmed?.toString() ?: getString(R.string.no_data)
                text_recovered.text = summary.recovered?.toString() ?: getString(R.string.no_data)
                text_deaths.text = summary.deaths?.toString() ?: getString(R.string.no_data)
            }
        })

        localViewModel.getLocalPieChartData().observe(viewLifecycleOwner, Observer { chartData ->
            setDataToPieChart(chartData)
        })

        localViewModel.getDataFetchingStatus().observe(viewLifecycleOwner, Observer {
            swipe_refresh.isRefreshing = it
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

        swipe_refresh.setOnRefreshListener {
            localViewModel.refreshLocalSummary(forceRefresh = true)
        }

        image_edit_location.setOnClickListener {
            val bottomSheetFragment = ChooseLocationBottomDialogFragment()
            bottomSheetFragment.show(
                requireActivity().supportFragmentManager,
                bottomSheetFragment.tag
            )
        }

        image_confirmed.setOnClickListener { pie_chart_local.highlightValue(0f, 0) }
        image_recovered.setOnClickListener { pie_chart_local.highlightValue(1f, 0) }
        image_deaths.setOnClickListener { pie_chart_local.highlightValue(2f, 0) }
    }

    override fun onResume() {
        super.onResume()
        localViewModel.refreshLocalSummary(forceRefresh = false)
    }

    private fun setDataToPieChart(data: List<PieEntry>) {
        val dataSet = PieDataSet(data, "")
        setupDataSetStyle(dataSet)
        val pieData = PieData(dataSet)
        pie_chart_local.data = pieData
        pie_chart_local.invalidate()
    }

    private fun setupDataSetStyle(dataSet: PieDataSet) = dataSet.apply {
        colors = getPieChartColorsPalette()
        setDrawValues(false)
    }

    private fun styleGlobalPieChart() = pie_chart_local.apply {
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
        val snackbar = Snackbar.make(swipe_refresh, text, Snackbar.LENGTH_LONG)
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
