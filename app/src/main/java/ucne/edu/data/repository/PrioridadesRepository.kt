package ucne.edu.data.repository

import kotlinx.coroutines.flow.Flow
import ucne.edu.data.local.dao.PrioridadDao
import ucne.edu.data.local.entities.PrioridadEntity

class PrioridadesRepository(
    private val dao: PrioridadDao
) {
    suspend fun save(prioridad: PrioridadEntity) = dao.save(prioridad)
    suspend fun find(id: Int): PrioridadEntity? = dao.find(id)
    suspend fun delete(prioridad: PrioridadEntity) = dao.delete(prioridad)
    fun getAll(): Flow<List<PrioridadEntity>> = dao.getALl()
}