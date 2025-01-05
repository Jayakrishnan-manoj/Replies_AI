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

        val rootNode = accessibilityService.rootInActiveWindow
            ?: return emptyList()

//        val rootNode = context.findViewsWithText(null, "", Find.FIND_VIEWS_WITH_TEXT)
//            .firstOrNull()?.rootView?.createAccessibilityNodeInfo() ?: return emptyList()

        return extractMessages(rootNode)
    }



    private fun extractMessages(node: AccessibilityNodeInfo): List<String> {
        val messages = mutableListOf<String>()
        findTextNodes(node, messages)

        return messages.takeLast(5)
    }

    private fun findTextNodes(node: AccessibilityNodeInfo, messages: MutableList<String>) {
        if (node.text != null && !node.text.isNullOrBlank()) {
            messages.add(node.text.toString())
        }

        for (i in 0 until node.childCount) {
            val child = node.getChild(i) ?: continue
            findTextNodes(child, messages)

        }
    }

}