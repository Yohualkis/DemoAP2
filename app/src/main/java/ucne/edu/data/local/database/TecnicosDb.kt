package ucne.edu.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import ucne.edu.data.local.dao.TecnicoDao
import ucne.edu.data.local.entities.TecnicoEntity

@Database(
    entities = [TecnicoEntity::class],
    version = 3,
    exportSchema = false
)
abstract class TecnicosDb : RoomDatabase(){
    abstract fun TecnicoDao(): TecnicoDao
}