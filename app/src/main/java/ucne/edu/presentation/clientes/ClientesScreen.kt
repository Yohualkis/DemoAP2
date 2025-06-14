package ucne.edu.presentation.clientes

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
import androidx.compose.material.icons.filled.Call
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
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
fun ClientesScreen(
    viewModel: ClientesViewModel = hiltViewModel(),
    goBack: () -> Unit,
) {
    LaunchedEffect(0) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                UiEvent.NavigateUp -> goBack()
            }
        }
    }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    ClienteFormulario(
        uiState = uiState,
        evento = viewModel::onEvent,
        goBack = goBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClienteFormulario(
    uiState: ClientesDtoUiState,
    evento: (ClienteEvent) -> Unit,
    goBack: () -> Unit,
) {
    val buscadorFocus = remember { FocusRequester() }
    val manejadorFocus = LocalFocusManager.current

    Scaffold(
        topBar = {
            TopBarGenerica(
                titulo = "Atrás",
                goBack = goBack
            )
        },
    ) { padding ->
        Spacer(Modifier.height(128.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp)
                .padding(padding)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    manejadorFocus.clearFocus()
                },
        ) {
            Text(
                text = if (uiState.clienteId != null && uiState.clienteId != 0) "Editar cliente" else "Registrar cliente",
                fontSize = 24.sp,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                fontWeight = FontWeight.Bold,
            )

            Spacer(modifier = Modifier.height(6.dp))

            // CAMPO NOMBRE
            OutlinedTextField(
                value = uiState.nombre ?: "",
                onValueChange = {
                    evento(ClienteEvent.LimpiarErrorMessageNombre)
                    evento(ClienteEvent.NombreChange(it))
                },
                label = { Text("Nombre completo") },
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(buscadorFocus),
                singleLine = true,
                isError = uiState.nombre.isNullOrBlank() && !uiState.errorMessageNombre.isNullOrBlank(),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = ""
                    )
                }
            )
            MensajeDeErrorGenerico(uiState.errorMessageNombre)

            // CAMPO TELEFONO
            OutlinedTextField(
                value = uiState.telefono.toString(),
                onValueChange = {
                    evento(ClienteEvent.LimpiarErrorMessageTelefono)
                    evento(ClienteEvent.TelefonoChange(it))
                },
                label = { Text("Teléfono") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                isError = uiState.telefono.isNullOrBlank() && !uiState.errorMessageTelefono.isNullOrBlank(),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Call,
                        contentDescription = ""
                    )
                }
            )

            MensajeDeErrorGenerico(uiState.errorMessageTelefono)

            Spacer(modifier = Modifier.height(8.dp))

            // BOTONES
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Button(
                        onClick = {
                            evento(ClienteEvent.Limpiar)
                            manejadorFocus.clearFocus()
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
                            evento(ClienteEvent.PostCliente)
                            manejadorFocus.clearFocus()
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
fun ClienteFormularioPreview(){

    ClienteFormulario(
        uiState = ClientesDtoUiState(),
        evento = {}
    ) { }
}