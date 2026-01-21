package com.example.beer.ui.navigationbar

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.beer.data.enums.TabItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

private const val TAG = "TabsViewModel"

@HiltViewModel
class TabsViewModel @Inject constructor() : ViewModel() {

    private val _selectedTab = MutableStateFlow(TabItem.Beer)
    val selectedTab = _selectedTab.asStateFlow()

    private val _content = MutableStateFlow("Willkommen auf Home")
    val content = _content.asStateFlow()

    fun selectTab(tab: TabItem) {
        Log.d(TAG, "Navigating to tab: ${tab.name}")
        _selectedTab.value = tab

        _content.value = when (tab) {
            TabItem.Beer -> "Bier"
            TabItem.Rating -> "Rating"
            TabItem.Settings -> "Settings"
        }
    }
}