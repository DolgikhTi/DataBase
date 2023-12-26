package com.example.database

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class VerificationActivity : AppCompatActivity() {

    lateinit var codeEditText: EditText
    lateinit var submitButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verification)

        codeEditText = findViewById(R.id.codeEditText)
        submitButton = findViewById(R.id.submitButton)

        submitButton.setOnClickListener {
            val verificationCode = codeEditText.text.toString()

            checkVerificationCode(verificationCode)
        }
    }

    private fun checkVerificationCode(verificationCode: String) {
        // Создаем объект запроса на авторизацию с кодом подтверждения
        val authRequest = AuthRequest(phone = "+79008887766", code = verificationCode)
        // Запускаем корутину для асинхронного выполнения запроса
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Отправляем запрос на сервер с использованием Retrofit
                val response = ApiClient.apiService.sendPhoneNumber(authRequest)
                // Переключаемся на основной поток для обновления пользовательского интерфейса
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        // Обработка успешного ответа от сервера
                        val authResponse = response.body()
                        if (authResponse?.success == true) {
                            // Обработка успешного входа
                            val message = "Hello, ${authRequest.phone}, Your token is ${authResponse.token}"
                            Toast.makeText(this@VerificationActivity, message, Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this@VerificationActivity, "Error: ${authResponse?.message}", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this@VerificationActivity, "Network Error", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                // Обработка исключений, таких как сетевые ошибки или ошибки парсинга
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@VerificationActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
