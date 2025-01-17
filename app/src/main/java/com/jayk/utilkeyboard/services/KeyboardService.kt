package com.jayk.utilkeyboard.services

import android.content.ComponentName
import android.content.Intent
import android.content.res.ColorStateList
import android.inputmethodservice.InputMethodService
import android.provider.Settings
import android.util.Log
import android.view.HapticFeedbackConstants
import android.view.KeyEvent
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModel
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.jayk.utilkeyboard.R
import com.jayk.utilkeyboard.databinding.ChatSuggestionsLayoutBinding
import com.jayk.utilkeyboard.databinding.KeyboardLayoutBinding
import com.jayk.utilkeyboard.presentation.SuggestionsAdapter
import com.jayk.utilkeyboard.repositories.AccessibilityRepository
import com.jayk.utilkeyboard.viewmodel.ChatViewModel
import com.jayk.utilkeyboard.viewmodel.ChatViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class KeyboardService(

) : InputMethodService(), ViewModelStoreOwner {

    private lateinit var suggestionsAdapter: SuggestionsAdapter
    private var currentMode = "Happy"

    @Inject
    lateinit var chatViewModelFactory: ChatViewModelFactory

    private lateinit var binding: KeyboardLayoutBinding
    private lateinit var chatBinding: ChatSuggestionsLayoutBinding
    private val serviceScope = CoroutineScope(Dispatchers.Main + Job())
    private val _viewModelStore by lazy { ViewModelStore() }
    private val chatViewModel: ChatViewModel by lazy {
        chatViewModelFactory.create()
    }
    private var isCapitalized = false
    private var isSymbolsMode = false
    private var isChatScreen = false


    @Inject
    lateinit var accessibilityRepository: AccessibilityRepository


    override fun onCreate() {
        super.onCreate()
        observeViewModel()
    }



    private fun getMessages(emotion: String?, isFormal: Boolean = false) {
        if (!isAccessibilityServiceEnabled()) {
            showAccessibilityPrompt()
            return
        }

        serviceScope.launch {
            val lastMessage = accessibilityRepository.getLastMessage()
            val isDatingApp = accessibilityRepository.checkForDatingApp()
            if (lastMessage == null) {
                println("No messages available")
                chatBinding.apply {
                    messageUnavailableText.visibility = View.VISIBLE
                }

            } else {
                chatBinding.apply {
                    messageUnavailableText.visibility = View.GONE
                }
                println("Last message: $lastMessage")
                if(isDatingApp!=null && isDatingApp) {
                    chatViewModel.sendMessage("$emotion : $lastMessage", isDatingApp = true)
                }
                if (!isFormal) {
                    chatViewModel.sendMessage("$emotion : $lastMessage")
                } else {
                    chatViewModel.sendMessage("formal:send some general formal text replies")
                }

            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }

    override val viewModelStore: ViewModelStore
        get() = _viewModelStore


    override fun onCreateInputView(): View {
        binding = KeyboardLayoutBinding.inflate(layoutInflater)
        chatBinding = ChatSuggestionsLayoutBinding.inflate(layoutInflater)

        setupButtons()
        setupRecyclerView()
        setupClickListeners()

        println(isCapitalized)

        fun performHapticFeedback(view: View) {
            view.performHapticFeedback(
                HapticFeedbackConstants.KEYBOARD_TAP,

                )
        }

        val letterButtons = mapOf(
            binding.btnA to Pair("a", "A"),
            binding.btnB to Pair("b", "B"),
            binding.btnC to Pair("c", "C"),
            binding.btnD to Pair("d", "D"),
            binding.btnE to Pair("e", "E"),
            binding.btnF to Pair("f", "F"),
            binding.btnG to Pair("g", "G"),
            binding.btnH to Pair("h", "H"),
            binding.btnI to Pair("i", "I"),
            binding.btnJ to Pair("j", "J"),
            binding.btnK to Pair("k", "K"),
            binding.btnL to Pair("l", "L"),
            binding.btnM to Pair("m", "M"),
            binding.btnN to Pair("n", "N"),
            binding.btnO to Pair("o", "O"),
            binding.btnP to Pair("p", "P"),
            binding.btnQ to Pair("q", "Q"),
            binding.btnR to Pair("r", "R"),
            binding.btnS to Pair("s", "S"),
            binding.btnT to Pair("t", "T"),
            binding.btnU to Pair("u", "U"),
            binding.btnV to Pair("v", "V"),
            binding.btnW to Pair("w", "W"),
            binding.btnX to Pair("x", "X"),
            binding.btnY to Pair("y", "Y"),
            binding.btnZ to Pair("z", "Z")
        )

        val numberButtons = mapOf(
            binding.btn0 to "0",
            binding.btn1 to "1",
            binding.btn2 to "2",
            binding.btn3 to "3",
            binding.btn4 to "4",
            binding.btn5 to "5",
            binding.btn6 to "6",
            binding.btn7 to "7",
            binding.btn8 to "8",
            binding.btn9 to "9"
        )

        val punctuationButtons = mapOf(
            binding.btnDot to ".",
            binding.btnComma to ","
        )

        val symbolButtons = mapOf(
            binding.btnStar to "*",
            binding.btnDQ to "\"",
            binding.btnSQ to "'",
            binding.btnColon to ":",
            binding.btnSemiColon to ";",
            binding.btnExclamation to "!",
            binding.btnQn to "?",
            binding.btnAt to "@",
            binding.btnHash to "#",
            binding.btnAnd to "&",
            binding.btnUnderscore to "_",
            binding.btnMinus to "-",
            binding.btnDollar to "$",
            binding.btnLeftBrace to "(",
            binding.btnRightBrace to ")",
            binding.btnPlus to "+",
            binding.btnSlash to "/"
        )


        fun updateButtonTexts() {
            if (isSymbolsMode) {
                symbolButtons.forEach { (button, symbol) ->
                    button.text = symbol
                }
            } else {
                letterButtons.forEach { (button, textPair) ->
                    button.text = if (isCapitalized) textPair.second else textPair.first
                }
            }
        }

        fun toggleSymbolsMode() {

            isSymbolsMode = !isSymbolsMode
            println(isSymbolsMode)
            if (isSymbolsMode) {
                binding.btnSymbols.setText("ABC")
            } else {
                binding.btnSymbols.setText("123")
            }
            binding.asdf.visibility = if (isSymbolsMode) View.GONE else View.VISIBLE
            binding.qwerty.visibility = if (isSymbolsMode) View.GONE else View.VISIBLE
            binding.SymbolsFirst.visibility = if (isSymbolsMode) View.VISIBLE else View.GONE
            binding.numbers.visibility = if (isSymbolsMode) View.VISIBLE else View.GONE
            binding.symbolsLast.visibility = if (isSymbolsMode) View.VISIBLE else View.GONE
            binding.zxcv.visibility = if (isSymbolsMode) View.GONE else View.VISIBLE
            updateButtonTexts()
        }

        symbolButtons.forEach { (button, text) ->
            button.setOnClickListener { view ->
                performHapticFeedback(view)
                currentInputConnection?.commitText(text, 1)

            }
        }

        letterButtons.forEach { (button, textPair) ->
            button.setOnClickListener { view ->
                view.performHapticFeedback(
                    HapticFeedbackConstants.KEYBOARD_TAP,
                )
                val textToCommit = if (isCapitalized) textPair.second else textPair.first
                currentInputConnection?.commitText(textToCommit, 1)
            }
        }

        numberButtons.forEach { (button, text) ->
            button.setOnClickListener { view ->
                performHapticFeedback(view)
                currentInputConnection?.commitText(text, 1)
            }
        }

        punctuationButtons.forEach { (button, text) ->
            button.setOnClickListener { view ->
                performHapticFeedback(view)
                currentInputConnection?.commitText(text, 1)
            }
        }

        binding.btnSymbols.setOnClickListener { view ->
            performHapticFeedback(view)
            toggleSymbolsMode()
        }

        binding.btnBackSpace.setOnClickListener { view ->
            performHapticFeedback(view)
            currentInputConnection?.sendKeyEvent(
                KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL)
            )
        }

        binding.btnBackSpace2.setOnClickListener { view ->
            performHapticFeedback(view)
            currentInputConnection?.sendKeyEvent(
                KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL)
            )
        }

        binding.btnCapitalize.setOnClickListener { view ->
            performHapticFeedback(view)
            isCapitalized = !isCapitalized
            updateButtonTexts()
            binding.btnCapitalize.backgroundTintList = ContextCompat.getColorStateList(
                baseContext,
                if (isCapitalized) R.color.primaryColor else R.color.greyColor
            )
        }

        binding.btnCapitalize2.setOnClickListener { view ->
            performHapticFeedback(view)
            isCapitalized = !isCapitalized
            updateButtonTexts()
            binding.btnCapitalize.backgroundTintList = ContextCompat.getColorStateList(
                baseContext,
                if (isCapitalized) R.color.primaryColor else R.color.greyColor
            )
        }

        //updateButtonTexts()

        binding.btnSpace.setOnClickListener { view ->
            performHapticFeedback(view)
            currentInputConnection?.sendKeyEvent(
                KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_SPACE)
            )
        }

        binding.btnEnter.setOnClickListener { view ->
            performHapticFeedback(view)
            currentInputConnection?.sendKeyEvent(
                KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER)
            )
        }

        return if (isChatScreen) chatBinding.root else binding.root
    }

    private fun setupButtons() {
        chatBinding.apply {
            btnHappy.setOnClickListener {
                updateButtonStates(it as Button, "Happy")
                getMessages("Happy")
            }

            btnSad.setOnClickListener {
                updateButtonStates(it as Button, "Sad")
                getMessages("Sad")
            }

            btnAngry.setOnClickListener {
                updateButtonStates(it as Button, "Angry")
                getMessages("Angry")
            }

            btnExcited.setOnClickListener {
                updateButtonStates(it as Button, "Excited")
                getMessages("Excited")
            }

            btnFlirty.setOnClickListener {
                updateButtonStates(it as Button, "Flirty")
                getMessages("Flirty")
            }

            btnFunny.setOnClickListener {
                updateButtonStates(it as Button, "Funny")
                getMessages("Funny")
            }

            btnGoBack.setOnClickListener {
                isChatScreen = false
                setInputView(onCreateInputView())
            }
        }
    }

    private fun updateButtonStates(selectedButton: Button, mode: String) {
        // Reset all buttons
        chatBinding.apply {
            btnHappy.backgroundTintList = ColorStateList.valueOf(getColor(R.color.greyColor))
            btnSad.backgroundTintList = ColorStateList.valueOf(getColor(R.color.greyColor))
            btnAngry.backgroundTintList = ColorStateList.valueOf(getColor(R.color.greyColor))
            btnFunny.backgroundTintList = ColorStateList.valueOf(getColor(R.color.greyColor))
            btnExcited.backgroundTintList = ColorStateList.valueOf(getColor(R.color.greyColor))
            btnFlirty.backgroundTintList = ColorStateList.valueOf(getColor(R.color.greyColor))
        }

        // Highlight selected button
        selectedButton.backgroundTintList = ColorStateList.valueOf(getColor(R.color.primaryColor))
        currentMode = mode
    }

    private fun setupRecyclerView() {
        suggestionsAdapter = SuggestionsAdapter { suggestion ->
            // Handle suggestion click
            getCurrentInputConnection()?.commitText(suggestion, 1)
        }

        chatBinding.suggestionsRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = suggestionsAdapter
        }
    }

    private fun setupClickListeners() {
        binding.btnAI.setOnClickListener {
            isChatScreen = true
            setInputView(onCreateInputView())
            getMessages(null, true)


        }
    }

    private fun observeViewModel() {
        chatViewModel.isLoading.observeForever { isLoading ->
            if (isLoading) {
                chatBinding.loadingIndicator.visibility = View.VISIBLE
                chatBinding.suggestionsRecyclerView.visibility = View.GONE
            } else {
                chatBinding.loadingIndicator.visibility = View.GONE
                chatBinding.suggestionsRecyclerView.visibility = View.VISIBLE
            }

        }

        chatViewModel.chatResponse.observeForever { chatResponse ->
            val suggestions = chatResponse.choices.firstOrNull()?.message?.content?.let { content ->
                content.trim('[', ']').split("||").map { it.trim() }
            } ?: emptyList()
            println(suggestions)

            suggestionsAdapter.submitList(suggestions)
            chatBinding.loadingIndicator.visibility = View.GONE
        }
    }

    private fun isAccessibilityServiceEnabled(): Boolean {
        val enabledServices = Settings.Secure.getString(
            contentResolver,
            Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
        ) ?: return false

        val componentName = ComponentName(packageName, MessageAccessibilityService::class.java.name)
        return enabledServices.contains(componentName.flattenToString())
    }

    private fun showAccessibilityPrompt() {
        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        Toast.makeText(
            this,
            "Please enable accessibility service for message suggestions",
            Toast.LENGTH_LONG
        ).show()
        startActivity(intent)
    }

}