package ucne.edu.presentation.prioridades

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ucne.edu.data.local.entities.PrioridadEntity
import ucne.edu.data.repository.PrioridadesRepository

class PrioridadesViewModel(
    private val repository: PrioridadesRepository
) : ViewModel() {

    fun savePrioridad(prioridad: PrioridadEntity){
        viewModelScope.launch {
            repository.save(prioridad)
        }
    }

    suspend fun findPrioridad(id: Int): PrioridadEntity?{
        return repository.find(id)
    }

    fun delete(prioridad: PrioridadEntity){
        viewModelScope.launch {
            repository.delete(prioridad)
        }
    }
}