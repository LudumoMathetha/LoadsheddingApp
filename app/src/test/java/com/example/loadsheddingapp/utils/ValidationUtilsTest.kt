package com.example.loadsheddingapp.utils

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ValidationUtilsTest {

    @Test
    fun isValidEmail_validEmails_returnsTrue() {
        assertTrue(ValidationUtils.isValidEmail("student@example.com"))
        assertTrue(ValidationUtils.isValidEmail("john.doe@university.ac.za"))
        assertTrue(ValidationUtils.isValidEmail("user123@domain.co.za"))
    }

    @Test
    fun isValidEmail_invalidEmails_returnsFalse() {
        assertFalse(ValidationUtils.isValidEmail(""))
        assertFalse(ValidationUtils.isValidEmail("plainaddress"))
        assertFalse(ValidationUtils.isValidEmail("@missingusername.com"))
        assertFalse(ValidationUtils.isValidEmail("username@.com"))
        assertFalse(ValidationUtils.isValidEmail("username@domain"))
    }

    @Test
    fun isValidPassword_passwordLength_validatesCorrectly() {
        assertFalse(ValidationUtils.isValidPassword(""))
        assertFalse(ValidationUtils.isValidPassword("12345"))
        assertTrue(ValidationUtils.isValidPassword("123456"))
        assertTrue(ValidationUtils.isValidPassword("SecureP@ssw0rd!"))
    }

    @Test
    fun doPasswordsMatch_matchingPasswords_returnsTrue() {
        assertTrue(ValidationUtils.doPasswordsMatch("password123", "password123"))
        assertFalse(ValidationUtils.doPasswordsMatch("password123", "password124"))
        assertFalse(ValidationUtils.doPasswordsMatch("Password123", "password123"))
    }

    @Test
    fun hashPassword_producesConsistentSha256Hash() {
        val hash1 = ValidationUtils.hashPassword("MySecretPassword")
        val hash2 = ValidationUtils.hashPassword("MySecretPassword")
        val hash3 = ValidationUtils.hashPassword("DifferentPassword")

        assertEquals(hash1, hash2)
        assertNotEquals(hash1, hash3)
        assertEquals(64, hash1.length) // SHA-256 hex string length is 64 chars
    }
}
