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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ucne.edu.data.local.entities.PrioridadEntity
import ucne.edu.data.local.entities.TecnicoEntity
import ucne.edu.data.local.entities.TicketEntity
import ucne.edu.presentation.componentes.TopBarGenerica
import java.util.Date

@Composable
fun DesplegarListadoDeTickets(
    viewModel: TicketsViewModel = hiltViewModel(),
    goToTicket: (Int?) -> Unit,
    goBack: () -> Unit,
    onComentariosClick: (Int?, String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    ListadoTickets(
        uiState = uiState,
        goToTicket = { goToTicket(it) },
        onDeleteClick = { viewModel.onEvent(TicketEvent.Delete(it)) },
        goBack = goBack,
        onComentariosClick = onComentariosClick
    )
}

@Composable
fun ListadoTickets(
    uiState: TicketUiState,
    goToTicket: (Int?) -> Unit,
    onDeleteClick: (TicketEntity) -> Unit,
    goBack: () -> Unit,
    onComentariosClick: (Int?, String) -> Unit
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
                goBack = goBack,
                titulo = "Listado de Tickets"
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
        ) {

            items(uiState.listaTickets) { ticket ->
                var nombreTecnico = uiState.listaTecnicos.find {
                    it.tecnicoId == ticket.tecnicoId
                }?.nombre ?: "N/D"

                var descripcionPrioridad = uiState.listaPrioridades.find {
                    it.prioridadId == ticket.prioridadId
                }?.descripcion ?: "N/D"

                TicketCardInfo(
                    ticket = ticket,
                    nombreTecnico = nombreTecnico,
                    descripcionPrioridad = descripcionPrioridad,
                    onEditClick = { goToTicket(ticket.ticketId) },
                    onDeleteClick = { onDeleteClick(ticket) },
                    onComentariosClick = onComentariosClick
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewListado() {
    ListadoTickets(
        uiState = TicketUiState(
            listaTickets = listOf(
                TicketEntity(
                    ticketId = 1,
                    cliente = "Juan",
                    tecnicoId = 1,
                    prioridadId = 1,
                    asunto = "No enciende",
                    descripcion = "La PC no enciende",
                    fecha = Date()
                ),
                TicketEntity(
                    ticketId = 3,
                    cliente = "Pedro",
                    tecnicoId = 1,
                    prioridadId = 1,
                    asunto = "Impresora dañada",
                    descripcion = "La impresora no imprime a color",
                    fecha = Date()
                )
            ),
            listaTecnicos = listOf(
                TecnicoEntity(tecnicoId = 1, nombre = "Pedro", sueldoHora = 2.0)
            ),
            listaPrioridades = listOf(
                PrioridadEntity(prioridadId = 1, descripcion = "Alta")
            )
        ),
        goToTicket = {},
        onDeleteClick = {},
        goBack = {},
        onComentariosClick = {p1, p2 ->}
    )
}