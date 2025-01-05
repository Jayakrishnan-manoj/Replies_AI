package com.jayk.utilkeyboard.services

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent

class MessageAccessibilityService : AccessibilityService() {
    companion object {
        private var instance: MessageAccessibilityService? = null

        fun getInstance(): MessageAccessibilityService? = instance
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        TODO("Not yet implemented")
    }

    override fun onInterrupt() {
        TODO("Not yet implemented")
    }

}