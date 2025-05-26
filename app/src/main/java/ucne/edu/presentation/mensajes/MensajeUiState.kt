package ucne.edu.presentation.mensajes

import ucne.edu.data.local.entities.MensajeEntity
import java.util.Date

data class MensajeUiState(
    val mensajeId: Int? = null,
    val ticketId: Int = 0,
    val autorNombre: String? = "",
    val autorNombreErrorMensaje: String = "",
    val tipoAutor: String? = "Owner",
    val contenido: String? = "",
    val contenidoErrorMensaje: String = "",
    val mensajes: List<MensajeEntity> = emptyList(),
    val fechaCreacion: Date = Date(),
    val errorMessages: String = "",
    val success: Boolean = false
)
