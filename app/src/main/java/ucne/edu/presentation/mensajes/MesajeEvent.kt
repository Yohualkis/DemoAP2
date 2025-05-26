package ucne.edu.presentation.mensajes

import java.util.Date

sealed interface MensajeEvent {
    data class MensajeIdChange(val mensajeId: Int?) : MensajeEvent
    data class TicketIdChange(val ticketId: Int?) : MensajeEvent
    data class AutorNombreChange(val autorNombre: String?) : MensajeEvent
    data class TipoAutorChange(val tipoAutor: String?) : MensajeEvent
    data class ContenidoChange(val contenido: String?) : MensajeEvent
    data class FechaCreacionChange(val fecha: Date): MensajeEvent
    data object Save : MensajeEvent
    data object Delete : MensajeEvent
    data object Limpiar : MensajeEvent
}