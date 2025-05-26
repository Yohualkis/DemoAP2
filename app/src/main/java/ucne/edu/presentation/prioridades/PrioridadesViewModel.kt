package ucne.edu.presentation.prioridades

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ucne.edu.data.local.entities.PrioridadEntity
import ucne.edu.data.repository.PrioridadesRepository
import ucne.edu.presentation.UiEvent
import javax.inject.Inject

@HiltViewModel
class PrioridadesViewModel @Inject constructor(
    private val repository: PrioridadesRepository
) : ViewModel(){
    private val _uiState = MutableStateFlow(PrioridadUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init{
        getPrioridades()
    }

    fun onEvent(event: PrioridadEvent){
        when(event){
            is PrioridadEvent.PrioridadChange -> onPrioridadChange(event.prioridadId)
            is PrioridadEvent.DescripcionChange -> onDescripcionChange(event.descripcion)

            is PrioridadEvent.Delete -> delete(event.prioridad)
            PrioridadEvent.Limpiar -> limpiar()
            PrioridadEvent.Save -> save()
            PrioridadEvent.LimpiarError -> limpiarError()
        }
    }

    private fun limpiarError() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(errorMessage = "")
            }
        }
    }

    private fun onPrioridadChange(id: Int?){
        viewModelScope.launch {
            _uiState.update {
                it.copy(prioridadId = id)
            }
        }
    }

    private fun onDescripcionChange(decripcion: String){
        viewModelScope.launch {
            _uiState.update {
                it.copy(descripcion = decripcion)
            }
        }
    }

    private fun PrioridadUiState.toEntity() =
        PrioridadEntity(
            prioridadId = prioridadId,
            descripcion = descripcion ?: ""
        )

    private fun save(){
        viewModelScope.launch {
            _uiState.update {
                it.copy(errorMessage = null)
            }
            if(_uiState.value.descripcion.isNullOrBlank()){
                _uiState.update {
                    it.copy(errorMessage = "Este campo es obligatorio *")
                }
                return@launch
            }
            if(_uiState.value.descripcion?.length!! > 16){
                _uiState.update {
                    it.copy(errorMessage = "La descripción no puede tener mas de 16 caracteres *")
                }
                return@launch
            }
            repository.save(_uiState.value.toEntity())
            limpiar()
            _uiEvent.send(UiEvent.NavigateUp)
        }
    }

    private fun limpiar(){
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    descripcion = null,
                    errorMessage = null,
                )
            }
        }
    }

    private fun delete(prioridad: PrioridadEntity){
        viewModelScope.launch {
            repository.delete(prioridad)
        }
    }

    fun selectedPrioridad(prioridadId: Int) {
        viewModelScope.launch {
            if (prioridadId > 0) {
                val prioridad = repository.find(prioridadId)
                _uiState.update {
                    it.copy(
                        prioridadId = prioridad?.prioridadId,
                        descripcion = prioridad?.descripcion
                    )
                }
            }
        }
    }


    private fun getPrioridades(){
        viewModelScope.launch {
            repository.getAll().collect { lista ->
                _uiState.update {
                    it.copy(listaPrioridades = lista)
                }
            }
        }
    }
}