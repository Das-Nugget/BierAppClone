package com.example.beer.ui.beer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.beer.data.model.BeerModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun BeerTabScreen(viewModel: BeerTabViewModel) {
    val beers by viewModel.allBeers.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(beers) { beer ->
                BeerItem(beer = beer)
            }
        }
    }
}

@Composable
fun BeerItem(beer: BeerModel) {

    Card (
        modifier = Modifier.fillMaxWidth(),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(0)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(Color(0xFFFFB300)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "IMG",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = beer.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = beer.producer,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                Text(
                    text = "year",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
            Box(
                modifier = Modifier.fillMaxHeight(),
                contentAlignment = Alignment.BottomEnd
            ) {
                Text(
                    text = "created-date",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }
        HorizontalDivider(thickness = 0.5.dp, color = Color.LightGray)
    }
}
