package io.github.omisie11.coronatracker.ui.local

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import io.github.omisie11.coronatracker.R
import io.github.omisie11.coronatracker.vo.FetchResult
import kotlinx.android.synthetic.main.fragment_local.*
import org.koin.androidx.viewmodel.ext.android.viewModel

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
                Snackbar.make(swipe_refresh, it, Snackbar.LENGTH_LONG).show()
                localViewModel.onSnackbarShown()
            }
        })

        swipe_refresh.setOnRefreshListener { localViewModel.fetchSummary() }
    }
}
