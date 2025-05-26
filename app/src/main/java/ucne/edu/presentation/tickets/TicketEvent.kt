package ucne.edu.presentation.tickets

import ucne.edu.data.local.entities.TicketEntity

sealed interface TicketEvent{
    data class TicketChange(val ticketId: Int): TicketEvent
    data class TecnicoIdChange(val tecnicoId: Int): TicketEvent
    data class PrioridadIdChange(val prioridadId: Int): TicketEvent
    data class ClienteChange(val cliente:String): TicketEvent
    data class AsuntoChange(val asunto: String): TicketEvent
    data class DescripcionChange(val descripcion: String): TicketEvent
    data class DescripcionPrioridadChange(val descripcion: String): TicketEvent
    data class NombreTecnicoChange(val nombre: String): TicketEvent
    data class GetIdAndDescriptionPrioridad(val id: Int?, val descripcion: String): TicketEvent
    data class GetIdAndDescriptionTecnico(val id: Int?, val nombre: String): TicketEvent
    data object Save: TicketEvent
    data class Delete(val ticket: TicketEntity): TicketEvent
    data object Limpiar: TicketEvent
    data object LimpiarError: TicketEvent
}