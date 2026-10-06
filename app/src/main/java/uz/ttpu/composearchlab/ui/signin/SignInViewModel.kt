package uz.ttpu.composearchlab.ui.signin



import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SignInViewModel : ViewModel() {

    private val _uiState = mutableStateOf<SignInUiState>(SignInUiState.SignedOut)

    // КОММЕНТАРИЙ TASK 6: _uiState является private и mutable, чтобы изменять состояние мог
    // только ViewModel (инкапсуляция). Публичное свойство uiState доступно только для чтения,
    // что гарантирует, что UI-слой не сможет изменить состояние напрямую.
    val uiState: State<SignInUiState>
        get() = _uiState

    fun onSignIn(email: String, password: String) {
        if (_uiState.value is SignInUiState.InProgress) return

        _uiState.value = SignInUiState.InProgress

        viewModelScope.launch {
            delay(1500)
            if (password == "kotlin123") {
                _uiState.value = SignInUiState.SignedIn(email)
            } else {
                _uiState.value = SignInUiState.Error("Wrong email or password")
            }
        }
    }

    fun onErrorShown() {
        if (_uiState.value is SignInUiState.Error) {
            _uiState.value = SignInUiState.SignedOut
        }
    }
}
