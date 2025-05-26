package ucne.edu.presentation.tecnicos

import ucne.edu.data.local.entities.TecnicoEntity

data class TecnicosUiState(
    val tecnicoId: Int? = null,
    val nombre: String? = "",
    val sueldoHora: Double? = null,
    val fotoPath: String? = "",
    val errorMessage: String? = "",
    val listaTecnicos: List<TecnicoEntity> = emptyList()
)
