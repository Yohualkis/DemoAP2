package ucne.edu.presentation.prioridades

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
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
import ucne.edu.data.local.entities.PrioridadEntity
import ucne.edu.presentation.componentes.TopBarGenerica

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DesplegarListadoDePrioridades(
    listaPrioridades: List<PrioridadEntity>,
    navController: NavController,
    onEditClick: (Int?) -> Unit,
    onDeleteClick: (PrioridadEntity) -> Unit,
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
                titulo = "Listado de Prioridades"
            )
        }
    ) { innerPadding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .padding(innerPadding)
        ) {
            items(listaPrioridades) { prioridad ->
                PrioridadCardInfo(
                    prioridad = prioridad,
                    onEditClick = { onEditClick(prioridad.prioridadId) },
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
    val nav = rememberNavController()
    val listaPrioridades = listOf(
        PrioridadEntity(
            prioridadId = 1,
            descripcion = "Descripcion",
        ),
        PrioridadEntity(
            prioridadId = 2,
            descripcion = "Descripcion",
        ),
        PrioridadEntity(
            prioridadId = 2,
            descripcion = "Descripcion",
        )
    )
    DesplegarListadoDePrioridades(
        listaPrioridades = listaPrioridades,
        navController = nav,
        onEditClick = {},
        onDeleteClick = {},
    )
}
