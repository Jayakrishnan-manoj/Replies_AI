package com.jayk.utilkeyboard.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jayk.utilkeyboard.repositories.AccessibilityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

//@HiltViewModel
//class KeyboardViewModel @Inject constructor(
//    private val accessibilityRepository: AccessibilityRepository
//)  : ViewModel(){
//
//    private val _isAccessibilityEnabled = MutableStateFlow(false)
//    val isAccessibilityEnabled: StateFlow<Boolean> = _isAccessibilityEnabled.asStateFlow()
//
//    fun getMessages(){
//        viewModelScope.launch {
//            val messages = accessibilityRepository.getLatestMessages()
//            if(messages.isEmpty()){
//                println("no messages, accessibility might be disabled")
//            }else{
//                println(messages[0])
//                println(messages[1])
//                println(messages[2])
//            }
//        }
//    }
//
//}