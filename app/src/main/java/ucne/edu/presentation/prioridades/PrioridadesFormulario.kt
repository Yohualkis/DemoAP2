package ucne.edu.presentation.prioridades

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Article
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import ucne.edu.data.local.entities.PrioridadEntity
import ucne.edu.presentation.componentes.MensajeDeErrorGenerico
import ucne.edu.presentation.componentes.TopBarGenerica

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrioridadesFormulario(
    prioridadId: Int? = null,
    viewModel: PrioridadesViewModel? = null,
    navController: NavController,
    modifier: Modifier = Modifier,
) {
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    var descripcion: String by remember { mutableStateOf("") }
    var editandoPrioridad by remember { mutableStateOf<PrioridadEntity?>(null) }
    var errorMessage by remember { mutableStateOf("") }

    LaunchedEffect(prioridadId) {
        if (prioridadId != null && prioridadId > 0) {
            val prioridad = viewModel?.findPrioridad(prioridadId)
            prioridad.let { prioridad ->
                editandoPrioridad = prioridad
                descripcion = prioridad?.descripcion.toString()
            }
        }
    }

    Scaffold(
        topBar = {
            TopBarGenerica(
                titulo = "Atrás",
                navController = navController
            )
        },
    ) { innerPadding ->
        // FORMULARIO
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
                .padding(16.dp)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    focusManager.clearFocus()
                },
        ) {
            Text(
                text = if (prioridadId != null && prioridadId != 0) "Editar prioridad" else "Registrar prioridad",
                fontSize = 24.sp,
                modifier = modifier.align(Alignment.CenterHorizontally),
                fontWeight = FontWeight.Bold,
            )

            Spacer(modifier = Modifier.height(8.dp))

            // CAMPO DESCRIPCION
            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                label = { Text("Descripción") },
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    unfocusedBorderColor = Color.LightGray,
                ),
                isError = descripcion.isBlank() && errorMessage != "",
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Article,
                        contentDescription = "Sueldo por hora"
                    )
                }
            )

            MensajeDeErrorGenerico(errorMessage)

            Spacer(modifier = Modifier.height(8.dp))

            // BOTONES
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Button(
                        onClick = {
                            descripcion = ""
                            errorMessage = ""
                            editandoPrioridad = null
                            focusManager.clearFocus()
                        },
                        modifier = Modifier.padding(4.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color.White,
                            containerColor = Color.Gray
                        ),
                    ) {
                        Icon(
                            imageVector = Icons.Filled.CleaningServices,
                            contentDescription = "Limpiar",
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.width(ButtonDefaults.IconSpacing))
                        Text(
                            text = "Limpiar",
                            fontSize = 16.sp
                        )
                    }
                }

                Column {
                    Button(
                        onClick = {
                            errorMessage = ""
                            if (descripcion.isBlank()) {
                                errorMessage = "Este campo es obligatorio *"
                                return@Button
                            }

                            if (descripcion.length > 16) {
                                errorMessage =
                                    "La descripción no puede tener más de 16 caracteres *"
                                return@Button
                            }
                            viewModel?.savePrioridad(
                                PrioridadEntity(
                                    prioridadId = editandoPrioridad?.prioridadId,
                                    descripcion = descripcion,
                                )
                            )

                            descripcion = ""
                            errorMessage = ""
                            editandoPrioridad = null
                            navController.navigateUp()
                            focusManager.clearFocus()
                        },
                        modifier = Modifier.padding(4.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Save,
                            contentDescription = "Guardar",
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.width(ButtonDefaults.IconSpacing))
                        Text(
                            text = "Guardar",
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Previeww() {
    val navController = rememberNavController()
    PrioridadesFormulario(
        prioridadId = null,
        viewModel = null,
        navController = navController
    )
}
