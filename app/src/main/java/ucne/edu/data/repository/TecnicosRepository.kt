package ucne.edu.data.repository

import kotlinx.coroutines.flow.Flow
import ucne.edu.data.local.dao.TecnicoDao
import ucne.edu.data.local.entities.TecnicoEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TecnicosRepository @Inject constructor(
    private val dao: TecnicoDao
) {
    suspend fun save(tecnico: TecnicoEntity) = dao.save(tecnico)
    suspend fun find(id: Int?) = dao.find(id)
    suspend fun delete(tecnico: TecnicoEntity) = dao.delete(tecnico)
    fun getAll(): Flow<List<TecnicoEntity>> = dao.getAll()
}