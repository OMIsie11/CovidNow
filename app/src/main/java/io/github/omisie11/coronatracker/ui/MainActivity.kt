package io.github.omisie11.coronatracker.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import io.github.omisie11.coronatracker.R
import io.github.omisie11.coronatracker.vo.FetchResult
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val globalViewModel by viewModel<GlobalViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        globalViewModel.getGlobalSummary().observe(this, Observer {
            if (it != null) {
                text_view.text = it.confirmed.toString()
            }
        })

        globalViewModel.getDataFetchingStatus().observe(this, Observer {
            swipe_refresh.isRefreshing = it
        })

        globalViewModel.snackbar.observe(this, Observer { fetchResult ->
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
