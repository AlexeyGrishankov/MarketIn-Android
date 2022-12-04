package ru.grishankov.marketin.screens.app_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.grishankov.marketin.data.repositories.ApplicationsRepo
import ru.grishankov.marketin.screens.app_list.state.AppListState

class ScreenAppListViewModel(
    private val applicationsRepo: ApplicationsRepo
) : ViewModel() {

    private val _appListState = MutableStateFlow(AppListState())
    val appListState = _appListState.asStateFlow()

    fun getAppList() {
        _appListState.value = AppListState(isLoading = true)
        viewModelScope.launch {
            applicationsRepo.getAppList().onEach { data ->
                _appListState.value = AppListState(content = data.data, error = data.error)
            }.collect()
        }
    }
}