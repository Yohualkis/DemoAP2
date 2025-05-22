package ucne.edu.presentation.componentes

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TopBarHome() {
    TopAppBar(
        title = {
            Text(
                text = "DemoAP2",
                fontWeight = FontWeight.Bold
            )
        }
    )
}