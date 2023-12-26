package com.example.database

// Класс данных, представляющий запрос на авторизацию
data class AuthRequest(
    val phone: String, // Номер телефона
    val code: String? // Код подтверждения (может быть null на этапе отправки номера телефона)
)
