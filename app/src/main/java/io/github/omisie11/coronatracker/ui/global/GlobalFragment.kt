package io.github.omisie11.coronatracker.ui.global

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import io.github.omisie11.coronatracker.R
import io.github.omisie11.coronatracker.vo.FetchResult
import kotlinx.android.synthetic.main.fragment_global.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class GlobalFragment : Fragment() {

    private val globalViewModel by viewModel<GlobalViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_global, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        globalViewModel.getGlobalSummary().observe(viewLifecycleOwner, Observer {
            if (it != null) {
                text_view.text = it.confirmed.toString()
            }
        })

        globalViewModel.getDataFetchingStatus().observe(viewLifecycleOwner, Observer {
            swipe_refresh.isRefreshing = it
        })

        globalViewModel.snackbar.observe(viewLifecycleOwner, Observer { fetchResult ->
            val message: String? = when (fetchResult) {
                FetchResult.SERVER_ERROR -> "Server error"
                FetchResult.NETWORK_ERROR -> "Network error"
                FetchResult.UNEXPECTED_ERROR -> "Unexpected error occurred"
                else -> null
            }
            message?.let {
                Snackbar.make(swipe_refresh, it, Snackbar.LENGTH_LONG).show()
                globalViewModel.onSnackbarShown()
            }
        })

        swipe_refresh.setOnRefreshListener {
            globalViewModel.fetchGlobalSummary()
        }
    }
}
