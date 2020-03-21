package io.github.omisie11.coronatracker.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.omisie11.coronatracker.data.MainRepository
import io.github.omisie11.coronatracker.data.model.GlobalSummary
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class GlobalViewModel(private val repository: MainRepository) : ViewModel() {

    private val globalSummary = MutableLiveData<GlobalSummary>()
    private val _isDataLoading = MutableLiveData<Boolean>(false)

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getGlobalSummaryFlow().collect {
                globalSummary.postValue(it)
            }
        }
    }

    fun getGlobalSummary(): LiveData<GlobalSummary> = globalSummary

    fun fetchGlobalSummary() {
        viewModelScope.launch(Dispatchers.IO) { repository.fetchGlobalSummary() }
    }
}
