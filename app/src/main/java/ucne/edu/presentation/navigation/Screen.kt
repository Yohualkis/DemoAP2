package ucne.edu.presentation.navigation

import kotlinx.serialization.Serializable

sealed class Screen {
    @Serializable
    data object Home: Screen()

    @Serializable
    data object TecnicoList: Screen()

    @Serializable
    data class Tecnico(val tecnicoId: Int?): Screen()

    @Serializable
    data object PrioridadesList: Screen()

    @Serializable
    data class Prioridad(val prioridadId: Int?): Screen()

    @Serializable
    data object TicketsList: Screen()

    @Serializable
    data class Ticket(val ticketId: Int?): Screen()
}