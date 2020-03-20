package io.github.omisie11.coronatracker.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.omisie11.coronatracker.data.MainRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GlobalViewModel(private val repository: MainRepository) : ViewModel() {

    fun fetchData() {
        viewModelScope.launch(Dispatchers.IO) { repository.fetchGlobalSummary() }
    }
}
