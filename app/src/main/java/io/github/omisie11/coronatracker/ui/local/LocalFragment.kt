package io.github.omisie11.coronatracker.ui.local

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import io.github.omisie11.coronatracker.R
import io.github.omisie11.coronatracker.vo.FetchResult
import kotlinx.android.synthetic.main.fragment_local.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class LocalFragment : Fragment() {

    private val localViewModel by viewModel<LocalViewModel>()

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

        localViewModel.getSummary().observe(viewLifecycleOwner, Observer { summary ->
            if (summary != null) {
                text_confirmed.text = summary.confirmed?.toString() ?: "No data"
                text_recovered.text = summary.recovered?.toString() ?: "No data"
                text_deaths.text = summary.deaths?.toString() ?: "No data"
            }
        })

        localViewModel.getDataFetchingStatus().observe(viewLifecycleOwner, Observer {
            swipe_refresh.isRefreshing = it
        })

        localViewModel.snackbar.observe(viewLifecycleOwner, Observer { fetchResult ->
            val message: String? = when (fetchResult) {
                FetchResult.SERVER_ERROR -> "Server error"
                FetchResult.NETWORK_ERROR -> "Network error"
                FetchResult.UNEXPECTED_ERROR -> "Unexpected error occurred"
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
    }

    override fun onResume() {
        super.onResume()
        Timber.d("On resume")
        localViewModel.refreshLocalSummary(forceRefresh = false)
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
            setAction("Retry") { localViewModel.refreshLocalSummary(forceRefresh = true) }
            show()
        }
    }
}
