package ucne.edu.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ucne.edu.data.local.dao.MensajeDao
import ucne.edu.data.local.dao.PrioridadDao
import ucne.edu.data.local.dao.TecnicoDao
import ucne.edu.data.local.dao.TicketDao
import ucne.edu.data.local.entities.MensajeEntity
import ucne.edu.data.local.entities.PrioridadEntity
import ucne.edu.data.local.entities.TecnicoEntity
import ucne.edu.data.local.entities.TicketEntity
import ucne.edu.domain.models.Converters

@Database(
    entities = [
        TecnicoEntity::class,
        PrioridadEntity::class,
        TicketEntity::class,
        MensajeEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class TecnicosDb : RoomDatabase(){
    abstract fun TecnicoDao(): TecnicoDao
    abstract fun TicketDao(): TicketDao
    abstract fun PrioridadDao(): PrioridadDao
    abstract fun MensajeDao(): MensajeDao
}