package ucne.edu.presentation.clientes

import ucne.edu.data.remote.dto.ClienteDto

data class ClientesDtoUiState(
    val clienteId: Int? = null,
    val nombre: String? = "",
    val telefono: String? = "",
    val errorMessageNombre: String? = "",
    val errorMessageTelefono: String? = "",
    val isLoading: Boolean = false,
    val errorMessage: String = "",
    val listaClientesDto: List<ClienteDto> = emptyList()
)
