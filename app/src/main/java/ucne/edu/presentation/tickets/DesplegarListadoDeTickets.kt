package ucne.edu.presentation.tickets

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import ucne.edu.data.local.entities.TecnicoEntity
import ucne.edu.data.local.entities.TicketEntity
import ucne.edu.presentation.componentes.TopBarGenerica
import java.util.Date

@Composable
fun DesplegarListadoDeTickets(
    listaTickets: List<TicketEntity>,
    listaTecnicos: List<TecnicoEntity>,
    listaPrioridades: List<PrioridadEntity>,
    onEditClick: (Int?) -> Unit,
    onDeleteClick: (TicketEntity) -> Unit,
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
                titulo = "Listado de Tickets"
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
        ) {

            items(listaTickets, key = { it.ticketId!! } ) { ticket ->
                val nombreTecnico = listaTecnicos.find {
                    it.tecnicoId == ticket.tecnicoId
                }?.nombre ?: "N/D"
                val descripcionPrioridad = listaPrioridades.find {
                    it.prioridadId == ticket.prioridadId
                }?.descripcion ?: "N/D"
                TicketCardInfo(
                    ticketId = ticket.ticketId,
                    nombreCliente = ticket.cliente,
                    fecha = ticket.fecha,
                    asunto = ticket.asunto,
                    descripcion = ticket.descripcion,
                    nombreTecnico = nombreTecnico,
                    descripcionPrioridad = descripcionPrioridad,
                    onEditClick = {
                        onEditClick(ticket.ticketId)
                    },
                    onDeleteClick = {
                        onDeleteClick(ticket)
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Previewww(){
    val listaTickets: List<TicketEntity> = listOf(
        TicketEntity(
            ticketId = 1,
            prioridadId = 1,
            tecnicoId = 1,
            fecha = Date(),
            cliente = "Yohualkis",
            asunto = "Asunto",
            descripcion = "Descripcion"
        ),
        TicketEntity(
            ticketId = 2,
            prioridadId = 2,
            tecnicoId = 2,
            fecha = Date(),
            cliente = "Jerony",
            asunto = "Asunto",
            descripcion = "Descripcion"
        )

    )
    val listaTecnicos: List<TecnicoEntity> = emptyList()
    val listaPrioridades: List<PrioridadEntity> = emptyList()
    DesplegarListadoDeTickets(
        listaTickets = listaTickets,
        listaTecnicos = listaTecnicos,
        listaPrioridades = listaPrioridades,
        navController = rememberNavController(),
        onEditClick = {  },
        onDeleteClick = {  }
    )
}