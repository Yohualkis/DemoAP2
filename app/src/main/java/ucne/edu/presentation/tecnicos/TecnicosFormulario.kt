package ucne.edu.presentation.tecnicos

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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import ucne.edu.data.local.entities.TecnicoEntity
import ucne.edu.presentation.componentes.MensajeDeErrorGenerico
import ucne.edu.presentation.componentes.TopBarGenerica

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TecnicosFormulario(
    tecnicoId: Int? = null,
    tecnicoViewModel: TecnicosViewModel? = null,
    navController: NavController,
    modifier: Modifier = Modifier,
) {
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    var nombre: String by remember { mutableStateOf("") }
    var sueldo: Double by remember { mutableStateOf(0.0) }
    var editandoTecnico by remember { mutableStateOf<TecnicoEntity?>(null) }
    var errorMessage by remember { mutableStateOf("") }

    LaunchedEffect(tecnicoId) {
        if (tecnicoId != null && tecnicoId > 0) {
            val tecnico = tecnicoViewModel?.find(tecnicoId)
            tecnico?.let {
                editandoTecnico = it
                nombre = it.nombre
                sueldo = it.sueldoHora
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
        Spacer(Modifier.height(128.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .padding(innerPadding)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    focusManager.clearFocus()
                },
        ) {
            Text(
                text = if (tecnicoId != null && tecnicoId != 0) "Editar técnico" else "Registrar técnico",
                fontSize = 24.sp,
                modifier = modifier.align(Alignment.CenterHorizontally),
                fontWeight = FontWeight.Bold,
            )

            Spacer(modifier = Modifier.height(8.dp))

            // CAMPO NOMBRE
            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre del Técnico") },
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    unfocusedBorderColor = Color.LightGray,
                ),
                singleLine = true,
                isError = nombre.isBlank() && errorMessage != "",
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Sueldo por hora"
                    )
                }
            )
            MensajeDeErrorGenerico(errorMessage)

            // CAMPO SUELDO HORA
            OutlinedTextField(
                value = if (sueldo == 0.0 && editandoTecnico == null) "" else sueldo.toString(),
                onValueChange = {
                    sueldo = it.toDoubleOrNull() ?: 0.0
                },
                label = { Text("Sueldo por hora") },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    unfocusedBorderColor = Color.LightGray,
                ),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                isError = sueldo <= 0.0 && errorMessage != "",
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.AttachMoney,
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
                            nombre = ""
                            sueldo = 0.0
                            errorMessage = ""
                            editandoTecnico = null
                            focusManager.clearFocus()
                        },
                        modifier = Modifier.padding(4.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer,
                            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
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
                            if (nombre.isBlank()) {
                                errorMessage = "Este campo es obligatorio *"
                                return@Button
                            }

                            if (nombre.length > 12) {
                                errorMessage =
                                    "El nombre no puede tener más de 12 caracteres *"
                                return@Button
                            }

                            if (sueldo <= 0) {
                                errorMessage =
                                    "Este campo no puede ser menor o igual a 0 *"
                                return@Button
                            }

                            tecnicoViewModel?.saveTecnico(
                                TecnicoEntity(
                                    tecnicoId = editandoTecnico?.tecnicoId,
                                    nombre = nombre,
                                    sueldoHora = sueldo,
                                    fotoPath = editandoTecnico?.fotoPath
                                )
                            )

                            nombre = ""
                            sueldo = 0.0
                            errorMessage = ""
                            editandoTecnico = null
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
fun PreviewTecnicosFormulario() {
    val navController = rememberNavController()
    TecnicosFormulario(
        tecnicoId = null,
        tecnicoViewModel = null,
        navController = navController
    )
}
