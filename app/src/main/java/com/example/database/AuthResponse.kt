package com.example.database

// Класс данных, представляющий ответ от сервера после отправки запроса на авторизацию
data class AuthResponse(
    val success: Boolean, // Флаг успеха операции
    val message: String?, // Сообщение от сервера (например, ошибки)
    val token: String? // Токен, полученный в случае успешной авторизации
)
