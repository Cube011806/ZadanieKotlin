import com.kk.zadaniekotlin.model.Item

sealed class DashboardUiState {
    data object Loading : DashboardUiState()
    data class Success(val items: List<Item>) : DashboardUiState()
    data object Empty : DashboardUiState()
    data class Error(val exception: Exception) : DashboardUiState()
}
