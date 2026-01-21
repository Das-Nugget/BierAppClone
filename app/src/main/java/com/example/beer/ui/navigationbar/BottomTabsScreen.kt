package com.example.beer.ui.navigationbar

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.beer.data.enums.TabItem
import com.example.beer.ui.beer.BeerTabScreen
import com.example.beer.ui.beer.BeerTabViewModel
import com.example.beer.ui.rating.RatingTabScreen
import com.example.beer.ui.rating.RatingTabViewModel
import com.example.beer.ui.setting.SettingsTabScreen
import com.example.beer.ui.setting.SettingsTabViewModel

private const val TAG = "BottomTabsScreen"

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
                        onClick = {
                            Log.d(TAG, "Tab clicked: ${tab.name}")
                            tabsViewModel.selectTab(tab)
                        },
                        label = {
                            Text(
                                text = tab.title,
                                color = if (isSelected) selectedContentColor else unselectedContentColor
                            )
                        },
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
            /**
             * The 'when' block acts as the primary navigation engine,
             * swapping the content of the screen based on the state of the tabsViewModel.
             */
            when (selectedTab) {
                TabItem.Beer -> {
                    Log.v(TAG, "Displaying Beer Tab")
                    BeerTabScreen(viewModel = beerTabViewModel)
                }
                TabItem.Rating -> {
                    Log.v(TAG, "Displaying Rating Tab")
                    RatingTabScreen(viewModel = ratingTabViewModel)
                }
                TabItem.Settings -> {
                    Log.v(TAG, "Displaying Settings Tab")
                    SettingsTabScreen(viewModel = settingsTabViewModel)
                }
            }
        }
    }
}