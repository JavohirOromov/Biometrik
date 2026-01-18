package com.javohir.biometrik
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.javohir.biometrik.ui.theme.BiometrikTheme

class MainActivity : FragmentActivity() {

    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setupBiometricPrompt()

        setContent {
            BiometrikTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    BiometricScreen(
                        modifier = Modifier.padding(innerPadding),
                        onBiometricClick = {
                            checkBiometricAvailability()
                        }
                    )
                }
            }
        }
    }

    private fun setupBiometricPrompt() {
        val executor = ContextCompat.getMainExecutor(this)

        biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(
                    errorCode: Int,
                    errString: CharSequence
                ) {
                    super.onAuthenticationError(errorCode, errString)
                    runOnUiThread {
                        Toast.makeText(
                           this@MainActivity,
                            "Xato: $errString",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult
                ) {
                    super.onAuthenticationSucceeded(result)
                    runOnUiThread {
                        Toast.makeText(
                            this@MainActivity,
                            "Biometrik autentifikatsiya muvaffaqiyatli!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    runOnUiThread {
                        Toast.makeText(
                            this@MainActivity,
                            "Autentifikatsiya muvaffaqiyatsiz",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometrik Autentifikatsiya")
            .setSubtitle("Barmoq izingizni skanerlang")
            .setNegativeButtonText("Bekor qilish")
            .setAllowedAuthenticators(BIOMETRIC_STRONG)
            .build()
    }

    private fun checkBiometricAvailability() {
        val biometricManager = BiometricManager.from(this)
        when (biometricManager.canAuthenticate(BIOMETRIC_STRONG)) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                biometricPrompt.authenticate(promptInfo)
            }
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                Toast.makeText(
                    this,
                    "Qurilmada biometrik sensor mavjud emas",
                    Toast.LENGTH_SHORT
                ).show()
            }
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                Toast.makeText(
                    this,
                    "Biometrik sensor hozir ishlamayapti",
                    Toast.LENGTH_SHORT
                ).show()
            }
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                Toast.makeText(
                    this,
                    "Qurilmada biometrik ma'lumotlar ro'yxatdan o'tmagan",
                    Toast.LENGTH_SHORT
                ).show()
            }
            else -> {
                Toast.makeText(
                    this,
                    "Biometrik autentifikatsiya mavjud emas",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}

@Composable
fun BiometricScreen(
    modifier: Modifier = Modifier,
    onBiometricClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Biometrik Scanner",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Creator: Javohir Oromov",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onBiometricClick,
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Biometrikni Test Qilish",
                fontSize = 18.sp,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }
    }
}