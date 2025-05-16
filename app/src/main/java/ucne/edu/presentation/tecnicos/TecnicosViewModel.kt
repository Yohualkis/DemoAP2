package ucne.edu.presentation.tecnicos

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ucne.edu.data.local.entities.TecnicoEntity
import ucne.edu.data.repository.TecnicosRepository
import java.io.File
import java.io.FileOutputStream

class TecnicosViewModel(
    private val tecnicosRepository: TecnicosRepository,
) : ViewModel() {

    fun saveTecnico(tecnico: TecnicoEntity) {
        viewModelScope.launch {
            tecnicosRepository.save(tecnico)
        }
    }

    suspend fun find(id: Int): TecnicoEntity? {
        return tecnicosRepository.find(id)
    }

    fun deleteTecnico(tecnico: TecnicoEntity) {
        viewModelScope.launch {
            tecnicosRepository.delete(tecnico)
        }
    }

    fun copiarImagenDeExploradorDeArchivos(context: Context, uri: Uri): String {
        val inputStream = context.contentResolver.openInputStream(uri)
        val fileName = "foto_${System.currentTimeMillis()}.jpg"
        val file = File(context.filesDir, fileName)
        val outputStream = FileOutputStream(file)
        inputStream?.copyTo(outputStream)
        inputStream?.close()
        outputStream.close()
        return file.absolutePath
    }

    // ESTO LO USAREMOS MAS TARDE, FAVOR BORRAR ESTE COMENTARIO UNA VEZ QUE SE APLIQUE
//    companion object {
//        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
//            @Suppress("UNCHECKED_CAST")
//            override fun <T : ViewModel> create(
//                modelClass: Class<T>,
//                extras: CreationExtras,
//            ): T {
//                // Get the Application object from extras
//                val application = checkNotNull(extras[APPLICATION_KEY])
//
//
//                return TecnicosViewModel(
//
//                ) as T
//            }
//        }
}