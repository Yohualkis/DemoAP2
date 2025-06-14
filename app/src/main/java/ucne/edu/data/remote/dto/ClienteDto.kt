package ucne.edu.data.remote.dto

import com.squareup.moshi.Json

data class ClienteDto(
    val clienteId: Int?,
    // EL decorador "Json" se usa cuando en la API que se esta llamando, el nombre del campo en la API
    // es diferente al que se tiene en el DTO
    @Json(name = "nombres")
    val nombre: String?,
    @Json(name = "whatsApp")
    val telefono: String?
)
