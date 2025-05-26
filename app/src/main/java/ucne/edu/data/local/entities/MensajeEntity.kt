package ucne.edu.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "Mensajes")
data class MensajeEntity(
    @PrimaryKey
    val mensajeId: Int? = null,
    val ticketId: Int,
    val autorNombre: String,
    val tipoAutor: String,
    val contenido: String,
    val fecha: Date = Date()
)