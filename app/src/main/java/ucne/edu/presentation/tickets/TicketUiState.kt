package ucne.edu.presentation.tickets

import ucne.edu.data.local.entities.PrioridadEntity
import ucne.edu.data.local.entities.TecnicoEntity
import ucne.edu.data.local.entities.TicketEntity
import java.util.Date

data class TicketUiState (
    val ticketId: Int? = null,
    val tecnicoId: Int? = null,
    val prioridadId: Int? = null,
    val fecha: Date? = Date(),
    val cliente: String? = "",
    val asunto: String? = "",
    val descripcion: String? = "",
    val errorMessage: String? = "",
    val nombreTecnico: String? = "",
    val descripcionPrioridad: String? = "",
    val listaTickets: List<TicketEntity> = emptyList(),
    val listaTecnicos: List<TecnicoEntity> = emptyList(),
    val listaPrioridades: List<PrioridadEntity> = emptyList()
)