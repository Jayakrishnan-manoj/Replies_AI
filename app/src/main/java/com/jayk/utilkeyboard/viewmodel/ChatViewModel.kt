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

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading


    fun sendMessage(message: String) {
        viewModelScope.launch {

            _isLoading.value = true
            try {
                val messages = listOf(
                    Message("For the text that user sends which is in the format: \"emotion:message\", give 4 appropriate responses in the emotion provided. The response must be an array of replies separating each message with a double pipe (`||`)." +
                            "example: " +
                            "user message - happy: I'm getting a raise, your response: [oh that's great!||so happy for you!||congratulations!]", "developer"),
                    Message( message, "user")
                )

                when( val result = repository.getSearchResults("gpt-4o", messages)){
                    is ApiResult.Success ->{
                        _chatResponse.postValue(result.data)

                    }
                    is ApiResult.Error -> {
                        val errorMessage = result.message

                        println(errorMessage)
                    }
                }
            } finally {
                _isLoading.value = false
            }


        }
    }


}