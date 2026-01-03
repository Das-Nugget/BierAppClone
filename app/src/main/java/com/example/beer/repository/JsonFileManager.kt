package com.example.beer.repository

import android.content.Context
import androidx.core.content.FileProvider
import com.example.beer.data.dao.BeerDao
import com.example.beer.data.dto.toExport
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

@Singleton
class JsonFileManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val beerDao: BeerDao
) {
    private val json = Json {
        prettyPrint = true
        encodeDefaults = false
        explicitNulls = false
    }

    suspend fun exportBeersJson(): android.net.Uri {
        val beers = beerDao.getAllBeers()
            .first()
            .map { it.toExport() }

        val payload = json.encodeToString(beers)

        val outFile = File(context.cacheDir, "beers_export.json")
        outFile.writeText(payload, Charsets.UTF_8)

        return FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            outFile
        )
    }
}