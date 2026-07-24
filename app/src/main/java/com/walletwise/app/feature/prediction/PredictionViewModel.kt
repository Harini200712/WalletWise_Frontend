package com.walletwise.app.feature.prediction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.walletwise.app.core.model.Prediction
import com.walletwise.app.data.repository.WalletRepository
import kotlinx.coroutines.flow.*

data class PredictionUiState(
    val prediction: Prediction? = null,
    val isLoading: Boolean = false
)

class PredictionViewModel(
    private val repository: WalletRepository = WalletRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(PredictionUiState())
    val uiState: StateFlow<PredictionUiState> = _uiState.asStateFlow()

    init {
        repository.prediction.onEach { pred ->
            _uiState.value = PredictionUiState(prediction = pred)
        }.launchIn(viewModelScope)
    }
}
