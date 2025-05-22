package ucne.edu.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    tableName = "Tickets",
    foreignKeys = [
        ForeignKey(
            entity = PrioridadEntity::class,
            parentColumns = ["prioridadId"],
            childColumns = ["prioridadId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = TecnicoEntity::class,
            parentColumns = ["tecnicoId"],
            childColumns = ["tecnicoId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["prioridadId"]),
        Index(value = ["tecnicoId"])
    ]
)
data class TicketEntity(
    @PrimaryKey
    val ticketId: Int?,
    val prioridadId: Int?,
    val tecnicoId: Int?,
    val fecha: Date = Date(),
    val cliente: String,
    val asunto: String,
    val descripcion: String,
)