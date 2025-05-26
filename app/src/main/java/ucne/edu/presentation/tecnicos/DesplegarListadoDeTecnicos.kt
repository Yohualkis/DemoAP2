package ucne.edu.presentation.tecnicos

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ucne.edu.data.local.entities.TecnicoEntity
import ucne.edu.presentation.componentes.TopBarGenerica
import ucne.edu.presentation.tickets.TicketEvent

@Composable
fun DesplegarListadoDeTecnicos(
    viewModel: TecnicosViewModel = hiltViewModel(),
    goToTecnico: (Int?) -> Unit,
    goBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    ListadoTecnicos(
        uiState = uiState,
        goToTecnico = { goToTecnico(it) },
        goBack = goBack,
        evento = { viewModel::onEvent } ,
        onDeleteClick = { viewModel.onEvent(TecnicoEvent.Delete(it)) },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListadoTecnicos(
    uiState: TecnicosUiState,
    goToTecnico: (Int?) -> Unit,
    onDeleteClick: (TecnicoEntity) -> Unit,
    goBack: () -> Unit,
    evento: (TicketEvent) -> Unit
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    goToTecnico(0)
                }
            ) {
                Icon(Icons.Filled.Add, "Agregar")
            }
        },
        topBar = {
            TopBarGenerica(
                goBack = goBack,
                titulo = "Listado de Técnicos"
            )
        }
    ) { innerPadding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .padding(innerPadding)
        ) {
            items(uiState.listaTecnicos) { tecnico ->
                TecnicoCardInfo(
                    tecnico = tecnico,
                    onEditClick = { goToTecnico(tecnico.tecnicoId) },
                    onDeleteClick = { onDeleteClick(tecnico) },
                    evento =  { evento }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Preview() {
    ListadoTecnicos(
        uiState = TecnicosUiState(
            listaTecnicos = listOf(
                TecnicoEntity(
                    tecnicoId = 1,
                    nombre = "Juan",
                    sueldoHora = 100.0,
                ),
                TecnicoEntity(
                    tecnicoId = 2,
                    nombre = "Jose",
                    sueldoHora = 200.0
                ),
            )
        ),
        onDeleteClick = {},
        evento = {},
        goToTecnico = {},
        goBack = {},
    )
}
