package com.example.beer.ui.navigationbar

import androidx.lifecycle.ViewModel
import com.example.beer.data.enums.TabItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class TabsViewModel @Inject constructor() : ViewModel() {

    // aktuell ausgewählter Tab (Index oder Enum)
    private val _selectedTab = MutableStateFlow(TabItem.Beer)
    val selectedTab = _selectedTab.asStateFlow()

    // Beispiel: Inhalt für den aktuell ausgewählten Tab
    private val _content = MutableStateFlow("Willkommen auf Home")
    val content = _content.asStateFlow()

    fun selectTab(tab: TabItem) {
        _selectedTab.value = tab

        _content.value = when (tab) {
            TabItem.Beer -> "Bier"
            TabItem.Rating -> "Rating"
            TabItem.Settings -> "Settings"
        }
    }
}