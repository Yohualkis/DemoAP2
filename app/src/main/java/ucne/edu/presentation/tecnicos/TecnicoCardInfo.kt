package ucne.edu.presentation.tecnicos

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import ucne.edu.data.local.entities.TecnicoEntity
import java.io.File

@Composable
fun TecnicoCardInfo(
    tecnico: TecnicoEntity,
    onEditClick: (Int?) -> Unit,
    onDeleteClick: () -> Unit,
    viewModel: TecnicosViewModel?,
) {
    val context = LocalContext.current

    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            if (uri != null) {
                val path = viewModel?.copiarImagenDeExploradorDeArchivos(context, uri)
                val tecnicoActualizado = tecnico.copy(
                    fotoPath = path
                )
                viewModel?.saveTecnico(tecnicoActualizado)
            }
        }
    val imageFile = tecnico.fotoPath?.let { File(it) }
    val hasImage = imageFile != null && imageFile.exists()

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
                // Imagen o icono seleccionable
                Box(
                    modifier = Modifier
                        .size(52.dp) // 2dp más grande que icono (40dp + padding)
                        .clickable { launcher.launch("image/*") },
                    contentAlignment = Alignment.Center
                ) {
                    if (hasImage) {
                        Image(
                            painter = rememberAsyncImagePainter(imageFile),
                            contentDescription = "Foto del técnico",
                            modifier = Modifier
                                .size(52.dp)
                                .clip(CircleShape)
                                .border(2.dp, Color.Gray, CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Seleccionar foto",
                            tint = Color.Gray,
                            modifier = Modifier
                                .size(40.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Info
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = tecnico.nombre,
                        fontSize = 18.sp,
                        color = Color.Black
                    )
                    Text(
                        text = "RD$${"%.2f".format(tecnico.sueldoHora)}   |   ID: ${tecnico.tecnicoId}",
                        fontSize = 14 .sp,
                        color = Color.DarkGray
                    )
                }



                // Botones de acción
                Row {
                    IconButton(onClick = { onEditClick(tecnico.tecnicoId) }) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Editar",
                            tint = Color.Gray
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

@Preview(showBackground = true)
@Composable
fun TecnicoCardInfoPreview() {
    TecnicoCardInfo(
        tecnico = TecnicoEntity(1, "Prueba", 2.0),
        onEditClick = {},
        onDeleteClick = {},
        viewModel = null
    )
}