@file:OptIn(ExperimentalMaterial3Api::class)

package ucne.edu

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import ucne.edu.presentation.componentes.BarraNavegacion
import ucne.edu.presentation.navigation.GeneralNavHost
import ucne.edu.ui.theme.DemoAP2Theme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val nav = rememberNavController()
            DemoAP2Theme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = { BarraNavegacion(nav) },
                ) { innerPadding ->
                    Box (
                        modifier = Modifier.fillMaxSize().padding(innerPadding)
                    ) {
                        GeneralNavHost(
                            navHostController = nav,
                        )
                    }
                }
            }
        }
    }
}