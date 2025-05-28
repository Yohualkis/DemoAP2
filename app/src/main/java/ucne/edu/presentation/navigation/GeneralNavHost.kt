package ucne.edu.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import ucne.edu.presentation.componentes.Home
import ucne.edu.presentation.mensajes.TicketComentariosScreen
import ucne.edu.presentation.prioridades.DesplegarListadoDePrioridades
import ucne.edu.presentation.prioridades.PrioridadScreen
import ucne.edu.presentation.tecnicos.DesplegarListadoDeTecnicos
import ucne.edu.presentation.tecnicos.TecnicoScreen
import ucne.edu.presentation.tickets.DesplegarListadoDeTickets
import ucne.edu.presentation.tickets.TicketScreen

@Composable
fun GeneralNavHost(
    navHostController: NavHostController,
) {
    NavHost(
        navController = navHostController,
        startDestination = Screen.Home
    ) {

        composable<Screen.Home> {
            Home(nav = navHostController)
        }

        // MENSAJES
        composable<Screen.Mensaje> { backStack ->
            val args = backStack.toRoute<Screen.Mensaje>()
            TicketComentariosScreen(
                ticketId = args.ticketId,
                goBack = {
                    navHostController.navigateUp()
                },
            )
        }

        // TECNICOS
        // Esta lambda viene siendo como un (@page "/pagina")
        composable<Screen.TecnicoList> {
            DesplegarListadoDeTecnicos(
                goToTecnico = { navHostController.navigate(Screen.Tecnico(it)) },
                goBack = { navHostController.navigateUp() }
            )
        }

        composable<Screen.Tecnico> { backStack ->
            val args = backStack.toRoute<Screen.Tecnico>()
            TecnicoScreen(
                tecnicoId = args.tecnicoId,
                goBack = { navHostController.navigateUp() }
            )
        }

        // PRIORIDADES
        composable<Screen.PrioridadesList> {
            DesplegarListadoDePrioridades(
                goToPrioridad = { navHostController.navigate(Screen.Prioridad(it)) },
                goBack = { navHostController.navigateUp() }
            )
        }

        composable<Screen.Prioridad> { backStack ->
            val args = backStack.toRoute<Screen.Prioridad>()
            PrioridadScreen(
                prioridadId = args.prioridadId,
                goBack = { navHostController.navigateUp() }
            )
        }

        // TICKETS
        composable<Screen.TicketsList> {
            DesplegarListadoDeTickets(
                goToTicket = { navHostController.navigate(Screen.Ticket(it)) },
                goBack = { navHostController.navigateUp() },
                onComentariosClick = { ticketId, asunto ->
                    navHostController.navigate(Screen.Mensaje(ticketId, asunto))
                }
            )
        }

        composable<Screen.Ticket> { backStack ->
            val args = backStack.toRoute<Screen.Ticket>()
            TicketScreen(
                goBack = { navHostController.navigateUp() },
                ticketId = args.ticketId,
            )
        }
    }
}