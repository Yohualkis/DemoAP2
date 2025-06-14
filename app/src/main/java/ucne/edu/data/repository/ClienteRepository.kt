package ucne.edu.data.repository

import coil.network.HttpException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ucne.edu.data.remote.RemoteDataSource
import ucne.edu.data.remote.Resource
import ucne.edu.data.remote.dto.ClienteDto
import javax.inject.Inject

class ClienteRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
) {
    suspend fun getClienteById(id: Int) = remoteDataSource.findClienteById(id)
    suspend fun addCliente(clienteDto: ClienteDto) = remoteDataSource.addCliente(clienteDto)
    suspend fun deleteCliente(id: Int) = remoteDataSource.deleteCliente(id)
    suspend fun updateCliente(id: Int) = remoteDataSource.updateCliente(id)

    fun getClientes(): Flow<Resource<List<ClienteDto>>> = flow {
        try {
            emit(Resource.Loading())
            val listaClientes = remoteDataSource.getClientes()
            emit(Resource.Success(listaClientes))
        } catch (e: HttpException) {
            val errorMessage = e.response.toString()
            emit(Resource.Error("Error de conexión: $errorMessage"))
        } catch (e: Exception) {
            emit(Resource.Error("${e.message}"))
        }
    }
}