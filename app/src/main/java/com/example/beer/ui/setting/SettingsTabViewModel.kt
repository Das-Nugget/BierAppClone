package com.example.beer.ui.setting

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.beer.repository.JsonFileManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class SettingsTabViewModel @Inject constructor(
    private val jsonFileManager: JsonFileManager
) : ViewModel() {

    sealed class SettingsEvent {
        data class ExportSuccess(val uri: Uri) : SettingsEvent()
        data class ExportError(val error: Throwable) : SettingsEvent()
    }

    private val _events = MutableSharedFlow<SettingsEvent>()
    val events = _events.asSharedFlow()

    fun export() {
        //startet Hintergrund-Task (Coroutine), damit UI nicht blockiert.
        viewModelScope.launch {
            try {
                //Erfolgreich: VM informiert UI und gibt uri weiter
                //UI führt dann Intent aus
                val uri = jsonFileManager.exportBeersJson()
                _events.emit(SettingsEvent.ExportSuccess(uri))
            } catch (e: Exception) {
                _events.emit(SettingsEvent.ExportError(e))
            }
        }
    }

    fun import() {
        viewModelScope.launch {
        }
    }

    fun infos() {
    }
}