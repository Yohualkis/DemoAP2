package ucne.edu.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import ucne.edu.data.local.entities.PrioridadEntity
import ucne.edu.data.local.entities.TecnicoEntity
import ucne.edu.data.local.entities.TicketEntity
import ucne.edu.presentation.generalComposables.Home
import ucne.edu.presentation.prioridades.DesplegarListadoDePrioridades
import ucne.edu.presentation.prioridades.PrioridadesFormulario
import ucne.edu.presentation.prioridades.PrioridadesViewModel
import ucne.edu.presentation.tecnicos.DesplegarListadoDeTecnicos
import ucne.edu.presentation.tecnicos.TecnicosFormulario
import ucne.edu.presentation.tecnicos.TecnicosViewModel
import ucne.edu.presentation.tickets.DesplegarListadoDeTickets
import ucne.edu.presentation.tickets.TicketsFormulario
import ucne.edu.presentation.tickets.TicketsViewModel

@Composable
fun TecnicosNavHost(
    navHostController: NavHostController,
    navController: NavController,
    listaTecnicos: List<TecnicoEntity>,
    tecnicoViewModel: TecnicosViewModel,
    listaPrioridades: List<PrioridadEntity>,
    prioridadViewModel: PrioridadesViewModel,
    listaTickets: List<TicketEntity>,
    ticketViewModel: TicketsViewModel,
) {
    NavHost(
        navController = navHostController,
        startDestination = Screen.Home
    ) {

        composable<Screen.Home> {
            Home(
                listaTecnicos = listaTecnicos,
                listaTickets = listaTickets,
                listaPrioridades = listaPrioridades
            )
        }

        // TECNICOS
        // Esta lambda viene siendo como un (@page "/pagina")
        composable<Screen.TecnicoList> {
            DesplegarListadoDeTecnicos(
                listaTecnicos = listaTecnicos,
                onEditClick = { tecnicoId ->
                    navHostController.navigate(Screen.Tecnico(tecnicoId))
                },
                onDeleteClick = { tecnico ->
                    tecnicoViewModel.deleteTecnico(tecnico)
                },
                tecnicoViewModel = tecnicoViewModel,
                navController = navController
            )
        }

        composable<Screen.Tecnico> { backStack ->
            val tecnicoId = backStack.toRoute<Screen.Tecnico>().tecnicoId
            TecnicosFormulario(
                tecnicoViewModel = tecnicoViewModel,
                tecnicoId = tecnicoId,
                navController = navController,
            )
        }

        // PRIORIDADES
        composable<Screen.PrioridadesList> {
            DesplegarListadoDePrioridades(
                listaPrioridades = listaPrioridades,
                navController = navController,
                onEditClick = { prioridadId ->
                    navHostController.navigate(Screen.Prioridad(prioridadId))
                },
                onDeleteClick = { prioridad ->
                    prioridadViewModel.delete(prioridad)
                }
            )
        }

        composable<Screen.Prioridad> { backStack ->
            val prioridadId = backStack.toRoute<Screen.Prioridad>().prioridadId
            PrioridadesFormulario(
                prioridadId = prioridadId,
                viewModel = prioridadViewModel,
                navController = navController,
            )
        }

        // TICKETS
        composable<Screen.TicketsList> {
            DesplegarListadoDeTickets(
                listaTickets = listaTickets,
                listaTecnicos = listaTecnicos,
                listaPrioridades = listaPrioridades,
                onEditClick = { ticketId ->
                    navHostController.navigate(Screen.Ticket(ticketId))
                },
                onDeleteClick = { ticket ->
                    ticketViewModel.delete(ticket)
                },
                navController = navController
            )
        }

        composable<Screen.Ticket> { backStack ->
            val ticketId = backStack.toRoute<Screen.Ticket>().ticketId
            TicketsFormulario(
                ticketId = ticketId,
                listaTecnicos = listaTecnicos,
                listaPrioridades = listaPrioridades,
                navController = navController,
                viewModel = ticketViewModel,
            )
        }
    }
}