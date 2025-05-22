package ucne.edu.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import ucne.edu.data.local.entities.TicketEntity

@Dao
interface TicketDao {
    @Upsert
    suspend fun save(ticket: TicketEntity)

    @Query(
        """
            select *
            from Tickets
            where ticketId =:id
            limit 1
        """
    )
    suspend fun find(id: Int): TicketEntity?

    @Delete
    suspend fun delete(ticket: TicketEntity)

    @Query("select * from Tickets")
    fun getAll(): Flow<List<TicketEntity>>
}