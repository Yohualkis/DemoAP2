package ucne.edu.presentation.clientes

sealed interface ClienteEvent {
    data class NombreChange(val nombre: String): ClienteEvent
    data class TelefonoChange(val telefono: String): ClienteEvent

    data object PostCliente: ClienteEvent
    data object GetClientes: ClienteEvent
    data object Limpiar: ClienteEvent
    data object LimpiarErrorMessageNombre: ClienteEvent
    data object LimpiarErrorMessageTelefono: ClienteEvent
}