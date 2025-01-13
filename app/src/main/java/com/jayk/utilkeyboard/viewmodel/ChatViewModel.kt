package com.jayk.utilkeyboard.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jayk.utilkeyboard.data.ApiResult
import com.jayk.utilkeyboard.data.models.request.Message
import com.jayk.utilkeyboard.data.models.response.ChatResponse
import com.jayk.utilkeyboard.data.repository.APIRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

class ChatViewModel @Inject constructor(
    private val repository: APIRepository
) : ViewModel() {
    private val _chatResponse = MutableLiveData<ChatResponse>()
    val chatResponse: LiveData<ChatResponse> = _chatResponse


    fun sendMessage(message: String) {
        viewModelScope.launch {
            val messages = listOf(
                Message("You are a helpful assistant.", "developer"),
                Message( message, "user")
            )

            when( val result = repository.getSearchResults("gpt-4o", messages)){
                is ApiResult.Success ->{
                    val response = result.data.choices.firstOrNull()?.message?.content
                    println(response)
                }
                is ApiResult.Error -> {
                    val errorMessage = result.message

                    println(errorMessage)
                }
            }
        }
    }


}