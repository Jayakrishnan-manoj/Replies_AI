package com.jayk.utilkeyboard

import android.inputmethodservice.InputMethodService
import android.view.HapticFeedbackConstants
import android.view.KeyEvent
import android.view.View
import androidx.core.content.ContextCompat
import com.jayk.utilkeyboard.databinding.KeyboardLayoutBinding

class KeyboardService : InputMethodService() {

    private lateinit var binding: KeyboardLayoutBinding
    private var isCapitalized = false

    override fun onCreateInputView(): View {
        // Setup data binding
        binding = KeyboardLayoutBinding.inflate(layoutInflater)

        println(isCapitalized)

        fun performHapticFeedback(view: View) {
            view.performHapticFeedback(
                HapticFeedbackConstants.KEYBOARD_TAP,

                )
        }

        // Create a map of all letter buttons using data binding references
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

        fun updateButtonTexts() {
            letterButtons.forEach { (button, textPair) ->
                button.text = if (isCapitalized) textPair.second else textPair.first
            }

        }

        // Set click listeners for all letter buttons
        letterButtons.forEach { (button, textPair) ->
            button.setOnClickListener { view ->
                view.performHapticFeedback(
                    HapticFeedbackConstants.KEYBOARD_TAP,
                )
                val textToCommit = if (isCapitalized) textPair.second else textPair.first
                currentInputConnection?.commitText(textToCommit, 1)
            }
        }

        // Set click listeners for all number buttons
        numberButtons.forEach { (button, text) ->
            button.setOnClickListener { view ->
                performHapticFeedback(view)
                currentInputConnection?.commitText(text, 1)
            }
        }

        // Set click listeners for punctuation buttons
        punctuationButtons.forEach { (button, text) ->
            button.setOnClickListener { view ->
                performHapticFeedback(view)
                currentInputConnection?.commitText(text, 1)
            }
        }

        // Special keys
        binding.btnBackSpace.setOnClickListener { view ->
            performHapticFeedback(view)
            currentInputConnection?.sendKeyEvent(
                KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL)
            )
        }

        binding.btnCapitalize.setOnClickListener { view->
            performHapticFeedback(view)
            println(isCapitalized)
            currentInputConnection?.sendKeyEvent(
                KeyEvent(KeyEvent.ACTION_DOWN,KeyEvent.KEYCODE_CAPS_LOCK)
            )
            isCapitalized = !isCapitalized
            updateButtonTexts()

            binding.apply {
                if(!isCapitalized){
                    btnCapitalize.backgroundTintList = ContextCompat.getColorStateList(baseContext,R.color.greyColor)
                }else{
                    btnCapitalize.backgroundTintList = ContextCompat.getColorStateList (baseContext, R.color.primaryColor)
                }

            }

//            // Change the visible text on all letter buttons
//            binding.apply {
//                btnA.text = if (isCapitalized) "A" else "a"
//                btnB.text = if (isCapitalized) "B" else "b"
//                btnC.text = if (isCapitalized) "C" else "c"
//                btnD.text = if (isCapitalized) "D" else "d"
//                btnE.text = if (isCapitalized) "E" else "e"
//                btnF.text = if (isCapitalized) "F" else "f"
//                btnG.text = if (isCapitalized) "G" else "g"
//                btnH.text = if (isCapitalized) "H" else "h"
//                btnI.text = if (isCapitalized) "I" else "i"
//                btnJ.text = if (isCapitalized) "J" else "j"
//                btnK.text = if (isCapitalized) "K" else "k"
//                btnL.text = if (isCapitalized) "L" else "l"
//                btnM.text = if (isCapitalized) "M" else "m"
//                btnN.text = if (isCapitalized) "N" else "n"
//                btnO.text = if (isCapitalized) "O" else "o"
//                btnP.text = if (isCapitalized) "P" else "p"
//                btnQ.text = if (isCapitalized) "Q" else "q"
//                btnR.text = if (isCapitalized) "R" else "r"
//                btnS.text = if (isCapitalized) "S" else "s"
//                btnT.text = if (isCapitalized) "T" else "t"
//                btnU.text = if (isCapitalized) "U" else "u"
//                btnV.text = if (isCapitalized) "V" else "v"
//                btnW.text = if (isCapitalized) "W" else "w"
//                btnX.text = if (isCapitalized) "X" else "x"
//                btnY.text = if (isCapitalized) "Y" else "y"
//                btnZ.text = if (isCapitalized) "Z" else "z"
//            }

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

        return binding.root
    }
}