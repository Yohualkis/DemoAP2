package ucne.edu.presentation.tecnicos

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import ucne.edu.data.local.entities.TecnicoEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TecnicosFormulario(
    tecnicoId: Int? = null,
    tecnicoViewModel: TecnicosViewModel,
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
        if(tecnicoId != null && tecnicoId > 0){
            val tecnico = tecnicoViewModel.find(tecnicoId)
            tecnico?.let {
                editandoTecnico = it
                nombre = it.nombre
                sueldo = it.sueldoHora
            }
        }
    }

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
                Row(
                    modifier = Modifier.fillMaxWidth(),
                ){
                    if (navController != null){
                        IconButton(
                            onClick = { navController.popBackStack() },
                            modifier = Modifier
                        ) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "volver")
                        }
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp)
                    ) {
                        Text(
                            text = "Registro de Técnicos",
                            fontSize = 20.sp,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }

                Spacer(
                    modifier = Modifier.height(8.dp)
                )
                //CAMPO NOMBRE
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre") },
                    modifier = Modifier
                        .fillMaxWidth()
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
                                navController.navigateUp()
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
                                tecnicoViewModel.saveTecnico(
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
                                focusManager.clearFocus()
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
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewTecnicosFormulario() {
    val navController = rememberNavController()
    TecnicosFormulario(
        tecnicoId = null,
        tecnicoViewModel = TODO(),
        navController = navController
    )
}
