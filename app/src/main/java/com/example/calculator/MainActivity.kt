package com.example.calculator

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_hub)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnGoToCalculator = findViewById<Button>(R.id.btn_calculator)
        btnGoToCalculator.setOnClickListener {
            val calculatorIntent = Intent(this, CalculatorActivity::class.java)
            startActivity(calculatorIntent)
        }


        val btnGoToPlayer = findViewById<Button>(R.id.btn_player)
        btnGoToPlayer.setOnClickListener {
            val playerIntent = Intent(this, MediaPlayerActivity::class.java)
            startActivity(playerIntent)
        }

        val btnGoToLocation = findViewById<Button>(R.id.btn_Location)
        btnGoToLocation.setOnClickListener {
            val locationIntent = Intent(this, LocationActivity::class.java)
            startActivity(locationIntent)
        }
    }
}