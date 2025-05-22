package ucne.edu.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import ucne.edu.data.local.entities.PrioridadEntity

@Dao
interface PrioridadDao {
    @Upsert
    suspend fun save(prioridad: PrioridadEntity)

    @Query(
        """
            select *
            from Prioridades
            where prioridadId =:id
            limit 1
        """
    )
    suspend fun find(id: Int): PrioridadEntity?

    @Delete
    suspend fun delete(prioridad: PrioridadEntity)

    @Query("select * from Prioridades")
    fun getALl(): Flow<List<PrioridadEntity>>
}