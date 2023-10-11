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

    private lateinit var equalButton: TextView
    private lateinit var resultTextView: TextView
    private lateinit var editTextOne: EditText
    private lateinit var editTextTwo: EditText
    private lateinit var btnOne: TextView
    private lateinit var btnTwo: TextView
    private lateinit var btnThree: TextView
    private lateinit var btnFour: TextView
    private lateinit var btnFive: TextView
    private lateinit var btnSix: TextView
    private lateinit var btnSeven: TextView
    private lateinit var btnEight: TextView
    private lateinit var btnNine: TextView
    private lateinit var btnPlus: TextView
    private lateinit var btnMinus: TextView
    private lateinit var btnMul: TextView
    private lateinit var btnDiv: TextView
    private lateinit var btnZero: TextView
    private lateinit var icClear: ImageView
    private lateinit var btnDot: TextView
    private lateinit var allClearBtn: TextView

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
        btnOne = findViewById(R.id.btn_one)
        btnTwo = findViewById(R.id.btn_two)
        btnThree = findViewById(R.id.btn_three)
        btnFour = findViewById(R.id.btn_four)
        btnFive = findViewById(R.id.btn_five)
        btnSix = findViewById(R.id.btn_six)
        btnSeven = findViewById(R.id.btn_seven)
        btnEight = findViewById(R.id.btn_eight)
        btnNine = findViewById(R.id.btn_nine)
        btnPlus = findViewById(R.id.btn_plus)
        btnMinus = findViewById(R.id.btn_minus)
        btnMul = findViewById(R.id.btn_mul)
        btnDiv = findViewById(R.id.btn_div)
        equalButton = findViewById(R.id.btn_equal)
        resultTextView = findViewById(R.id.answer)
        icClear = findViewById(R.id.ic_back)
        allClearBtn = findViewById(R.id.btn_clear)
        btnDot = findViewById(R.id.btn_dot)
        btnZero = findViewById(R.id.btn_zero)
        editTextOne = findViewById(R.id.value_one_text_field)
        editTextTwo = findViewById(R.id.value_two_text_field)
    }

    private fun setUpClickListeners() {
        setNumberClickListener(btnOne)
        setNumberClickListener(btnTwo)
        setNumberClickListener(btnThree)
        setNumberClickListener(btnFour)
        setNumberClickListener(btnFive)
        setNumberClickListener(btnSix)
        setNumberClickListener(btnSeven)
        setNumberClickListener(btnEight)
        setNumberClickListener(btnNine)
        setUpOperatorClickListener(btnPlus)
        setUpOperatorClickListener(btnMinus)
        setUpOperatorClickListener(btnMul)
        setUpOperatorClickListener(btnDiv)
        setNumberClickListener(btnDot)
        setNumberClickListener(btnZero)
        equalButton.setOnClickListener { if (checkInputValid()) calculateResult() }
        findViewById<TextView>(R.id.btn_clear).setOnClickListener { clearField() }
        icClear.setOnClickListener {
            if (editTextOne.hasFocus()) editTextOne.removeLastCharacterFromEditText()
            else if (editTextTwo.hasFocus()) editTextTwo.removeLastCharacterFromEditText()
        }
    }

    private fun calculateResult() {
        val value1 = editTextOne.text.toString().toFloatOrNull() ?: 0f
        val value2 = editTextTwo.text.toString().toFloatOrNull() ?: 0f
        resultTextView.text = when (operator) {
            CalculatorOperator.ADDITION.symbol -> (value1 + value2).toString()
            CalculatorOperator.SUBTRACTION.symbol -> (value1 - value2).toString()
            CalculatorOperator.MULTIPLICATION.symbol -> (value1 * value2).toString()
            CalculatorOperator.DIVISION.symbol -> (value1 / value2).toString()
            else -> CalculatorOperator.EMPTY.symbol
        }
    }

    private fun setNumberClickListener(btn: TextView) {
        btn.setOnClickListener {
            val currentNumber = btn.text.toString()
            val textFieldToAdd =
                if (editTextOne.hasFocus()) editTextOne
                else if (editTextTwo.hasFocus()) editTextTwo else return@setOnClickListener
            if (textFieldToAdd.text.contains(getString(R.string.btn_dot)) && currentNumber == getString(R.string.btn_dot)) return@setOnClickListener
            textFieldToAdd.append(currentNumber)
        }
    }

    private fun setUpOperatorClickListener(opBtn: TextView) = opBtn.setOnClickListener { operator = opBtn.text.toString() }

    private fun setUpTextChangeWatcher() {
        val onFocusChange =
            object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    equalButton.isEnabled = checkInputValid()
                }

                override fun afterTextChanged(p0: Editable?) = Unit
            }
        editTextOne.addTextChangedListener(onFocusChange)
        editTextTwo.addTextChangedListener(onFocusChange)
    }

    private fun checkInputValid() =
        editTextOne.text.isNotBlank() && editTextTwo.text.isNotBlank() && editTextOne.text.toString().trim() != getString(R.string.btn_dot) && editTextTwo.text.toString().trim() != getString(R.string.btn_dot)

    private fun disableKeyBoard() {
        editTextOne.inputType = InputType.TYPE_NULL
        editTextTwo.inputType = InputType.TYPE_NULL
    }

    private fun clearField() {
        editTextOne.text.clear()
        editTextTwo.text.clear()
        resultTextView.text = CalculatorOperator.EMPTY.symbol
        operator = CalculatorOperator.EMPTY.symbol
    }

    private fun EditText.removeLastCharacterFromEditText() {
        text.takeIf { it.isNotEmpty() }?.apply {
            setText(text.dropLast(1))
            setSelection(text.length)
        }
    }

    companion object {
        private var operator = CalculatorOperator.EMPTY.symbol
    }

}
