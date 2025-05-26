package ucne.edu.presentation.prioridades

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
import ucne.edu.data.local.entities.PrioridadEntity
import ucne.edu.presentation.componentes.TopBarGenerica

@Composable
fun DesplegarListadoDePrioridades(
    viewModel: PrioridadesViewModel = hiltViewModel(),
    goToPrioridad: (Int?) -> Unit,
    goBack: () -> Unit
){
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    ListadoPrioridades(
        uiState = uiState,
        goBack = goBack,
        goToTicket = { goToPrioridad(it) },
        onDeleteClick = { viewModel.onEvent(PrioridadEvent.Delete(it)) },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListadoPrioridades(
    uiState: PrioridadUiState,
    goToTicket: (Int?) -> Unit,
    onDeleteClick: (PrioridadEntity) -> Unit,
    goBack: () -> Unit
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    goToTicket(0)
                }
            ) {
                Icon(Icons.Filled.Add, "Agregar")
            }
        },
        topBar = {
            TopBarGenerica(
                titulo = "Listado de Prioridades",
                goBack = goBack
            )
        }
    ) { innerPadding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .padding(innerPadding)
        ) {
            items(uiState.listaPrioridades) { prioridad ->
                PrioridadCardInfo(
                    prioridad = prioridad,
                    onEditClick = { goToTicket(prioridad.prioridadId) },
                    onDeleteClick = { onDeleteClick(prioridad) },
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Preview() {
    ListadoPrioridades(
        uiState = PrioridadUiState(listaPrioridades = listOf(
            PrioridadEntity(
                prioridadId = 1,
                descripcion = "Urgente"
            ),
            PrioridadEntity(
                prioridadId = 2,
                descripcion = "Alta"
            ),
        )),
        goToTicket = {},
        onDeleteClick = {},
        goBack = {},
    )
}
