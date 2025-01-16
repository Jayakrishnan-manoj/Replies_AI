package com.jayk.utilkeyboard.data

import android.content.Context
import android.view.accessibility.AccessibilityManager
import android.view.accessibility.AccessibilityNodeInfo
import com.jayk.utilkeyboard.repositories.AccessibilityRepository
import com.jayk.utilkeyboard.services.MessageAccessibilityService
import javax.inject.Inject

class AccessibilityRepositoryImpl @Inject constructor(
    private val context: Context,
    private val accessibilityManager: AccessibilityManager
) : AccessibilityRepository {

    override fun getLatestMessages(): List<String> {
        if (!accessibilityManager.isEnabled) {
            return emptyList()
        }

        val accessibilityService = MessageAccessibilityService.getInstance()
            ?: return emptyList()

        return accessibilityService.getLatestMessages()
    }

    override fun getLastMessage(): String? {
        if (!accessibilityManager.isEnabled) {
            return null
        }

        val accessibilityService = MessageAccessibilityService.getInstance()
            ?: return null

        return accessibilityService.getLastMessage()
    }
}