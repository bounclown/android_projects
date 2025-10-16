package com.example.calculator

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    private lateinit var resultTextView: TextView
    private var currentInput = ""
    private var currentOperator = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        resultTextView = findViewById(R.id.textView)
        val button0 = findViewById<Button>(R.id.button)
        button0.setOnClickListener {
            resultTextView.append("0")
        }
        val button1 = findViewById<Button>(R.id.button1)
        button1.setOnClickListener {
            resultTextView.append("1")
        }
        val button2 = findViewById<Button>(R.id.button2)
        button2.setOnClickListener {
            resultTextView.append("2")
        }
        val button3 = findViewById<Button>(R.id.button3)
        button3.setOnClickListener {
            resultTextView.append("3")
        }
        val button4 = findViewById<Button>(R.id.button4)
        button4.setOnClickListener {
            resultTextView.append("4")
        }
        val button5 = findViewById<Button>(R.id.button5)
        button5.setOnClickListener {
            resultTextView.append("5")
        }
        val button6 = findViewById<Button>(R.id.button6)
        button6.setOnClickListener {
            resultTextView.append("6")
        }
        val button7 = findViewById<Button>(R.id.button7)
        button7.setOnClickListener {
            resultTextView.append("7")
        }
        val button8 = findViewById<Button>(R.id.button8)
        button8.setOnClickListener {
            resultTextView.append("8")
        }
        val button9 = findViewById<Button>(R.id.button9)
        button9.setOnClickListener {
            resultTextView.append("9")
        }
        val buttonPlus = findViewById<Button>(R.id.button13)
        buttonPlus.setOnClickListener {
            currentInput = resultTextView.text.toString()
            currentOperator = "+"
            resultTextView.text = ""
        }
        val buttonMinus = findViewById<Button>(R.id.button12)
        buttonMinus.setOnClickListener {
            currentInput = resultTextView.text.toString()
            currentOperator = "-"
            resultTextView.text = ""
        }
        val buttonMultiply = findViewById<Button>(R.id.button11)
        buttonMultiply.setOnClickListener {
            currentInput = resultTextView.text.toString()
            currentOperator = "*"
            resultTextView.text = ""
        }
        val buttonDivide = findViewById<Button>(R.id.button10)
        buttonDivide.setOnClickListener {
            currentInput = resultTextView.text.toString()
            currentOperator = "/"
            resultTextView.text = ""
        }
        val buttonEquals = findViewById<Button>(R.id.button14)
        buttonEquals.setOnClickListener {
            val secondInput = resultTextView.text.toString()
            val result = when (currentOperator) {
                "+" -> (currentInput.toInt() + secondInput.toInt()).toString()
                "-" -> (currentInput.toInt() - secondInput.toInt()).toString()
                "*" -> (currentInput.toInt() * secondInput.toInt()).toString()
                "/" -> (currentInput.toInt() / secondInput.toInt()).toString()
                else -> ""
            }
            resultTextView.text = result
        }
    }
}

