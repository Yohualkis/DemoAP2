package ucne.edu.presentation.tecnicos

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ucne.edu.data.local.entities.TecnicoEntity
import ucne.edu.data.repository.TecnicosRepository
import ucne.edu.presentation.UiEvent
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

@HiltViewModel
class TecnicosViewModel @Inject constructor(
    private val tecnicosRepository: TecnicosRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(TecnicosUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        getTecnicos()
    }

    fun onEvent(event: TecnicoEvent) {
        when (event) {
            is TecnicoEvent.NombreChange -> onNombreChange(event.nombre)
            is TecnicoEvent.SueldoChange -> onSueldoChange(event.sueldo)
            is TecnicoEvent.TecnicoChange -> onTecnicoChange(event.tecnicoId)
            is TecnicoEvent.FotoChange -> {
                val path = importarImagen(event.context, event.uri)
                event.onResultado(path)
            }

            is TecnicoEvent.Delete -> delete(event.tecnico)
            TecnicoEvent.Limpiar -> limpiar()
            TecnicoEvent.Save -> save()
            TecnicoEvent.LimpiarError -> limpiarError()
        }
    }

    private fun limpiarError() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(errorMessage = "")
            }
        }
    }

    private fun getTecnicos() {
        viewModelScope.launch {
            tecnicosRepository.getAll().collect { lista ->
                _uiState.update {
                    it.copy(listaTecnicos = lista)
                }
            }
        }
    }

    private fun limpiar() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    nombre = "",
                    sueldoHora = null,
                    errorMessage = "",
                )
            }
        }
    }

    private fun TecnicosUiState.toEntity() =
        TecnicoEntity(
            tecnicoId = tecnicoId,
            nombre = nombre ?: "",
            sueldoHora = sueldoHora ?: 0.0,
            fotoPath = fotoPath ?: ""
        )

    private fun save() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(errorMessage = null)
            }
            if (
                _uiState.value.nombre.isNullOrBlank() ||
                _uiState.value.sueldoHora.toString().isBlank()
            ) {
                _uiState.update {
                    it.copy(errorMessage = "Este campo es obligatorio *")
                }
                return@launch
            }
            if (_uiState.value.nombre?.length!! > 12) {
                _uiState.update {
                    it.copy(errorMessage = "El nombre no puede tener más de 12 caracteres *")
                }
                return@launch
            }
            if ((_uiState.value.sueldoHora ?: 0.0) <= 0.0) {
                _uiState.update {
                    it.copy(errorMessage = "El sueldo no puede ser menor a 0 *")
                }
                return@launch
            }
            tecnicosRepository.save(_uiState.value.toEntity())
            limpiar()
            _uiEvent.send(UiEvent.NavigateUp)
        }
    }

    fun selectedTecnico(tenicoId: Int) {
        viewModelScope.launch {
            if (tenicoId > 0) {
                val tecnico = tecnicosRepository.find(tenicoId)
                _uiState.update {
                    it.copy(
                        tecnicoId = tecnico?.tecnicoId,
                        nombre = tecnico?.nombre,
                        sueldoHora = tecnico?.sueldoHora,
                        fotoPath = tecnico?.fotoPath,
                    )
                }
            }
        }
    }

    private fun delete(tecnico: TecnicoEntity) {
        viewModelScope.launch {
            tecnicosRepository.delete(tecnico)
            getTecnicos()
        }
    }

    private fun onTecnicoChange(tecnicoId: Int) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(tecnicoId = tecnicoId)
            }
        }
    }

    private fun onSueldoChange(sueldo: Double) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(sueldoHora = sueldo)
            }
        }
    }


    private fun onNombreChange(nombre: String) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(nombre = nombre)
            }
        }
    }

    private fun importarImagen(context: Context, uri: Uri): String {
        val inputStream = context.contentResolver.openInputStream(uri)
        val fileName = "foto_${System.currentTimeMillis()}.jpg"
        val file = File(context.filesDir, fileName)
        val outputStream = FileOutputStream(file)
        inputStream?.copyTo(outputStream)
        inputStream?.close()
        outputStream.close()
        return file.absolutePath
    }


    // ESTO LO USAREMOS MAS TARDE, FAVOR BORRAR ESTE COMENTARIO UNA VEZ QUE SE APLIQUE
//    companion object {
//        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
//            @Suppress("UNCHECKED_CAST")
//            override fun <T : ViewModel> create(
//                modelClass: Class<T>,
//                extras: CreationExtras,
//            ): T {
//                // Get the Application object from extras
//                val application = checkNotNull(extras[APPLICATION_KEY])
//
//
//                return TecnicosViewModel(
//
//                ) as T
//            }
//        }
}