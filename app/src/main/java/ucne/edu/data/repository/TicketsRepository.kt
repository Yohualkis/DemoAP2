package ucne.edu.data.repository

import kotlinx.coroutines.flow.Flow
import ucne.edu.data.local.dao.TicketDao
import ucne.edu.data.local.entities.TicketEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TicketsRepository @Inject constructor(
    private val dao: TicketDao
) {
    suspend fun save(ticket: TicketEntity) = dao.save(ticket)
    suspend fun find(id: Int): TicketEntity? = dao.find(id)
    suspend fun delete(ticket: TicketEntity) = dao.delete(ticket)
    fun getAll(): Flow<List<TicketEntity>> = dao.getAll()
}