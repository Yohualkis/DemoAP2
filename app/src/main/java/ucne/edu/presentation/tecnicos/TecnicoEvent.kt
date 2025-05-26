package ucne.edu.presentation.tecnicos

import android.content.Context
import android.net.Uri
import ucne.edu.data.local.entities.TecnicoEntity

sealed interface TecnicoEvent {
    data class TecnicoChange(val tecnicoId: Int): TecnicoEvent
    data class NombreChange(val nombre: String): TecnicoEvent
    data class SueldoChange(val sueldo: Double): TecnicoEvent
    data class FotoChange(
        val context: Context,
        val uri: Uri,
        val onResultado: (String) -> Unit
    ): TecnicoEvent

    data class Delete(val tecnico: TecnicoEntity): TecnicoEvent
    data object Save: TecnicoEvent
    data object Limpiar: TecnicoEvent
    data object LimpiarError: TecnicoEvent
}