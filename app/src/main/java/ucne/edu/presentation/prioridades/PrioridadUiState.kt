package ucne.edu.presentation.prioridades

import ucne.edu.data.local.entities.PrioridadEntity

data class PrioridadUiState(
    val prioridadId: Int? = null,
    val descripcion: String? = null,
    val errorMessage: String? = null,
    val listaPrioridades: List<PrioridadEntity> = emptyList()
)