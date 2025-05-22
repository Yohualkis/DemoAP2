@file:Suppress("DEPRECATION")

package ucne.edu.presentation.tickets

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
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import ucne.edu.data.local.entities.PrioridadEntity
import ucne.edu.data.local.entities.TecnicoEntity
import ucne.edu.data.local.entities.TicketEntity
import ucne.edu.presentation.componentes.MensajeDeErrorGenerico
import ucne.edu.presentation.componentes.TopBarGenerica
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicketsFormulario(
    ticketId: Int? = null,
    listaTecnicos: List<TecnicoEntity?>,
    listaPrioridades: List<PrioridadEntity?>,
    navController: NavController,
    viewModel: TicketsViewModel?,
) {
    var asunto by remember { mutableStateOf("") }
    var cliente by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var fechaTicket: Date by remember { mutableStateOf(Date()) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var editandoTicket by remember { mutableStateOf<TicketEntity?>(null) }

    var tecnicoSeleccionado by remember { mutableStateOf<TecnicoEntity?>(null) }
    var prioridadSeleccionada by remember { mutableStateOf<PrioridadEntity?>(null) }

    var expandedTecnico by remember { mutableStateOf(false) }
    var expandedPrioridad by remember { mutableStateOf(false) }

    val dateFormat = remember {
        SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    }
    val fechaFormateada = remember(fechaTicket) {
        dateFormat.format(fechaTicket)
    }

    LaunchedEffect(ticketId) {
        if (ticketId != null && ticketId > 0) {
            val ticket = viewModel?.find(ticketId)
            ticket?.let {
                asunto = it.asunto
                cliente = it.cliente
                descripcion = it.descripcion
                tecnicoSeleccionado = listaTecnicos.find { t ->
                    t?.tecnicoId == it.tecnicoId
                }
                prioridadSeleccionada = listaPrioridades.find { p ->
                    p?.prioridadId == it.prioridadId
                }
                fechaTicket = it.fecha
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
                text = if (ticketId != null && ticketId != 0) "Editar ticket" else "Registrar ticket",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

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

            OutlinedTextField(
                value = cliente,
                onValueChange = { cliente = it },
                label = { Text("Cliente") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .focusRequester(focusRequester),
                isError = cliente.isBlank() && errorMessage != null,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Cliente"
                    )
                }
            )
            MensajeDeErrorGenerico(errorMessage)

            OutlinedTextField(
                value = asunto,
                onValueChange = { asunto = it },
                label = { Text("Asunto") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .focusRequester(focusRequester),
                isError = asunto.isBlank() && errorMessage != null,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Title,
                        contentDescription = "Asunto"
                    )
                }
            )
            MensajeDeErrorGenerico(errorMessage)

            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                label = {
                    Text(
                        text = "Descripción",
                        modifier = Modifier.padding(top = 38.dp)
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                minLines = 5,
                isError = descripcion.isBlank() && errorMessage != null,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Article,
                        contentDescription = "Descripicon"
                    )
                }
            )
            MensajeDeErrorGenerico(errorMessage)

            ExposedDropdownMenuBox(
                expanded = expandedTecnico,
                onExpandedChange = { expandedTecnico = !expandedTecnico },
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                OutlinedTextField(
                    value = tecnicoSeleccionado?.nombre ?: "Seleccionar técnico",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Técnico") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedTecnico) },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                        .focusRequester(focusRequester),
                    isError = tecnicoSeleccionado == null && errorMessage != null,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Filled.Build,
                            contentDescription = "Tecnicos"
                        )
                    }
                )
                ExposedDropdownMenu(
                    expanded = expandedTecnico,
                    onDismissRequest = { expandedTecnico = false }
                ) {
                    listaTecnicos.forEach { tecnico ->
                        DropdownMenuItem(
                            text = { Text(text = "${tecnico?.nombre}") },
                            onClick = {
                                tecnicoSeleccionado = tecnico
                                expandedTecnico = false
                            }
                        )
                    }
                }
            }
            MensajeDeErrorGenerico(errorMessage)

            ExposedDropdownMenuBox(
                expanded = expandedPrioridad,
                onExpandedChange = { expandedPrioridad = !expandedPrioridad },
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                OutlinedTextField(
                    value = prioridadSeleccionada?.descripcion
                        ?: "Seleccionar prioridad",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Prioridad") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedPrioridad) },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                        .focusRequester(focusRequester),
                    isError = prioridadSeleccionada == null && errorMessage != null,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Filled.PriorityHigh,
                            contentDescription = "Prioridad"
                        )
                    }
                )
                ExposedDropdownMenu(
                    expanded = expandedPrioridad,
                    onDismissRequest = { expandedPrioridad = false }
                ) {
                    listaPrioridades.forEach { prioridad ->
                        DropdownMenuItem(
                            text = { Text(text = "${prioridad?.descripcion}") },
                            onClick = {
                                prioridadSeleccionada = prioridad
                                expandedPrioridad = false
                            }
                        )
                    }
                }
            }
            MensajeDeErrorGenerico(errorMessage)

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = {
                        asunto = ""
                        cliente = ""
                        descripcion = ""
                        tecnicoSeleccionado = null
                        prioridadSeleccionada = null
                        errorMessage = null
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
                        when {
                            asunto.isBlank() -> {
                                errorMessage = "Este campo es obligatorio *"
                                return@Button
                            }

                            cliente.isBlank() -> {
                                errorMessage = "Este campo es obligatorio *"
                                return@Button
                            }

                            descripcion.isBlank() -> {
                                errorMessage = "Este campo es obligatorio *"
                                return@Button
                            }

                            descripcion.length > 36 -> {
                                errorMessage =
                                    "La descripción no puede tener mas de 36 caracteres."
                                return@Button
                            }

                            tecnicoSeleccionado == null -> {
                                errorMessage = "Este campo es obligatorio *"
                                return@Button
                            }

                            prioridadSeleccionada == null -> {
                                errorMessage = "Este campo es obligatorio *"
                                return@Button
                            }
                        }

                        // Guardar
                        viewModel?.save(
                            TicketEntity(
                                fecha = fechaTicket,
                                prioridadId = prioridadSeleccionada!!.prioridadId,
                                cliente = cliente,
                                asunto = asunto,
                                descripcion = descripcion,
                                tecnicoId = tecnicoSeleccionado!!.tecnicoId,
                                ticketId = editandoTicket?.ticketId
                            )
                        )
                        focusManager.clearFocus()
                        navController.navigateUp()
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

@Preview(showBackground = true)
@Composable
fun Previeww() {
    val listaTecnicos = listOf(
        TecnicoEntity(tecnicoId = 1, nombre = "Juan Pérez", sueldoHora = 300.0),
        TecnicoEntity(tecnicoId = 2, nombre = "Ana Gómez", sueldoHora = 280.0)
    )

    val listaPrioridades = listOf(
        PrioridadEntity(prioridadId = 1, descripcion = "Alta"),
        PrioridadEntity(prioridadId = 2, descripcion = "Media"),
        PrioridadEntity(prioridadId = 3, descripcion = "Baja")
    )
    val nav = rememberNavController()
    TicketsFormulario(
        ticketId = null,
        listaTecnicos = listaTecnicos,
        listaPrioridades = listaPrioridades,
        navController = nav,
        viewModel = null
    )
}