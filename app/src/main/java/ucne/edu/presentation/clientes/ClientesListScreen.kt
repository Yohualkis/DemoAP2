package ucne.edu.presentation.clientes

import android.widget.Toast
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import ucne.edu.data.remote.dto.ClienteDto
import ucne.edu.presentation.componentes.TopBarGenerica

@Composable
fun ClientesListScreen(
    viewModel: ClientesViewModel = hiltViewModel(),
    nuevoCliente: () -> Unit,
    goBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    ClienteListBody(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        nuevoCliente = nuevoCliente,
        goBack = goBack
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ClienteListBody(
    uiState: ClientesDtoUiState,
    onEvent: (ClienteEvent) -> Unit,
    nuevoCliente: () -> Unit,
    goBack: () -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopBarGenerica(
                goBack = goBack,
                titulo = "Clientes API"
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    nuevoCliente()
                }
            ) {
                Icon(Icons.Filled.Add, "")
            }
        },
    ) { paddingValues ->
        val refreshing = uiState.isLoading
        val pullRefreshState = rememberPullRefreshState(
            refreshing = refreshing,
            onRefresh = { onEvent(ClienteEvent.GetClientes) }
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .pullRefresh(pullRefreshState)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                item {
                    ClienteListHeader()
                    HorizontalDivider()
                }

                items(uiState.listaClientesDto, key = { it.clienteId ?: 0 }) { cliente ->
                    ClienteListItem(cliente)
                }

                item {
                    if (uiState.errorMessage.isNotEmpty()) {
                        ShowErrorMessage(uiState.errorMessage)
                    }
                }
            }

            PullRefreshIndicator(
                refreshing = refreshing,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
    }
}

@Composable
fun ClienteListHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "Cliente", fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(0.10f))
        Text(text = "Teléfono", fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(0.10f))
    }
}

@Composable
fun ClienteListContent(uiState: ClientesDtoUiState) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            if (uiState.isLoading) {
                LoadingIndicator()
            }
        }

        items(uiState.listaClientesDto, key = { it.clienteId ?: 0 }) { cliente ->
            ClienteListItem(cliente)
        }
    }
}

@Composable
fun LoadingIndicator() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
    ) {
        CircularProgressIndicator(modifier = Modifier.padding(16.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClienteListItem(cliente: ClienteDto) {
    val coroutineScope = rememberCoroutineScope()
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { state ->
            if (state == SwipeToDismissBoxValue.EndToStart) {
                coroutineScope.launch {}
                true
            } else false
        }
    )

    SwipeToDismissBox(
        state = dismissState,
        enableDismissFromStartToEnd = false,
        enableDismissFromEndToStart = false,
        backgroundContent = {
            SwipeBackground(dismissState)
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { }
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = cliente.nombre.toString(), modifier = Modifier.weight(0.12f))
            Text(text = cliente.telefono.toString(), modifier = Modifier.weight(0.12f))
        }
        HorizontalDivider(modifier = Modifier.padding(horizontal = 20.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwipeBackground(dismissState: SwipeToDismissBoxState) {
    val color by animateColorAsState(
        when (dismissState.targetValue) {
            SwipeToDismissBoxValue.Settled -> Color.Transparent
            SwipeToDismissBoxValue.EndToStart -> Color.Red
            SwipeToDismissBoxValue.StartToEnd -> Color.Red
        },
        label = "Changing color"
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color, shape = RoundedCornerShape(8.dp))
            .padding(16.dp),
        contentAlignment = Alignment.CenterEnd
    ) {
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = null,
            tint = Color.White
        )
    }
}

@Composable
fun ShowErrorMessage(errorMessage: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
    ) {
        Toast.makeText(LocalContext.current, errorMessage, Toast.LENGTH_LONG).show()
    }
}

@Preview(showBackground = true)
@Composable
fun ClienteListPreview() {
    val list = listOf(
        ClienteDto(
            clienteId = 1,
            nombre = "Juan Perez",
            telefono = "809-123-4567",
        ),
        ClienteDto(
            clienteId = 2,
            nombre = "Maria Perez",
            telefono = "809-300-9212"
        )
    )

    ClienteListBody(
        uiState = ClientesDtoUiState(listaClientesDto = list),
        onEvent = {},
        nuevoCliente = {},
        goBack = {}
    )
}