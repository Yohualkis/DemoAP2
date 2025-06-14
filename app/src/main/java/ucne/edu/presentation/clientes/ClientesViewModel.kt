package ucne.edu.presentation.clientes

import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ucne.edu.data.remote.Resource
import ucne.edu.data.remote.dto.ClienteDto
import ucne.edu.data.repository.ClienteRepository
import ucne.edu.presentation.UiEvent
import javax.inject.Inject

@HiltViewModel
class ClientesViewModel @Inject constructor(
    private val repository: ClienteRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(ClientesDtoUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        getClientes()
    }

    fun onEvent(event: ClienteEvent) {
        when (event) {
            is ClienteEvent.PostCliente -> addCliente()
            is ClienteEvent.GetClientes -> getClientes()
            is ClienteEvent.Limpiar -> limpiar()
            is ClienteEvent.NombreChange -> nombreChange(event.nombre)
            is ClienteEvent.TelefonoChange -> telefonoChange(event.telefono)
            ClienteEvent.LimpiarErrorMessageNombre -> limpiarErrorMessageNombre()
            ClienteEvent.LimpiarErrorMessageTelefono -> limpiarErrorMessageTelefono()
        }
    }

    private fun limpiarErrorMessageNombre() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(errorMessageNombre = "")
            }
        }
    }

    private fun limpiarErrorMessageTelefono() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(errorMessageTelefono = "")
            }
        }
    }

    private fun telefonoChange(telefono: String) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(telefono = telefono)
            }
        }
    }

    private fun nombreChange(nombre: String) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(nombre = nombre)
            }
        }
    }

    private fun limpiar() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    nombre = "",
                    telefono = "",
                    errorMessageNombre = "",
                    errorMessageTelefono = "",
                    errorMessage = "",
                )
            }
        }
    }

    private fun addCliente() {
        viewModelScope.launch {
            var error = false
            if (_uiState.value.nombre.isNullOrBlank()) {
                _uiState.update {
                    it.copy(errorMessageNombre = "Este campo es obligatorio *")
                }
                error = true
            }
            if (_uiState.value.telefono.isNullOrBlank()) {
                _uiState.update {
                    it.copy(errorMessageTelefono = "Este campo es obligatorio *")
                }
                error = true
            }
            if (error) return@launch
            if (_uiState.value.nombre!!.length > 20) {
                _uiState.update {
                    it.copy(errorMessageTelefono = "Este campo no puede tener más de 20 caracteres *")
                }
                return@launch
            }
            if (!_uiState.value.telefono!!.isDigitsOnly()) {
                _uiState.update {
                    it.copy(errorMessageTelefono = "Este campo solo puede contener números *")
                }
                return@launch
            }
            if (_uiState.value.telefono!!.length > 10) {
                _uiState.update {
                    it.copy(errorMessageTelefono = "Este campo no puede tener más de 10 caracteres *")
                }
                return@launch
            }
            repository.addCliente(_uiState.value.toEntity())
            getClientes()
            limpiar()
            _uiEvent.send(UiEvent.NavigateUp)
        }
    }

    private fun getClientes() {
        viewModelScope.launch {
            repository.getClientes().collectLatest { result ->
                when (result) {
                    is Resource.Loading -> {
                        _uiState.update {
                            it.copy(isLoading = true)
                        }
                    }

                    is Resource.Success -> {
                        _uiState.update {
                            it.copy(
                                listaClientesDto = result.data ?: emptyList(),
                                isLoading = false
                            )
                        }
                    }

                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(
                                errorMessage = result.message ?: "Error desconocido",
                                isLoading = false
                            )
                        }
                    }
                }
            }
        }
    }

    fun ClientesDtoUiState.toEntity() = ClienteDto(
        clienteId = clienteId,
        nombre = nombre,
        telefono = telefono,
    )
}
