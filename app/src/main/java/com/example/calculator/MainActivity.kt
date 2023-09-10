package com.example.calculator

import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private var operator = ""
    private lateinit var equalButton: Button
    private lateinit var resultTextView: TextView
    private lateinit var editTextOne: EditText
    private lateinit var editTextTwo: EditText
    private lateinit var numberButtons: List<Int>
    private lateinit var operatorsButtons: List<Int>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setUpView()
        setUpDefaultSettings()
        setUpTextChangeWatcher()
        setUpClickListeners()
    }

    private fun setUpDefaultSettings() {
        disableKeyBoard()
        equalButton.isEnabled = checkInputValid()
        editTextOne.requestFocus()
    }

    private fun setUpView() {
        numberButtons =
            listOf(
                R.id.btn_one,
                R.id.btn_two,
                R.id.btn_three,
                R.id.btn_four,
                R.id.btn_five,
                R.id.btn_six,
                R.id.btn_seven,
                R.id.btn_eight,
                R.id.btn_nine,
                R.id.btn_zero,
                R.id.btn_dot
            )
        operatorsButtons = listOf(R.id.btn_plus, R.id.btn_minus, R.id.btn_mul, R.id.btn_div)
        equalButton = findViewById(R.id.btn_equal)
        resultTextView = findViewById(R.id.answer)
        editTextOne = findViewById(R.id.value_one_text_field)
        editTextTwo = findViewById(R.id.value_two_text_field)
    }

    private fun setUpClickListeners() {
        numberButtons.forEach { id ->
            findViewById<Button>(id).apply {
                setOnClickListener {
                    val textFieldToAdd =
                        if (editTextOne.hasFocus() && editTextOne.getLastElement()) editTextOne
                        else if (editTextTwo.hasFocus() && editTextTwo.getLastElement()) editTextTwo else return@setOnClickListener

                    textFieldToAdd.append(text.toString())
                }
            }
        }

        operatorsButtons.forEach { id -> findViewById<Button>(id).apply { setOnClickListener { operator = text.toString() } } }

        findViewById<Button>(R.id.btn_equal).setOnClickListener { if (checkInputValid()) calculateResult() }

        findViewById<TextView>(R.id.btn_clear).setOnClickListener { clearField() }

        findViewById<ImageView>(R.id.ic_back).setOnClickListener {
            if (editTextOne.hasFocus()) editTextOne.removeLastCharacterFromEditText()
            else if (editTextTwo.hasFocus()) editTextTwo.removeLastCharacterFromEditText()
        }
    }

    private fun calculateResult() {

        val value1 = editTextOne.text.toString().toFloatOrNull() ?: 0f
        val value2 = editTextTwo.text.toString().toFloatOrNull() ?: 0f
        resultTextView.text =
            when (operator) {
                CalculatorOperations.ADDITION -> (value1 + value2).toString()
                CalculatorOperations.SUBTRACTION -> (value1 - value2).toString()
                CalculatorOperations.MULTIPLICATION -> (value1 * value2).toString()
                CalculatorOperations.DIVISION -> (value1 / value2).toString()
                else -> ""
            }
    }

    private fun EditText.getLastElement() = text.toString().takeIf { it.isNotEmpty() }?.last() != '.'

    private fun setUpTextChangeWatcher() {

        val onFocusChange =
            object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    equalButton.isEnabled = checkInputValid()
                }

                override fun afterTextChanged(p0: Editable?) {}
            }

        editTextOne.addTextChangedListener(onFocusChange)
        editTextTwo.addTextChangedListener(onFocusChange)
    }

    private fun checkInputValid() =
        editTextOne.text.isNotBlank() &&
                editTextTwo.text.isNotBlank() &&
                editTextOne.text.toString().trim() != "." &&
                editTextTwo.text.toString().trim() != "."

    private fun disableKeyBoard() {
        editTextOne.inputType = InputType.TYPE_NULL
        editTextTwo.inputType = InputType.TYPE_NULL
    }

    private fun clearField() {
        editTextOne.text.clear()
        editTextTwo.text.clear()
        resultTextView.text = ""
    }

    private fun EditText.removeLastCharacterFromEditText(): Boolean {
        if (text.isNotEmpty()) {
            setText(text.dropLast(1))
            setSelection(text.length)
        }
        return true
    }
}
