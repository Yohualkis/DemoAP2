package ucne.edu.presentation.tecnicos

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ucne.edu.data.local.entities.TecnicoEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DesplegarListadoDeTecnicos(
    listaTecnicos: List<TecnicoEntity>,
    onEditClick: (Int?) -> Unit,
    onDeleteClick: (TecnicoEntity) -> Unit,
    tecnicoViewModel: TecnicosViewModel?
) {
    Scaffold(

        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    println("Navegando a técnico con ID 0")
                    onEditClick(0)
                }
            ) {
                Icon(Icons.Filled.Add, "Agregar")
            }
        },
    ) { innerPadding ->
        Column {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(innerPadding)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp)
                    ) {
                        Text(
                            text = "Listado de Técnicos",
                            fontSize = 20.sp,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        items(listaTecnicos) { tecnico ->
                            TecnicoCardInfo(
                                tecnico = tecnico,
                                onEditClick = { onEditClick(tecnico.tecnicoId) },
                                onDeleteClick = { onDeleteClick(tecnico) },
                                tecnicoViewModel = tecnicoViewModel
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun Preview(){
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
    DesplegarListadoDeTecnicos(
        listaTecnicos = listaTecnicos,
        onEditClick = {},
        onDeleteClick = {},
        tecnicoViewModel = null
    )
}
