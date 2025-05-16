@file:OptIn(ExperimentalMaterial3Api::class)

package ucne.edu

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
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
import ucne.edu.data.repository.TecnicosRepository
import ucne.edu.presentation.navigation.TecnicosNavHost
import ucne.edu.presentation.tecnicos.SimpleTopAppBar
import ucne.edu.presentation.tecnicos.TecnicosViewModel
import ucne.edu.ui.theme.DemoAP2Theme


class MainActivity : ComponentActivity() {
    private lateinit var tecnicoDb: TecnicosDb
    private lateinit var tecnicosRepository: TecnicosRepository
    private lateinit var tecnicosViewModel: TecnicosViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        tecnicoDb = Room.databaseBuilder(
            applicationContext,
            TecnicosDb::class.java,
            "Tecnico.db"
        ).fallbackToDestructiveMigration().build()

        tecnicosRepository = TecnicosRepository(tecnicoDb.TecnicoDao())

        tecnicosViewModel = TecnicosViewModel(tecnicosRepository)

        setContent {
            val lifecycleOwner = LocalLifecycleOwner.current
            val listaTecnicos by tecnicoDb.TecnicoDao().getAll()
                .collectAsStateWithLifecycle(
                    initialValue = emptyList(),
                    lifecycleOwner = lifecycleOwner,
                    minActiveState = Lifecycle.State.STARTED
                )

            DemoAP2Theme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = { SimpleTopAppBar() }
                ) { innerPadding ->
                    Column (
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        val nav = rememberNavController()
                        TecnicosNavHost(
                            navHostController = nav,
                            listaTecnicos = listaTecnicos,
                            viewModel = tecnicosViewModel,
                            navController = nav
                        )
                    }
                }
            }
        }
    }
}