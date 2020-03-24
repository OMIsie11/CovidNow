package io.github.omisie11.coronatracker.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.omisie11.coronatracker.data.repository.CountriesRepository
import io.github.omisie11.coronatracker.vo.FetchResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class CountriesViewModel(private val repository: CountriesRepository) : ViewModel() {

    private val countriesNamesList = MutableLiveData<List<String>>()
    private val isDataFetching: LiveData<Boolean> = repository.getFetchingStatus()
    private val _snackBar: MutableLiveData<FetchResult> = repository.getFetchResult()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getCountriesNamesFlow().collect { countriesNamesList.postValue(it) }
        }
    }

    fun getCountries(): LiveData<List<String>> = countriesNamesList

    fun refreshCountriesData(forceRefresh: Boolean = false) {
        viewModelScope.launch(Dispatchers.IO) { repository.refreshData(forceRefresh) }
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
