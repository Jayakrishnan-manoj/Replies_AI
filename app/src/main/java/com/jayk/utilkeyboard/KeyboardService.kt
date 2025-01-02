package com.jayk.utilkeyboard

import android.inputmethodservice.InputMethodService
import android.view.KeyEvent
import android.view.View
import com.jayk.utilkeyboard.databinding.KeyboardLayoutBinding

//class KeyboardService : InputMethodService() {
//
//    override fun onCreateInputView(): View {
//
//        val keyboardBinding = KeyboardLayoutBinding.inflate(layoutInflater)
//
//        val buttonIds = arrayOf(
//            R.id.btn0,
//            R.id.btn1,
//            R.id.btn2,
//            R.id.btn3,
//            R.id.btn4,
//            R.id.btn5,
//            R.id.btn6,
//            R.id.btn7,
//            R.id.btn8,
//            R.id.btn9,
//            R.id.btn0,
//            R.id.btnQ,
//            R.id.btnW,
//            R.id.btnE,
//            R.id.btnR,
//            R.id.btnT,
//            R.id.btnY,
//            R.id.btnU,
//            R.id.btnI,
//            R.id.btnO,
//            R.id.btnP,
//            R.id.btnA,
//            R.id.btnS,
//            R.id.btnD,
//            R.id.btnF,
//            R.id.btnG,
//            R.id.btnH,
//            R.id.btnJ,
//            R.id.btnK,
//            R.id.btnL,
//            R.id.btnZ,
//            R.id.btnX,
//            R.id.btnC,
//            R.id.btnV,
//            R.id.btnB,
//            R.id.btnN,
//            R.id.btnM,
//            R.id.btnDot,
//            R.id.btnComma
//        )
//
//        for (buttonId in buttonIds) {
//            val button = keyboardBinding.root.findViewById<Button>(buttonId)
//            button.setOnClickListener {
//                val inputConnection = currentInputConnection
//                inputConnection?.commitText(button.text.toString(), 1)
//            }
//        }
//
//        keyboardBinding.btnBackSpace.setOnClickListener {
//            val inputConnection = currentInputConnection
//            inputConnection?.sendKeyEvent(KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL))
//            return@setOnClickListener
//        }
//
//        keyboardBinding.btnSpace.setOnClickListener {
//            val inputConnection = currentInputConnection
//            inputConnection?.sendKeyEvent(KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_SPACE))
//            return@setOnClickListener
//        }
//
//        keyboardBinding.btnEnter.setOnClickListener {
//            val inputConnection = currentInputConnection
//            inputConnection?.sendKeyEvent(KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER))
//            return@setOnClickListener
//        }
//
//        return keyboardBinding.root
//    }
//}

class KeyboardService : InputMethodService() {

    private lateinit var binding: KeyboardLayoutBinding

    override fun onCreateInputView(): View {
        // Setup data binding
        binding = KeyboardLayoutBinding.inflate(layoutInflater)

        // Create a map of all letter buttons using data binding references
        val letterButtons = mapOf(
            binding.btnA to "A",
            binding.btnB to "B",
            binding.btnC to "C",
            binding.btnD to "D",
            binding.btnE to "E",
            binding.btnF to "F",
            binding.btnG to "G",
            binding.btnH to "H",
            binding.btnI to "I",
            binding.btnJ to "J",
            binding.btnK to "K",
            binding.btnL to "L",
            binding.btnM to "M",
            binding.btnN to "N",
            binding.btnO to "O",
            binding.btnP to "P",
            binding.btnQ to "Q",
            binding.btnR to "R",
            binding.btnS to "S",
            binding.btnT to "T",
            binding.btnU to "U",
            binding.btnV to "V",
            binding.btnW to "W",
            binding.btnX to "X",
            binding.btnY to "Y",
            binding.btnZ to "Z"
        )

        // Create a map of all number buttons
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

        // Create a map of punctuation buttons
        val punctuationButtons = mapOf(
            binding.btnDot to ".",
            binding.btnComma to ","
        )

        // Set click listeners for all letter buttons
        letterButtons.forEach { (button, text) ->
            button.setOnClickListener {
                currentInputConnection?.commitText(text, 1)
            }
        }

        // Set click listeners for all number buttons
        numberButtons.forEach { (button, text) ->
            button.setOnClickListener {
                currentInputConnection?.commitText(text, 1)
            }
        }

        // Set click listeners for punctuation buttons
        punctuationButtons.forEach { (button, text) ->
            button.setOnClickListener {
                currentInputConnection?.commitText(text, 1)
            }
        }

        // Special keys
        binding.btnBackSpace.setOnClickListener {
            currentInputConnection?.sendKeyEvent(
                KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL)
            )
        }

        binding.btnSpace.setOnClickListener {
            currentInputConnection?.sendKeyEvent(
                KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_SPACE)
            )
        }

        binding.btnEnter.setOnClickListener {
            currentInputConnection?.sendKeyEvent(
                KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER)
            )
        }

        return binding.root
    }
}