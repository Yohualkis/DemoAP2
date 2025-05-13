@file:OptIn(ExperimentalMaterial3Api::class)

package ucne.edu

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.room.Room
import kotlinx.coroutines.launch
import ucne.edu.data.local.database.TecnicosDb
import ucne.edu.data.local.entities.TecnicoEntity
import ucne.edu.ui.theme.DemoAP2Theme


class MainActivity : ComponentActivity() {
    private lateinit var tecnicoDb: TecnicosDb

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        tecnicoDb = Room.databaseBuilder(
            applicationContext,
            TecnicosDb::class.java,
            "Tecnico.db"
        ).fallbackToDestructiveMigration().build()

        setContent {
            DemoAP2Theme {
                AppFrontEnd(
                    contexto = tecnicoDb
                )

            }
        }
    }
}

@Composable
fun AppFrontEnd(modifier: Modifier = Modifier, contexto: TecnicosDb) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { SimpleTopAppBar() }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            FormularioTecnicos(
                contextoDb = contexto,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

suspend fun saveTecnico(contextoDb: TecnicosDb, tecnico: TecnicoEntity) {
    contextoDb.TecnicoDao().save(tecnico)
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SimpleTopAppBar() {
    TopAppBar(
        title = { Text("DemoAP2") },
        navigationIcon = {
            IconButton(onClick = { /* doSomething() */ }) {
                Icon(Icons.Filled.Menu, contentDescription = null)
            }
        },
        actions = {
            // RowScope here, so these icons will be placed horizontally
            IconButton(onClick = { /* doSomething() */ }, enabled = false) {
                Icon(Icons.Filled.Person, contentDescription = "")
            }
        }
    )
}

@Composable
fun FormularioTecnicos(
    contextoDb: TecnicosDb,
    modifier: Modifier = Modifier,
) {
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    var nombre by remember { mutableStateOf("") }
    var sueldo by remember { mutableStateOf(0.0) }
    var editandoTecnico by remember { mutableStateOf<TecnicoEntity?>(null) }
    var errorMessage by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    val listaTecnicos by contextoDb.TecnicoDao().getAll()
        .collectAsStateWithLifecycle(initialValue = emptyList())

    //FORMULARIO
    Column(
        modifier = Modifier
            .fillMaxSize()
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                focusManager.clearFocus()
            }
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Registro de Técnicos",
                    fontSize = 20.sp,
                    modifier = Modifier
                        .padding(horizontal = 54.dp)
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )
                //CAMPO NOMBRE
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre") },
                    modifier = Modifier.fillMaxWidth()
                        .focusRequester(focusRequester),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        unfocusedBorderColor = Color.LightGray,
                    )
                )

                Spacer(
                    modifier = Modifier.padding(2.dp)
                )

                if (errorMessage.isNotEmpty() && errorMessage == "Este campo es obligatorio *") {
                    Text(
                        text = errorMessage,
                        color = Color.Red,
                        fontSize = 8.sp
                    )
                }

                Spacer(
                    modifier = Modifier.height(2.dp)
                )

                //CAMPO SUELDO HORA
                OutlinedTextField(
                    value = sueldo.toString(),
                    onValueChange = { sueldo = it.toDoubleOrNull() ?: 0.0 },
                    label = { Text("Sueldo p/h") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        unfocusedBorderColor = Color.LightGray,
                    )
                )
                Spacer(
                    modifier = Modifier.padding(2.dp)
                )

                errorMessage?.let {
                    Text(
                        text = it,
                        color = Color.Red,
                        fontSize = 8.sp
                    )
                }

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                //BOTONES
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        // BTN LIMPIAR
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
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = Color.White,
                                containerColor = Color.Gray
                            )
                        ) {
                            Icon(
                                Icons.Default.Refresh,
                                contentDescription = "Limpiar",
                                modifier = Modifier.padding(end = 4.dp)
                            )
                            Text(text = "Limpiar")
                        }
                    }
                    Column {
                        //BTN AGREGAR
                        Button(
                            onClick = {
                                errorMessage = ""
                                if (nombre.isBlank()) {
                                    errorMessage = "Este campo es obligatorio *"
                                    return@Button
                                }

                                if (nombre.length > 12) {
                                    errorMessage = "El nombre no puede tener más de 12 caracteres *"
                                    return@Button
                                }

                                if (sueldo <= 0) {
                                    errorMessage = "Este campo no puede ser menor o igual a 0 *"
                                    return@Button
                                }
                                scope.launch {
                                    saveTecnico(
                                        contextoDb,
                                        TecnicoEntity(
                                            tecnicoId = editandoTecnico?.tecnicoId,
                                            nombre = nombre,
                                            sueldoHora = sueldo
                                        )
                                    )
                                    nombre = ""
                                    sueldo = 0.0
                                    errorMessage = ""
                                    editandoTecnico = null
                                    focusManager.clearFocus()
                                }
                            },
                            modifier = Modifier.padding(4.dp),
                            shape = RoundedCornerShape(12.dp),

                            ) {
                            Icon(
                                Icons.Default.Add,
                                contentDescription = "Agregar",
                                modifier = Modifier.padding(end = 4.dp)
                            )
                            Text(text = "Agregar")
                        }
                    }
                }
            }
        }

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        DesplegarListadoDeTecnicos(
            listaTecnicos = listaTecnicos,
            onEditClick = { tecnico ->
                editandoTecnico = tecnico
                nombre = tecnico.nombre
                sueldo = tecnico.sueldoHora
                focusRequester.requestFocus()
            },
            onDeleteClick = { tecnico ->
                scope.launch {
                    contextoDb?.TecnicoDao()?.delete(tecnico)
                }
            },
        )
    }
}

@Composable
fun DesplegarListadoDeTecnicos(
    listaTecnicos: List<TecnicoEntity>,
    onEditClick: (TecnicoEntity) -> Unit,
    onDeleteClick: (TecnicoEntity) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Listado de Técnicos",
                    fontSize = 20.sp,
                    modifier = Modifier.padding(horizontal = 54.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    items(listaTecnicos) { tecnico ->
                        TecnicoCardInfo(
                            tecnico = tecnico,
                            onEditClick = { onEditClick(tecnico) },
                            onDeleteClick = { onDeleteClick(tecnico) },
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun TecnicoCardInfo(
    tecnico: TecnicoEntity,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
) {
    val focusManager = LocalFocusManager.current
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        colors = CardDefaults.outlinedCardColors(containerColor = Color.White),
        border = CardDefaults.outlinedCardBorder()

    ) {
        Column {
            Row(
                modifier = Modifier
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Columna Izquierda: Icono + Info
                Row {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Técnico",
                        tint = Color.Gray,
                        modifier = Modifier
                            .padding(end = 12.dp)
                            .size(40.dp)
                    )

                    Column {
                        Text(
                            text = tecnico.nombre,
                            fontSize = 18.sp,
                            color = Color.Black
                        )
                        Text(
                            text = "RD$${"%.2f".format(tecnico.sueldoHora)}",
                            fontSize = 14.sp,
                            color = Color.DarkGray
                        )
                    }
                }

                // Columna Derecha: Botones
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentWidth(Alignment.End)
                ){
                    Row {
                        IconButton(onClick = {
                            onEditClick()
                        }) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Editar",
                                tint = Color.DarkGray
                            )
                        }
                        IconButton(onClick = { onDeleteClick() }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Eliminar",
                                tint = Color.Red
                            )
                        }
                    }
                }
            }
        }
    }
}
