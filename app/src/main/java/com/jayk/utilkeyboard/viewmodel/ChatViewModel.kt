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


    fun sendMessage(message: String, isDatingApp: Boolean = false) {
        viewModelScope.launch {

            _isLoading.value = true

            if (isDatingApp) {
                try {
                    val messages = listOf(
                        Message(
                            "For messages in the format 'emotion:message', provide 4 flirty, engaging dating app responses that match the given emotion. The responses should be separated by double pipes (||). The responses should be appropriate for dating app conversations and encourage further interaction." +
                                    "Example:" +
                                    "User message - happy: Just got a promotion at work!" +
                                    "Response: [Would love to celebrate your success over drinks! 🎉||This is amazing - your ambition is super attractive! 😊||Congratulations! Let's plan something special to celebrate? 🌟||Love seeing you win! Coffee on me to hear all about it? ☕]",
                            "developer"
                        ),
                        Message(message, "user")
                    )

                    when (val result = repository.getSearchResults("gpt-4o", messages)) {
                        is ApiResult.Success -> {
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
            } else {

                try {
                    val messages = listOf(
                        Message(
                            "For the text that user sends which is in the format: \"emotion:message\", give 4 appropriate responses in the emotion provided. The response must be an array of replies separating each message with a double pipe (`||`). and must contain emojis" +
                                    "example: " +
                                    "user message - happy: I'm getting a raise, your response: [oh that's great!||so happy for you!||congratulations!]",
                            "developer"
                        ),
                        Message(message, "user")
                    )

                    when (val result = repository.getSearchResults("gpt-4o", messages)) {
                        is ApiResult.Success -> {
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


}