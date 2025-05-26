package ucne.edu.presentation.prioridades

import ucne.edu.data.local.entities.PrioridadEntity

sealed interface PrioridadEvent {
    data class PrioridadChange(val prioridadId: Int?): PrioridadEvent
    data class DescripcionChange(val descripcion: String): PrioridadEvent
    data object Save: PrioridadEvent
    data class Delete(val prioridad: PrioridadEntity): PrioridadEvent
    data object Limpiar: PrioridadEvent
    data object LimpiarError: PrioridadEvent
}