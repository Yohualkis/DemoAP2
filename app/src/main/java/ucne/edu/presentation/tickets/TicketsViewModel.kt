package ucne.edu.presentation.tickets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ucne.edu.data.local.entities.TicketEntity
import ucne.edu.data.repository.PrioridadesRepository
import ucne.edu.data.repository.TecnicosRepository
import ucne.edu.data.repository.TicketsRepository
import ucne.edu.presentation.UiEvent
import javax.inject.Inject

@HiltViewModel
class TicketsViewModel @Inject constructor(
    private val ticketsRepository: TicketsRepository,
    private val tecnicosRepository: TecnicosRepository,
    private val prioridadesRepository: PrioridadesRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(TicketUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        getTickets()
        getPrioridades()
        getTecnicos()
    }

    fun onEvent(event: TicketEvent) {
        when (event) {
            is TicketEvent.TicketChange -> onTicketIdChange(event.ticketId)
            is TicketEvent.PrioridadIdChange -> onPrioridadChange(event.prioridadId)
            is TicketEvent.TecnicoIdChange -> onTecnicoChange(event.tecnicoId)
            is TicketEvent.ClienteChange -> onClienteChange(event.cliente)
            is TicketEvent.AsuntoChange -> onAsuntoChange(event.asunto)
            is TicketEvent.DescripcionChange -> onDescripcionChange(event.descripcion)
            is TicketEvent.DescripcionPrioridadChange -> onDescripcionPrioridadChange(event.descripcion)
            is TicketEvent.NombreTecnicoChange -> onNombreTecnicoChange(event.nombre)
            is TicketEvent.GetIdAndDescriptionPrioridad -> onIdAndDescriptionPrioridadChange(event.id, event.descripcion)
            is TicketEvent.GetIdAndDescriptionTecnico -> onIdAndDescriptionTecnicoChange(event.id, event.nombre)

            TicketEvent.Save -> save()
            is TicketEvent.Delete -> delete(event.ticket)
            TicketEvent.Limpiar -> limpiar()
            TicketEvent.LimpiarError -> limpiarError()
        }
    }

    private fun limpiarError() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(errorMessage = "")
            }
        }
    }

    private fun onIdAndDescriptionPrioridadChange(id: Int?, descripcion: String){
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    prioridadId = id,
                    descripcionPrioridad = descripcion
                )
            }
        }
    }

    private fun onIdAndDescriptionTecnicoChange(id: Int?, nombre: String){
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    tecnicoId = id,
                    nombreTecnico = nombre
                )
            }
        }
    }

    fun TicketUiState.toEntity() =
        TicketEntity(
            ticketId = ticketId,
            prioridadId = prioridadId,
            tecnicoId = tecnicoId,
            fecha = fecha,
            cliente = cliente ?: "",
            asunto = asunto ?: "",
            descripcion = descripcion ?: ""
        )

    private fun save() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(errorMessage = "")
            }
            if (
                _uiState.value.cliente.isNullOrBlank() ||
                _uiState.value.asunto.isNullOrBlank() ||
                _uiState.value.descripcion.isNullOrBlank() ||
                (_uiState.value.prioridadId ?: 0) <= 0 ||
                (_uiState.value.tecnicoId ?: 0) <= 0
            ) {
                _uiState.update {
                    it.copy(errorMessage = "Este campo es obligatorio *")
                }
                return@launch
            }
            if(_uiState.value.descripcion?.length!! > 26){
                _uiState.update {
                    it.copy(errorMessage = "La descripción no puede tener más de 26 caracteres *")
                }
                return@launch
            }
            ticketsRepository.save(_uiState.value.toEntity())
            limpiar()
            _uiEvent.send(UiEvent.NavigateUp)
        }
    }

    private fun delete(ticket: TicketEntity) {
        viewModelScope.launch {
            ticketsRepository.delete(ticket)
        }
    }

    private fun getPrioridades(){
        viewModelScope.launch {
            prioridadesRepository.getAll().collect { lista ->
                _uiState.update {
                    it.copy(listaPrioridades = lista)
                }
            }
        }
    }

    private fun getTecnicos(){
        viewModelScope.launch {
            tecnicosRepository.getAll().collect { lista->
                _uiState.update {
                    it.copy(listaTecnicos = lista)
                }
            }
        }
    }

    private fun getTickets() {
        viewModelScope.launch {
            ticketsRepository.getAll().collect { lista ->
                _uiState.update {
                    it.copy(listaTickets = lista)
                }
            }
        }
    }

    private fun limpiar() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    tecnicoId = null,
                    prioridadId = null,
                    cliente = "",
                    asunto = "",
                    descripcion = "",
                    errorMessage = "",
                )
            }
        }
    }

    fun selectedTicket(ticketId: Int) {
        viewModelScope.launch {
            if (ticketId > 0) {
                val ticket = ticketsRepository.find(ticketId)
                _uiState.update {
                    it.copy(
                        ticketId = ticket?.ticketId,
                        tecnicoId = ticket?.tecnicoId,
                        prioridadId = ticket?.prioridadId,
                        fecha = ticket?.fecha,
                        cliente = ticket?.cliente,
                        asunto = ticket?.asunto,
                        descripcion = ticket?.descripcion,
                    )
                }
            }
        }
    }

    private fun onClienteChange(cliente: String) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(cliente = cliente)
            }
        }
    }

    private fun onAsuntoChange(asunto: String) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(asunto = asunto)
            }
        }
    }

    private fun onDescripcionChange(descripcion: String) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(descripcion = descripcion)
            }
        }
    }

    private fun onTecnicoChange(tecnicoId: Int) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(tecnicoId = tecnicoId)
            }
        }
    }

    private fun onPrioridadChange(prioridadId: Int?) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(prioridadId = prioridadId)
            }
        }
    }

    private fun onNombreTecnicoChange(nombre: String?) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(nombreTecnico = nombre)
            }
        }
    }

    private fun onDescripcionPrioridadChange(descripcion: String?) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(descripcionPrioridad = descripcion)
            }
        }
    }

    private fun onTicketIdChange(ticketId: Int) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(ticketId = ticketId)
            }
        }
    }
}