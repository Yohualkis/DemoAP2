package ucne.edu.presentation.tickets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ucne.edu.data.local.entities.TicketEntity
import ucne.edu.data.repository.TicketsRepository


class TicketsViewModel(
    private val repository: TicketsRepository
) : ViewModel() {
    fun save(ticket: TicketEntity){
        viewModelScope.launch {
            repository.save(ticket)
        }
    }

    suspend fun find(id: Int): TicketEntity? {
        return repository.find(id)
    }

    fun delete(ticket: TicketEntity){
        viewModelScope.launch {
            repository.delete(ticket)
        }
    }
}