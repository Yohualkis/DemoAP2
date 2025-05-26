@file:Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "UNCHECKED_CAST")

package ucne.edu.presentation.tickets

//noinspection UsingMaterialAndMaterial3Libraries
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.TabRowDefaults.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Comment
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ucne.edu.data.local.entities.TicketEntity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun TicketCardInfo(
    ticket: TicketEntity,
    nombreTecnico: String?,
    descripcionPrioridad: String?,
    onEditClick: (Int?) -> Unit,
    onDeleteClick: () -> Unit,
    onComentariosClick: (Int?, String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp, horizontal = 12.dp),
        colors = CardDefaults.outlinedCardColors(containerColor = Color.White),
        border = CardDefaults.outlinedCardBorder(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Primera fila: ID y Fecha
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "Ticket #${ticket.ticketId}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.Black
                )
                val dateFormat = remember { SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()) }
                val fechaFormateada = remember(ticket.fecha) { dateFormat.format(ticket.fecha) }
                Text(fechaFormateada, fontSize = 14.sp, color = Color.Gray)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row {
                Column {
                    // Cliente
                    Text(
                        "Cliente:",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black
                    )
                    Text(
                        text = ticket.cliente,
                        fontSize = 15.sp,
                        color = Color.Black
                    )
                }
                Spacer(
                    modifier = Modifier.padding(8.dp)
                )
                Column {
                    // Tecnico
                    Text(
                        "Técnico:",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black
                    )
                    Text(
                        text = nombreTecnico ?: "N/D",
                        fontSize = 15.sp,
                        color = Color.Black
                    )
                }
                Spacer(
                    modifier = Modifier.padding(8.dp)
                )
                Column {
                    // Tecnico
                    Text(
                        "Prioridad:",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black
                    )
                    Text(
                        text = descripcionPrioridad ?: "N/D",
                        fontSize = 15.sp,
                        color = Color.Black
                    )
                }

            }

            Spacer(modifier = Modifier.height(8.dp))
            Divider(
                color = Color.LightGray,
                thickness = 1.dp,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {

                    // ASUNTO
                    Text(
                        text = "Asunto:",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = ticket.asunto,
                        fontSize = 14.sp,
                        color = Color.Black
                    )
                }
                Spacer(modifier = Modifier.weight(1f))

                //COMENTARIOS
                IconButton(onClick = {
                    onComentariosClick(ticket.ticketId, ticket.asunto)
                }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Comment,
                        contentDescription = "Comment",
                        tint = Color.Gray
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))

            Row {
                Column {
                    // Descripcion
                    Text(
                        "Descripción:",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = Color.Black
                    )
                    Text(
                        text = (if (expanded || ticket.descripcion.length <= 15)
                            ticket.descripcion
                        else ticket.descripcion.take(
                            15
                        ) + "...").toString(),
                        fontSize = 14.sp,
                        color = Color.DarkGray
                    )
                }

                // Botones
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    IconButton(onClick = { expanded = !expanded }) {
                        @Suppress("DEPRECATION")
                        Icon(
                            imageVector = if (expanded) Icons.Default.KeyboardArrowLeft else Icons.Default.KeyboardArrowRight,
                            contentDescription = if (expanded) "Ver menos" else "Ver más",
                            tint = Color.Gray
                        )
                    }
                    IconButton(onClick = { onEditClick(ticket.ticketId) }) {
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
fun Preview() {
    TicketCardInfo(
        ticket = TicketEntity(
            ticketId = 1,
            prioridadId = 1,
            tecnicoId = 1,
            fecha = Date(),
            cliente = "Cliente",
            asunto = "Asunto",
            descripcion = "Descripcion"
        ),
        nombreTecnico = "Tecnico",
        descripcionPrioridad = "Prioridad",
        onEditClick = { },
        onDeleteClick = { },
        onComentariosClick = {p1, p2 ->} ,
    )
}
