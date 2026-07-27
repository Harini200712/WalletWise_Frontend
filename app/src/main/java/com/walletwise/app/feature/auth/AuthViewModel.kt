package com.walletwise.app.feature.auth

import androidx.lifecycle.ViewModel
import com.walletwise.app.core.ui.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AuthViewModel : ViewModel() {

    // Login Form State
    val loginEmail = MutableStateFlow("")
    val loginPassword = MutableStateFlow("")
    val rememberMe = MutableStateFlow(true)
    val loginEmailError = MutableStateFlow<String?>(null)
    val loginPasswordError = MutableStateFlow<String?>(null)

    private val _loginState = MutableStateFlow<UiState<Boolean>>(UiState.Empty)
    val loginState: StateFlow<UiState<Boolean>> = _loginState.asStateFlow()

    // Register Form State
    val regFullName = MutableStateFlow("")
    val regEmail = MutableStateFlow("")
    val regPhone = MutableStateFlow("")
    val regPassword = MutableStateFlow("")
    val regConfirmPassword = MutableStateFlow("")
    
    val regEmailError = MutableStateFlow<String?>(null)
    val regPasswordError = MutableStateFlow<String?>(null)
    val regConfirmPasswordError = MutableStateFlow<String?>(null)
    val regPhoneError = MutableStateFlow<String?>(null)

    private val _registerState = MutableStateFlow<UiState<Boolean>>(UiState.Empty)
    val registerState: StateFlow<UiState<Boolean>> = _registerState.asStateFlow()

    // Forgot Password State
    val forgotEmail = MutableStateFlow("")
    val forgotEmailError = MutableStateFlow<String?>(null)

    private val _forgotState = MutableStateFlow<UiState<Boolean>>(UiState.Empty)
    val forgotState: StateFlow<UiState<Boolean>> = _forgotState.asStateFlow()

    fun onLoginEmailChange(input: String) {
        loginEmail.value = input
        loginEmailError.value = if (input.contains("@") && input.contains(".")) null else "Enter a valid email address"
    }

    fun onLoginPasswordChange(input: String) {
        loginPassword.value = input
        loginPasswordError.value = if (input.length >= 6) null else "Password must be at least 6 characters"
    }

    fun isLoginValid(): Boolean {
        return loginEmail.value.contains("@") && loginEmail.value.contains(".") && loginPassword.value.length >= 6
    }

    fun performLogin(onSuccess: () -> Unit) {
        if (!isLoginValid()) return
        _loginState.value = UiState.Loading
        // Simulated local auth delay
        _loginState.value = UiState.Success(true)
        onSuccess()
    }

    fun onRegEmailChange(input: String) {
        regEmail.value = input
        regEmailError.value = if (input.contains("@") && input.contains(".")) null else "Invalid email address"
    }

    fun onRegPhoneChange(input: String) {
        regPhone.value = input
        regPhoneError.value = if (input.length >= 10) null else "Enter valid 10-digit phone number"
    }

    fun onRegPasswordChange(input: String) {
        regPassword.value = input
        regPasswordError.value = if (input.length >= 6) null else "Password must be at least 6 characters"
        if (regConfirmPassword.value.isNotEmpty()) {
            regConfirmPasswordError.value = if (input == regConfirmPassword.value) null else "Passwords do not match"
        }
    }

    fun onRegConfirmPasswordChange(input: String) {
        regConfirmPassword.value = input
        regConfirmPasswordError.value = if (input == regPassword.value) null else "Passwords do not match"
    }

    fun getPasswordStrength(): PasswordStrength {
        val pass = regPassword.value
        if (pass.isEmpty()) return PasswordStrength.NONE
        return when {
            pass.length >= 10 && pass.any { it.isDigit() } && pass.any { !it.isLetterOrDigit() } -> PasswordStrength.STRONG
            pass.length >= 6 -> PasswordStrength.FAIR
            else -> PasswordStrength.WEAK
        }
    }

    fun isRegisterValid(): Boolean {
        return regFullName.value.isNotBlank() &&
                regEmail.value.contains("@") &&
                regPhone.value.length >= 10 &&
                regPassword.value.length >= 6 &&
                regPassword.value == regConfirmPassword.value
    }

    fun performRegister(onSuccess: () -> Unit) {
        if (!isRegisterValid()) return
        _registerState.value = UiState.Loading
        _registerState.value = UiState.Success(true)
        onSuccess()
    }

    fun onForgotEmailChange(input: String) {
        forgotEmail.value = input
        forgotEmailError.value = if (input.contains("@") && input.contains(".")) null else "Enter a valid registered email"
    }

    fun sendPasswordReset(onSuccess: () -> Unit) {
        if (!forgotEmail.value.contains("@")) return
        _forgotState.value = UiState.Loading
        _forgotState.value = UiState.Success(true)
        onSuccess()
    }
}

enum class PasswordStrength(val label: String, val progress: Float) {
    NONE("Empty", 0f),
    WEAK("Weak", 0.33f),
    FAIR("Fair", 0.66f),
    STRONG("Strong", 1f)
}
