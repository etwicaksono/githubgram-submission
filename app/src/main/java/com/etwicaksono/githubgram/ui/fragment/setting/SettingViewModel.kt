package com.etwicaksono.githubgram.ui.fragment.setting

import androidx.lifecycle.*
import kotlinx.coroutines.launch

class SettingViewModel(private val pref: SettingPreferences?) : ViewModel() {
    class Factory(private val pref: SettingPreferences?) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return SettingViewModel(pref) as T
        }
    }

    fun getThemeSettings(): LiveData<Boolean>? {
        return pref?.getThemeSetting()?.asLiveData()
    }

    fun saveThemeSetting(isDarkMode: Boolean) {
        viewModelScope.launch { pref?.saveThemeSetting(isDarkMode) }
    }
}