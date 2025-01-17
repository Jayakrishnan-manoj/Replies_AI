package com.jayk.utilkeyboard

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.jayk.utilkeyboard.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.apply {
            if(isKeyboardEnabled())
                btnEnableKeyboard.isEnabled = false
            btnEnableKeyboard.setOnClickListener {
                if(!isKeyboardEnabled())
                    openKeyboardSettings()
            }
            btnChooseKeyboard.setOnClickListener {
                if(isKeyboardEnabled()){
                    openKeyboardChooserSettings()
                }else{
                    Toast.makeText(this@MainActivity,"Enable the keyboard", Toast.LENGTH_LONG).show()
                }
            }
        }
    }



    private fun isKeyboardEnabled(): Boolean {
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val enabledInputMethodIds = inputMethodManager.enabledInputMethodList.map { it.id }
        return enabledInputMethodIds.contains("package com.jayk.utilkeyboard/.KeyboardService")
    }

    private fun openKeyboardChooserSettings(){
        val im = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        im.showInputMethodPicker()
    }



    private fun openKeyboardSettings() {
        val intent = Intent(Settings.ACTION_INPUT_METHOD_SETTINGS)
        startActivity(intent)
    }
}