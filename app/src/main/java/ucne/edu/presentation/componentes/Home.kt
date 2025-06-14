package ucne.edu.presentation.componentes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.PriorityHigh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import ucne.edu.presentation.clientes.ClientesViewModel
import ucne.edu.presentation.navigation.Screen
import ucne.edu.presentation.tickets.TicketsViewModel

@Composable
fun Home(
    ticketsViewModel: TicketsViewModel = hiltViewModel(),
    clienteViewModel: ClientesViewModel = hiltViewModel(),
    nav: NavHostController
) {
    val uiStateTickets by ticketsViewModel.uiState.collectAsStateWithLifecycle()
    val uiStateClientes by clienteViewModel.uiState.collectAsStateWithLifecycle()
    Scaffold(
        topBar = { TopBarHome() }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState()) // Esta linea es la que da el efecto de scroll jevi
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Bienvenido a DemoAP2",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Esta es una aplicación de CRUD's de Programación Aplicada 2.",
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }

            TotalEntidadesCard(
                titulo = "Técnicos:",
                icono = Icons.Filled.Build,
                listaEntidades = uiStateTickets.listaTecnicos,
                goToScreen = {
                    nav.navigate(it)
                },
                screen = Screen.TecnicoList
            )
            TotalEntidadesCard(
                titulo = "Prioridades:",
                icono = Icons.Filled.PriorityHigh,
                listaEntidades = uiStateTickets.listaPrioridades,
                goToScreen = {
                    nav.navigate(it)
                },
                screen = Screen.PrioridadesList
            )
            TotalEntidadesCard(
                titulo = "Tickets:",
                icono = Icons.Filled.ConfirmationNumber,
                listaEntidades = uiStateTickets.listaTickets,
                goToScreen = {
                    nav.navigate(it)
                },
                screen = Screen.TicketsList
            )
            TotalEntidadesCard(
                titulo = "Clientes:",
                icono = Icons.Filled.Groups,
                listaEntidades = uiStateClientes.listaClientesDto,
                goToScreen = {
                    nav.navigate(it)
                },
                screen = Screen.ClientesList
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewHome() {
    Home(
        nav = rememberNavController()
    )
}
