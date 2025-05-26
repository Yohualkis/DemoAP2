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
import androidx.compose.runtime.remember
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ucne.edu.presentation.UiEvent
import ucne.edu.presentation.componentes.MensajeDeErrorGenerico
import ucne.edu.presentation.componentes.TopBarGenerica

@Composable
fun TecnicoScreen(
    viewModel: TecnicosViewModel = hiltViewModel(),
    tecnicoId: Int?,
    goBack: () -> Unit,
) {
    LaunchedEffect(tecnicoId) {
        tecnicoId?.let {
            viewModel.selectedTecnico(tecnicoId)
        }
        viewModel.uiEvent.collect { event ->
            when (event) {
                UiEvent.NavigateUp -> goBack()
            }
        }
    }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    TecnicosFormulario(
        uiState = uiState,
        evento = viewModel::onEvent,
        goBack = goBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TecnicosFormulario(
    uiState: TecnicosUiState,
    evento: (TecnicoEvent) -> Unit,
    goBack: () -> Unit,
) {
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    Scaffold(
        topBar = {
            TopBarGenerica(
                titulo = "Atrás",
                goBack = goBack
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
                text = if (uiState.tecnicoId != null && uiState.tecnicoId != 0) "Editar técnico" else "Registrar técnico",
                fontSize = 24.sp,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                fontWeight = FontWeight.Bold,
            )

            Spacer(modifier = Modifier.height(8.dp))

            // CAMPO NOMBRE
            OutlinedTextField(
                value = uiState.nombre ?: "",
                onValueChange = {
                    evento(TecnicoEvent.LimpiarError)
                    evento(TecnicoEvent.NombreChange(it))
                },
                label = { Text("Nombre del Técnico") },
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    unfocusedBorderColor = Color.LightGray,
                ),
                singleLine = true,
                isError = uiState.nombre.isNullOrBlank() && !uiState.errorMessage.isNullOrBlank(),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Sueldo por hora"
                    )
                }
            )
            MensajeDeErrorGenerico(uiState.errorMessage)

            // CAMPO SUELDO HORA
            OutlinedTextField(
                value = uiState.sueldoHora?.takeIf { it > 0.0 }?.toString() ?: "",
                onValueChange = {
                    evento(TecnicoEvent.LimpiarError)
                    val sueldo = it.toDoubleOrNull()
                    if (sueldo != null) {
                        evento(TecnicoEvent.SueldoChange(sueldo))
                    }
                },
                label = { Text("Sueldo por hora") },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    unfocusedBorderColor = Color.LightGray,
                ),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                isError = (uiState.sueldoHora
                    ?: 0.0) <= 0.0 && !uiState.errorMessage.isNullOrBlank(),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.AttachMoney,
                        contentDescription = "Sueldo por hora"
                    )
                }
            )

            MensajeDeErrorGenerico(uiState.errorMessage)

            Spacer(modifier = Modifier.height(8.dp))

            // BOTONES
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Button(
                        onClick = {
                            evento(TecnicoEvent.Limpiar)
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
                            evento(TecnicoEvent.Save)
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
    TecnicosFormulario(
        uiState = TecnicosUiState(
            tecnicoId = 1,
            nombre = "Tecnico",
            sueldoHora = 100.0,
        ),
        evento = {},
        goBack = {}
    )
}
