package ucne.edu.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import ucne.edu.data.local.entities.TecnicoEntity
import ucne.edu.presentation.tecnicos.DesplegarListadoDeTecnicos
import ucne.edu.presentation.tecnicos.TecnicosFormulario
import ucne.edu.presentation.tecnicos.TecnicosViewModel

@Composable
fun TecnicosNavHost(
    navHostController: NavHostController,
    listaTecnicos: List<TecnicoEntity>,
    viewModel: TecnicosViewModel,
    navController: NavController,
) {
    NavHost(
        navController = navHostController,
        startDestination = Screen.TecnicoList
    ) {

        // Esta lambda viene siendo como un (@page "/pagina")
        composable<Screen.TecnicoList> {
            DesplegarListadoDeTecnicos(
                listaTecnicos = listaTecnicos,
                onEditClick = { tecnicoId ->
                    navHostController.navigate(Screen.Tecnico(tecnicoId))
                },
                onDeleteClick = { tecnico ->
                    viewModel.deleteTecnico(tecnico)
                },
                tecnicoViewModel = viewModel,
            )
        }

        composable<Screen.Tecnico> { backStack ->
            val tecnicoId = backStack.toRoute<Screen.Tecnico>().tecnicoId
            println("Entrando a la pantalla individual del tecnico")
            TecnicosFormulario(
                tecnicoViewModel = viewModel,
                tecnicoId = tecnicoId,
                navController = navController,
            )
        }
    }
}