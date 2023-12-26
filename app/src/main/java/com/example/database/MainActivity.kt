package com.example.database

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    lateinit var phoneNumberEditText: EditText
    lateinit var continueButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        phoneNumberEditText = findViewById(R.id.phoneNumberEditText)
        continueButton = findViewById(R.id.continueButton)

        continueButton.setOnClickListener {
            val phoneNumber = phoneNumberEditText.text.toString()

            sendVerificationCode(phoneNumber)
        }
    }

    private fun sendVerificationCode(phoneNumber: String) {
        // Создаем объект запроса на авторизацию
        val authRequest = AuthRequest(phone = phoneNumber, code = null)
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
                            navigateToVerificationScreen()
                        } else {
                            Toast.makeText(this@MainActivity, "Error: ${authResponse?.message}", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this@MainActivity, "Network Error", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                //Обработка исключений, таких как сетевые ошибки или ошибки парсинга
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@MainActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun navigateToVerificationScreen() {
        val intent = Intent(this, VerificationActivity::class.java)
        startActivity(intent)
        finish()
    }
}