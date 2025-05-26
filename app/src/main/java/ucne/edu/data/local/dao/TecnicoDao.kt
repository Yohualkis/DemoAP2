package ucne.edu.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import ucne.edu.data.local.entities.TecnicoEntity

@Dao
interface TecnicoDao {
    @Upsert
    suspend fun save(tecnico: TecnicoEntity)

    @Query(
        """
            select *
            from Tecnicos
            where tecnicoId =:id
            limit 1
        """
    )
    suspend fun find(id: Int?): TecnicoEntity?

    @Delete
    suspend fun delete(tecnico: TecnicoEntity)

    @Query("select * from Tecnicos")
    fun getAll(): Flow<List<TecnicoEntity>>
}