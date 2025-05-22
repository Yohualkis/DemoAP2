@file:OptIn(ExperimentalMaterial3Api::class)

package ucne.edu

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import ucne.edu.data.local.database.TecnicosDb
import ucne.edu.data.repository.PrioridadesRepository
import ucne.edu.data.repository.TecnicosRepository
import ucne.edu.data.repository.TicketsRepository
import ucne.edu.presentation.componentes.BarraNavegacion
import ucne.edu.presentation.navigation.TecnicosNavHost
import ucne.edu.presentation.prioridades.PrioridadesViewModel
import ucne.edu.presentation.tecnicos.TecnicosViewModel
import ucne.edu.presentation.tickets.TicketsViewModel
import ucne.edu.ui.theme.DemoAP2Theme


class MainActivity : ComponentActivity() {
    private lateinit var tecnicoDb: TecnicosDb
    private lateinit var tecnicosRepository: TecnicosRepository
    private lateinit var prioridadesRepository: PrioridadesRepository
    private lateinit var ticketsRepository: TicketsRepository

    private lateinit var tecnicosViewModel: TecnicosViewModel
    private lateinit var prioridadesViewModel: PrioridadesViewModel
    private lateinit var ticketsViewModel: TicketsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        tecnicoDb = Room.databaseBuilder(
            applicationContext,
            TecnicosDb::class.java,
            "Tecnico.db"
        ).fallbackToDestructiveMigration().build()

        tecnicosRepository = TecnicosRepository(tecnicoDb.TecnicoDao())
        ticketsRepository = TicketsRepository(tecnicoDb.TicketDao())
        prioridadesRepository = PrioridadesRepository(tecnicoDb.PrioridadDao())

        tecnicosViewModel = TecnicosViewModel(tecnicosRepository)
        ticketsViewModel = TicketsViewModel(ticketsRepository)
        prioridadesViewModel = PrioridadesViewModel(prioridadesRepository)

        setContent {
            val nav = rememberNavController()
            val lifecycleOwner = LocalLifecycleOwner.current

            val listaTecnicos by tecnicoDb.TecnicoDao().getAll()
                .collectAsStateWithLifecycle(
                    initialValue = emptyList(),
                    lifecycleOwner = lifecycleOwner,
                    minActiveState = Lifecycle.State.STARTED
                )
            val listaPrioridades by tecnicoDb.PrioridadDao().getALl()
                .collectAsStateWithLifecycle(
                    initialValue = emptyList(),
                    lifecycleOwner = lifecycleOwner,
                    minActiveState = Lifecycle.State.STARTED
                )
            val listaTickets by tecnicoDb.TicketDao().getAll()
                .collectAsStateWithLifecycle(
                    initialValue = emptyList(),
                    lifecycleOwner = lifecycleOwner,
                    minActiveState = Lifecycle.State.STARTED
                )

            DemoAP2Theme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = { BarraNavegacion(nav) },
                ) { innerPadding ->
                    Box (
                        modifier = Modifier.fillMaxSize().padding(innerPadding)
                    ) {
                        TecnicosNavHost(
                            navHostController = nav,
                            listaTecnicos = listaTecnicos,
                            tecnicoViewModel = tecnicosViewModel,
                            navController = nav,
                            listaPrioridades = listaPrioridades,
                            prioridadViewModel = prioridadesViewModel,
                            listaTickets = listaTickets,
                            ticketViewModel = ticketsViewModel,
                        )
                    }
                }
            }
        }
    }
}