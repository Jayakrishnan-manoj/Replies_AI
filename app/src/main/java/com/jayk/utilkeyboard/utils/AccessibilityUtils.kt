package com.jayk.utilkeyboard.utils

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.widget.Toast
import com.jayk.utilkeyboard.R

object AccessibilityUtils {
    fun isAccessibilityServiceEnabled(context: Context, serviceClass: Class<*>): Boolean {
        val enabledServices = Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
        ) ?: return false
        
        val componentName = ComponentName(context.packageName, serviceClass.name)
        return enabledServices.contains(componentName.flattenToString())
    }

    fun promptEnableAccessibilityService(context: Context) {
        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        Toast.makeText(
            context,
            context.getString(R.string.enable_accessibility_prompt),
            Toast.LENGTH_LONG
        ).show()
        context.startActivity(intent)
    }
} 