@file:Suppress("DEPRECATION")

package ucne.edu.presentation.tickets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Article
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PriorityHigh
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Title
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ucne.edu.presentation.UiEvent
import ucne.edu.presentation.componentes.DropdownGenerico
import ucne.edu.presentation.componentes.MensajeDeErrorGenerico
import ucne.edu.presentation.componentes.TopBarGenerica
import java.text.SimpleDateFormat
import java.util.Locale

@Preview(showBackground = true)
@Composable
fun PreviewTicketScreen() {
    TicketFormulario(
        uiState = TicketUiState(),
        onEvent = {},
        goBack = {}
    )
}

@Composable
fun TicketScreen(
    viewModel: TicketsViewModel = hiltViewModel(),
    ticketId: Int?,
    goBack: () -> Unit,
) {
    LaunchedEffect(ticketId) {
        ticketId?.let { id ->
            if (id > 0)
                viewModel.selectedTicket(id)
        }
        viewModel.uiEvent.collect { event ->
            when (event) {
                UiEvent.NavigateUp -> goBack()
            }
        }
    }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    TicketFormulario(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        goBack = goBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicketFormulario(
    uiState: TicketUiState,
    onEvent: (TicketEvent) -> Unit,
    goBack: () -> Unit,
) {
    val dateFormat = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }
    val fechaFormateada = remember(uiState.fecha) { dateFormat.format(uiState.fecha!!) }
    Scaffold(
        topBar = {
            TopBarGenerica(
                titulo = "Atrás",
                goBack = goBack
            )
        },
    ) { innerPadding ->
        val focusRequester = remember { FocusRequester() }
        val focusManager = LocalFocusManager.current
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    focusManager.clearFocus()
                },
        ) {
            Text(
                text = if (uiState.ticketId == 0) "Registrar ticket" else "Editar ticket",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            // CAMPO FECHA
            OutlinedTextField(
                value = fechaFormateada,
                onValueChange = { },
                label = { Text("Fecha") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                readOnly = true,
                enabled = false,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Fecha"
                    )
                }
            )

            // CAMPO NOMBRE CLIENTE
            OutlinedTextField(
                value = uiState.cliente ?: "",
                onValueChange = {
                    onEvent(TicketEvent.LimpiarError)
                    onEvent(TicketEvent.ClienteChange(it))
                },
                label = { Text("Cliente") },
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
                isError = uiState.cliente.isNullOrBlank() && !uiState.errorMessage.isNullOrBlank(),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Cliente"
                    )
                }
            )
            MensajeDeErrorGenerico(uiState.errorMessage)

            // CAMPO ASUNTO
            OutlinedTextField(
                value = uiState.asunto ?: "",
                onValueChange = {
                    onEvent(TicketEvent.LimpiarError)
                    onEvent(TicketEvent.AsuntoChange(it))
                },
                label = { Text("Asunto") },
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
                isError = uiState.asunto.isNullOrBlank() && !uiState.errorMessage.isNullOrBlank(),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Title,
                        contentDescription = "Asunto"
                    )
                }
            )
            MensajeDeErrorGenerico(uiState.errorMessage)

            // CAMPO DESCRIPCION
            OutlinedTextField(
                value = uiState.descripcion ?: "",
                onValueChange = {
                    onEvent(TicketEvent.LimpiarError)
                    onEvent(TicketEvent.DescripcionChange(it))
                },
                label = { Text(text = "Descripción", Modifier.padding(top = 18.dp)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = 100.dp),
                isError = uiState.descripcion.isNullOrBlank() && !uiState.errorMessage.isNullOrBlank(),
                singleLine = false,
                maxLines = 3,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Article,
                        contentDescription = "Descripción"
                    )
                }
            )
            MensajeDeErrorGenerico(uiState.errorMessage)

            // CAMPO PRIORIDAD ID
            DropdownGenerico(
                listaEntidades = uiState.listaPrioridades,
                nombreEntidadSeleccionada = uiState.listaPrioridades.find { it.prioridadId == uiState.prioridadId },
                onEntidadSelected = { prioridad ->
                    onEvent(TicketEvent.GetIdAndDescriptionPrioridad(
                        prioridad.prioridadId,
                        prioridad.descripcion))
                },
                placeholderVacio = "Seleccionar prioridad",
                placeholder = { it.descripcion },
                icono = Icons.Filled.PriorityHigh,
                label = "Prioridades",
                evento = {
                    onEvent(TicketEvent.LimpiarError)
                }
            )
            MensajeDeErrorGenerico(uiState.errorMessage)

            // CAMPO TECNICO ID
            DropdownGenerico(
                listaEntidades = uiState.listaTecnicos,
                nombreEntidadSeleccionada = uiState.listaTecnicos.find { it.tecnicoId == uiState.tecnicoId },
                onEntidadSelected = { tecnico ->
                    onEvent(TicketEvent.GetIdAndDescriptionTecnico(
                        tecnico.tecnicoId,
                        tecnico.nombre))
                },
                placeholderVacio = "Seleccionar técnico",
                placeholder = { it.nombre },
                icono = Icons.Filled.Build,
                label = "Técnico",
                evento = {
                    onEvent(TicketEvent.LimpiarError)
                }
            )
            MensajeDeErrorGenerico(uiState.errorMessage)

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = {
                        onEvent(TicketEvent.Limpiar)
                        focusManager.clearFocus()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                    ),
                    modifier = Modifier.padding(4.dp),
                    shape = RoundedCornerShape(12.dp)
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

                Button(
                    onClick = {
                        onEvent(TicketEvent.Save)
                        focusManager.clearFocus()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
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