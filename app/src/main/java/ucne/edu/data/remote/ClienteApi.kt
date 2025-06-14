package ucne.edu.data.remote

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import ucne.edu.data.remote.dto.ClienteDto

interface ClienteApi {
    @GET("api/Clientes")
    suspend fun getClientes(): List<ClienteDto>

    @PUT("api/Clientes/{id}")
    suspend fun updateCliente(@Path("id") id: Int): ClienteDto

    @POST("api/Clientes")
    suspend fun addCliente(@Body clienteDto: ClienteDto): ClienteDto

    @DELETE("api/Cliente/{id}")
    suspend fun deleteCliente(@Path("id") id: Int): Response<Unit>

    @GET("api/Clientes/{id}")
    suspend fun findClienteById(@Path("id") id: Int): ClienteDto
}