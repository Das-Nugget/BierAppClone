package com.example.beer

import android.app.Application
import android.content.Context
import android.util.Log
import com.example.beer.data.AppDatabase
import com.example.beer.data.dto.BeerDto
import com.example.beer.data.dto.toModel
import com.example.beer.data.model.BeerModel
import com.example.beer.interfaces.BeerRepository
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltAndroidApp
class App : Application() {
    @Inject
    lateinit var beerRepository: BeerRepository
    @Inject
    lateinit var json: Json

    @Inject
    lateinit var database: AppDatabase

    override fun onCreate() {
        super.onCreate()
        // Zugriff erzwingt, dass Hilt DB baut
        database.openHelper.writableDatabase  // legt app.db an

        //Erzeugt seed daten in db
        seedBeersIfFirstStart()
    }

    private fun seedBeersIfFirstStart() {
        val prefs = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val alreadySeeded = prefs.getBoolean("beers_seeded", false)
        if (alreadySeeded) return

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val text = assets.open("seed_beers.json")
                    .bufferedReader()
                    .use { reader ->
                        reader.readText()
                    }

                val dtos = json.decodeFromString<List<BeerDto>>(text)

                val batchSize = 50
                dtos.chunked(batchSize).forEach { dtoChunk ->
                    val models = dtoChunk.map { it.toModel() }
                    beerRepository.addBeers(models)
                }

                prefs.edit().putBoolean("beers_seeded", true).apply()
            } catch (e: Exception) {
                Log.e("App", "Seeding failed", e)
            }
        }
    }
}