package ucne.edu.presentation.mensajes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ucne.edu.data.local.entities.MensajeEntity
import ucne.edu.data.repository.MensajeRepository
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class MensajeViewModel @Inject constructor(
    private val mensajeRepository: MensajeRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(MensajeUiState())
    val uiState = _uiState.asStateFlow()

    init {
        getMensajes()
    }

    fun onEvent(event: MensajeEvent) {
        when (event) {
            is MensajeEvent.MensajeIdChange -> onMensajeIdChange(event.mensajeId)
            is MensajeEvent.TicketIdChange -> onTicketIdChange(event.ticketId)
            is MensajeEvent.AutorNombreChange -> onAutorNombreChange(event.autorNombre)
            is MensajeEvent.TipoAutorChange -> onTipoAutorChange(event.tipoAutor)
            is MensajeEvent.ContenidoChange -> onContenidoChange(event.contenido)
            is MensajeEvent.FechaCreacionChange -> onFechaCreacionChange(event.fecha)
            MensajeEvent.Limpiar -> limpiar()
            MensajeEvent.Save -> save()
            MensajeEvent.Delete -> delete()
        }
    }

    private fun onMensajeIdChange(id: Int?) {
        _uiState.update {
            it.copy(
                mensajeId = id
            )
        }
    }

    private fun onTicketIdChange(ticketId: Int?) {
        _uiState.update {
            it.copy(
                ticketId = ticketId ?: 0
            )
        }
        if (ticketId != null) {
            getMensajesByTicketId(ticketId)
        }
    }

    private fun onAutorNombreChange(autorNombre: String?) {
        _uiState.update {
            it.copy(
                autorNombre = autorNombre
            )
        }
    }

    private fun onTipoAutorChange(tipoAutor: String?) {
        _uiState.update {
            it.copy(
                tipoAutor = tipoAutor
            )
        }
    }

    private fun onContenidoChange(contenido: String?) {
        _uiState.update {
            it.copy(
                contenido = contenido
            )
        }
    }

    private fun onFechaCreacionChange(fechaCreacion: Date?) {
        _uiState.update {
            it.copy(
                fechaCreacion = fechaCreacion!!
            )
        }
    }

    private fun limpiar() {
        _uiState.update {
            it.copy(
                autorNombre = "",
                tipoAutor = "Owner",
                contenido = "",
                autorNombreErrorMensaje = "",
                contenidoErrorMensaje = ""
            )
        }
    }

    fun save() {
        viewModelScope.launch {
            var hasError = false

            if (_uiState.value.autorNombre!!.isBlank()) {
                _uiState.update {
                    it.copy(
                        autorNombreErrorMensaje = "Este campo es obligatorio *"
                    )
                }
                hasError = true
            } else {
                _uiState.update {
                    it.copy(
                        autorNombreErrorMensaje = ""
                    )
                }
            }

            if (_uiState.value.contenido!!.isBlank()) {
                _uiState.update {
                    it.copy(
                        contenidoErrorMensaje = "Este campo es obligatorio *"
                    )
                }
                hasError = true
            } else {
                _uiState.update {
                    it.copy(
                        contenidoErrorMensaje = ""
                    )
                }
            }

            if (!hasError) {
                // Si es un mensaje nuevo, asignar fecha actual
                if (_uiState.value.mensajeId == null) {
                    _uiState.update {
                        it.copy(
                            fechaCreacion = Date()
                        )
                    }
                }

                mensajeRepository.save(_uiState.value.toEntity())
                getMensajesByTicketId(_uiState.value.ticketId)
                // Limpiar solo los campos del formulario
                _uiState.update {
                    it.copy(
                        contenido = "",
                        autorNombre = "",
                        fechaCreacion = Date() // Resetear fecha para el próximo mensaje
                    )
                }
            }
        }
    }
    

    fun delete() {
        viewModelScope.launch {
            mensajeRepository.delete(_uiState.value.toEntity())
            getMensajesByTicketId(_uiState.value.ticketId)
        }
    }

    private fun getMensajes() {
        viewModelScope.launch {
            mensajeRepository.getAll().collect { mensajes ->
                _uiState.update {
                    it.copy(
                        mensajes = mensajes.filterNotNull()
                    )
                }
            }
        }
    }

    private fun getMensajesByTicketId(ticketId: Int) {
        viewModelScope.launch {
            mensajeRepository.getMensajesByTicketId(ticketId).collect { mensajes ->
                _uiState.update {
                    it.copy(
                        mensajes = mensajes
                    )
                }
            }
        }
    }

    fun MensajeUiState.toEntity() = MensajeEntity(
        mensajeId = mensajeId,
        ticketId = ticketId,
        autorNombre = autorNombre ?: "",
        tipoAutor = tipoAutor ?: "Owner",
        contenido = contenido ?: "",
        fecha = fechaCreacion
    )
}