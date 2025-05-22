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
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import ucne.edu.data.local.entities.TecnicoEntity
import ucne.edu.presentation.componentes.TopBarGenerica

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DesplegarListadoDeTecnicos(
    listaTecnicos: List<TecnicoEntity>,
    onEditClick: (Int?) -> Unit,
    onDeleteClick: (TecnicoEntity) -> Unit,
    tecnicoViewModel: TecnicosViewModel?,
    navController: NavController,
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    onEditClick(0)
                }
            ) {
                Icon(Icons.Filled.Add, "Agregar")
            }
        },
        topBar = {
            TopBarGenerica(
                navController = navController,
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
            items(listaTecnicos) { tecnico ->
                TecnicoCardInfo(
                    tecnico = tecnico,
                    onEditClick = { onEditClick(tecnico.tecnicoId) },
                    onDeleteClick = { onDeleteClick(tecnico) },
                    viewModel = tecnicoViewModel
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Preview() {
    val listaTecnicos = listOf(
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
        TecnicoEntity(
            tecnicoId = 2,
            nombre = "Jose",
            sueldoHora = 200.0
        )
    )
    val nav: NavController = rememberNavController()
    DesplegarListadoDeTecnicos(
        listaTecnicos = listaTecnicos,
        onEditClick = {},
        onDeleteClick = {},
        tecnicoViewModel = null,
        navController = nav
    )
}
