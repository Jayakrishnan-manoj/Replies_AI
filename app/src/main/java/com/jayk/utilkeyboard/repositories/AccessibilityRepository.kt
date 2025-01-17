package com.jayk.utilkeyboard.repositories

interface AccessibilityRepository {
    fun getLatestMessages(): List<String>
    fun getLastMessage(): String?
    fun checkForDatingApp(): Boolean?
}