package ucne.edu.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Prioridades")
data class PrioridadEntity (
    @PrimaryKey
    val prioridadId: Int?,
    val descripcion: String,
)