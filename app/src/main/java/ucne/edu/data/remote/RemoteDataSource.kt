package ucne.edu.data.remote

import ucne.edu.data.remote.dto.ClienteDto
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val clienteApi: ClienteApi
){
    suspend fun getClientes() = clienteApi.getClientes()
    suspend fun updateCliente(id: Int) = clienteApi.updateCliente(id)
    suspend fun deleteCliente(id: Int) = clienteApi.deleteCliente(id)
    suspend fun addCliente(clienteDto: ClienteDto) = clienteApi.addCliente(clienteDto)
    suspend fun findClienteById(id: Int) = clienteApi.findClienteById(id)
}