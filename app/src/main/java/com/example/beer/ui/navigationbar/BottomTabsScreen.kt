package com.example.beer.ui.navigationbar

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.beer.data.enums.TabItem
import com.example.beer.ui.beer.BeerTabViewModel
import com.example.beer.ui.rating.RatingTabViewModel
import com.example.beer.ui.setting.SettingsTabViewModel
import com.example.beer.ui.beer.BeerTabScreen
import com.example.beer.ui.rating.RatingTabScreen
import com.example.beer.ui.setting.SettingsTabScreen
import androidx.compose.material3.Icon
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItemDefaults

@Composable
fun BottomTabsScreen(
    beerTabViewModel: BeerTabViewModel = hiltViewModel(),
    ratingTabViewModel: RatingTabViewModel = hiltViewModel(),
    settingsTabViewModel: SettingsTabViewModel = hiltViewModel(),
    tabsViewModel: TabsViewModel = hiltViewModel()
) {
    val selectedTab by tabsViewModel.selectedTab.collectAsState()
    val navBarColor = Color(0xFFFFB300)
    val selectedContentColor = Color.White
    val unselectedContentColor = Color.White.copy(alpha = 0.6f)

    Scaffold(
        bottomBar = {
            NavigationBar (
                containerColor = navBarColor,
                contentColor = Color.White
            ) {
                TabItem.values().forEach { tab ->
                    val isSelected = tab == selectedTab

                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { tabsViewModel.selectTab(tab) },
                        label = { Text(text = tab.title, color = if (isSelected) selectedContentColor else unselectedContentColor) },
                        icon = {
                            Icon(
                                imageVector = if (isSelected) tab.selectedIcon else tab.unselectedIcon,
                                contentDescription = tab.title
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = selectedContentColor,
                            unselectedIconColor = unselectedContentColor,
                            selectedTextColor = selectedContentColor,
                            unselectedTextColor = unselectedContentColor,
                            indicatorColor = Color.White.copy(alpha = 0.2f)

                        )
                    )
                }
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            when (selectedTab) {
                TabItem.Beer -> BeerTabScreen(viewModel = beerTabViewModel)
                TabItem.Rating -> RatingTabScreen(viewModel = ratingTabViewModel)
                TabItem.Settings -> SettingsTabScreen(viewModel = settingsTabViewModel)
            }
        }
    }
}