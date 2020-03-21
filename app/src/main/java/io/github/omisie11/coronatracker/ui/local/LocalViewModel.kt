package io.github.omisie11.coronatracker.ui.local

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.omisie11.coronatracker.data.local.model.LocalSummary
import io.github.omisie11.coronatracker.data.repository.LocalSummaryRepository
import io.github.omisie11.coronatracker.vo.FetchResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class LocalViewModel(private val repository: LocalSummaryRepository) : ViewModel() {

    private val localSummary = MutableLiveData<LocalSummary>()
    private val isDataFetching: LiveData<Boolean> = repository.getFetchingStatus()
    private val _snackBar: MutableLiveData<FetchResult> = repository.getFetchResult()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getLocalSummaryFlow().collect { localSummary.postValue(it) }
        }
    }

    fun getSummary(): LiveData<LocalSummary> = localSummary

    fun getDataFetchingStatus(): LiveData<Boolean> = isDataFetching

    fun fetchSummary() {
        viewModelScope.launch(Dispatchers.IO) { repository.fetchDataFromApi() }
    }

    /**
     * Request a snackbar to display a string.
     */
    val snackbar: LiveData<FetchResult>
        get() = _snackBar

    /**
     * Called immediately after the UI shows the snackbar.
     */
    fun onSnackbarShown() {
        _snackBar.value = FetchResult.OK
    }
}
