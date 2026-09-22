package com.example.loadsheddingapp.utils

import java.security.MessageDigest
import java.util.regex.Pattern

// Utility object providing input validation logic for user authentication forms and security hashing.
object ValidationUtils {

    private val EMAIL_PATTERN: Pattern = Pattern.compile(
        "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$"
    )

    // Validates whether the provided email string matches standard email patterns.
    fun isValidEmail(email: String): Boolean {
        return email.isNotBlank() && EMAIL_PATTERN.matcher(email.trim()).matches()
    }

    // Validates password strength (minimum 6 characters for local prototype requirement).
    fun isValidPassword(password: String): Boolean {
        return password.isNotBlank() && password.length >= 6
    }

    // Checks if the password and confirm password fields match.
    fun doPasswordsMatch(p1: String, p2: String): Boolean {
        return p1 == p2
    }

    // Hashes plaintext passwords using SHA-256 for local prototype storage.
    // Plaintext passwords should NEVER be stored directly in a database.
    fun hashPassword(password: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(password.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }
}
