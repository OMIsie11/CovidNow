package io.github.omisie11.coronatracker

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.github.omisie11.coronatracker.ui.GlobalViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val globalViewModel by viewModel<GlobalViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        globalViewModel.fetchData()
    }
}
