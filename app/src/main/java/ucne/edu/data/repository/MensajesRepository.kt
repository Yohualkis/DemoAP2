package ucne.edu.data.repository

import kotlinx.coroutines.flow.Flow
import ucne.edu.data.local.dao.MensajeDao
import ucne.edu.data.local.entities.MensajeEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MensajeRepository @Inject constructor(
    private var dao: MensajeDao
) {
    suspend fun save(mensaje: MensajeEntity) = dao.save(mensaje)
    suspend fun find(id: Int?): MensajeEntity? = dao.find(id)
    suspend fun delete(mensaje: MensajeEntity) = dao.delete(mensaje)
    fun getAll(): Flow<List<MensajeEntity?>> = dao.getAll()
    fun getMensajesByTicketId(ticketId: Int): Flow<List<MensajeEntity>> = dao.getMensajesByTicketId(ticketId)
}