package ucne.edu.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Tecnicos")
data class TecnicoEntity(
    @PrimaryKey
    val tecnicoId: Int?,
    val nombre: String,
    val sueldoHora: Double,
    val fotoPath: String? = null
)