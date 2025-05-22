package ucne.edu.presentation.componentes

//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.BottomNavigation
//noinspection UsingMaterialAndMaterial3Libraries,UsingMaterialAndMaterial3Libraries,UsingMaterialAndMaterial3Libraries,UsingMaterialAndMaterial3Libraries,UsingMaterialAndMaterial3Libraries
import androidx.compose.material.BottomNavigationDefaults
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.BottomNavigationItem
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Icon
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PriorityHigh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import ucne.edu.presentation.navigation.Screen

@Preview(showBackground = true)
@Composable
fun BarraNavegacion(navController: NavController? = null) {
    var selectedItem by remember { mutableIntStateOf(0) }
    val items = listOf("Home","Técnicos", "Prioridades", "Tickets")

    BottomNavigation(
        windowInsets = BottomNavigationDefaults.windowInsets,
    ) {
        BottomNavigationItem(
            icon = { Icon(Icons.Filled.Home, contentDescription = null) },
            label = { Text(items[0]) },
            selected = selectedItem == 0,
            onClick = {
                selectedItem = 0
                navController?.navigate(Screen.Home) {
                    popUpTo(navController.graph.startDestinationId) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        )
        BottomNavigationItem(
            icon = { Icon(Icons.Filled.Build, contentDescription = null) },
            label = { Text(items[1]) },
            selected = selectedItem == 1,
            onClick = {
                selectedItem = 1
                navController?.navigate(Screen.TecnicoList) {
                    popUpTo(navController.graph.startDestinationId) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        )
        BottomNavigationItem(
            icon = { Icon(Icons.Filled.PriorityHigh, contentDescription = null) },
            label = { Text(
                text = items[2],
                fontSize = 11.5.sp
            ) },
            selected = selectedItem == 2,
            onClick = {
                selectedItem = 2
                navController?.navigate(Screen.PrioridadesList) {
                    popUpTo(navController.graph.startDestinationId) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        )
        BottomNavigationItem(
            icon = { Icon(Icons.Filled.ConfirmationNumber, contentDescription = null) },
            label = { Text(items[3]) },
            selected = selectedItem == 3,
            onClick = {
                selectedItem = 3
                navController?.navigate(Screen.TicketsList) {
                    popUpTo(navController.graph.startDestinationId) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        )
    }
}