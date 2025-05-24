package ucne.edu.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ucne.edu.data.local.database.TecnicosDb
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule{
    @Provides
    @Singleton
    fun database(@ApplicationContext contexto: Context) =
        Room.databaseBuilder(
            context = contexto,
            klass = TecnicosDb::class.java,
            name = "DemoAP2.db"
        ).fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideTicketDao(demoap2Db: TecnicosDb) = demoap2Db.TicketDao()

    @Provides
    fun provideTecnicoDao(demoap2Db: TecnicosDb) = demoap2Db.TecnicoDao()

    @Provides
    fun providePrioridadDao(demoap2Db: TecnicosDb) = demoap2Db.PrioridadDao()
}