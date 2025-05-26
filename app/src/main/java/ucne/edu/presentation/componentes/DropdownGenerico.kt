@file:OptIn(ExperimentalMaterial3Api::class)

package ucne.edu.presentation.componentes

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ucne.edu.data.local.entities.TecnicoEntity

@Composable
fun <T> DropdownGenerico(
    listaEntidades: List<T>,
    nombreEntidadSeleccionada: T?,
    onEntidadSelected: (T) -> Unit,
    label: String,
    placeholderVacio: String,
    placeholder: (T) -> String,
    icono: ImageVector,
    evento: (T) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    val displayText by remember(nombreEntidadSeleccionada) {
        derivedStateOf { nombreEntidadSeleccionada?.let { placeholder(it) } ?: placeholderVacio }
    }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier.padding(vertical = 8.dp),
    ) {
        OutlinedTextField(
            value = displayText,
            onValueChange = {
                evento
            },
            readOnly = true,
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            leadingIcon = {
                Icon(imageVector = icono, contentDescription = "Icono")
            }
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            listaEntidades.forEach { item ->
                DropdownMenuItem(
                    text = { Text(placeholder(item)) },
                    onClick = {
                        onEntidadSelected(item)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Previe() {
    
    val tt = TecnicoEntity(
        tecnicoId = 1,
        nombre = "Nombre",
        sueldoHora = 500.5,

        )
    
    val listaTecnicos = listOf(
        TecnicoEntity(
            tecnicoId = 1,
            nombre = "Nombre",
            sueldoHora = 500.5,

            ),
        TecnicoEntity(
            tecnicoId = 1,
            nombre = "Nombre",
            sueldoHora = 500.5,
        )
    )

    DropdownGenerico(
        listaEntidades = listaTecnicos,
        nombreEntidadSeleccionada = tt,
        onEntidadSelected = {  },
        label = "Tecnico",
        placeholderVacio = "Seleccionar tecnico",
        placeholder = { "" },
        icono = Icons.Default.Build,
        evento = {}
    )
}