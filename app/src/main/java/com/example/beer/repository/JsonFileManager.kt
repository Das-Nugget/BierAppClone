package com.example.beer.repository

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import com.example.beer.data.dao.BeerDao
import com.example.beer.data.dto.BeerDto
import com.example.beer.data.dto.toExport
import com.example.beer.data.dto.toModel
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
        ignoreUnknownKeys = true
    }

    data class ImportResult(
        val totalInFile: Int,
        val inserted: Int,
        val ignoredAsDuplicate: Int
    )

    suspend fun importBeersJson(uri: Uri): ImportResult {
        val text = context.contentResolver.openInputStream(uri)
            ?.bufferedReader(Charsets.UTF_8)
            ?.use { it.readText() }
            ?: error("Cannot open input stream for uri=$uri")

        val dtos = json.decodeFromString<List<BeerDto>>(text)
        val models = dtos.map { it.toModel() }

        val batchSize = 50
        var inserted = 0
        var ignored = 0

        models.chunked(batchSize).forEach { chunk ->
            val result = beerDao.insertBeers(chunk)
            val insertedInChunk = result.count { it != -1L }
            inserted += insertedInChunk
            ignored += (chunk.size - insertedInChunk)
        }

        return ImportResult(
            totalInFile = models.size,
            inserted = inserted,
            ignoredAsDuplicate = ignored
        )
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