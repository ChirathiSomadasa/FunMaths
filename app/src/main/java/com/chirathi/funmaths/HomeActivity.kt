package com.chirathi.funmaths

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class HomeActivity : AppCompatActivity() {

    private lateinit var tvHighScore: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)// Set the layout for this activity

        tvHighScore =
            findViewById<TextView>(R.id.tvHighScore)// Initialize TextView to display high score
        tvHighScore.text =
            "High Score : ${getHighScore()}"// Set the text of tvHighScore to display the high score

        val add =
            findViewById<Button>(R.id.btn_add)// Initialize and set click listener for the 'Addition' button
        add.setOnClickListener {
            val intent = Intent(
                this,
                AdditionActivity::class.java
            )// Create intent to navigate to AdditionActivity
            startActivity(intent)// Start AdditionActivity
        }

        val sub =
            findViewById<Button>(R.id.btn_sub)// Initialize and set click listener for the 'Subtraction' button
        sub.setOnClickListener {
            val intent = Intent(
                this,
                SubtractionActivity::class.java
            )// Create intent to navigate to SubtractionActivity
            startActivity(intent)// Start SubtractionActivity
        }

        val multiply =
            findViewById<Button>(R.id.btn_multiply)// Initialize and set click listener for the 'Multiplication' button
        multiply.setOnClickListener {
            val intent = Intent(
                this,
                MultiplicationActivity::class.java
            )// Create intent to navigate to MultiplicationActivity
            startActivity(intent)// Start MultiplicationActivity
        }

        val division =
            findViewById<Button>(R.id.btn_division)// Initialize and set click listener for the 'Division' button
        division.setOnClickListener {
            val intent = Intent(
                this,
                DivisionActivity::class.java
            )// Create intent to navigate to DivisionActivity
            startActivity(intent)// Start DivisionActivity
        }

        val quiz =
            findViewById<Button>(R.id.btn_quiz)// Initialize and set click listener for the 'Quiz' button
        quiz.setOnClickListener {
            val intent =
                Intent(this, QuizActivity::class.java)// Create intent to navigate to QuizActivity
            startActivity(intent)// Start QuizActivity
        }
    }

    override fun onResume() {
        super.onResume()
        tvHighScore.text =
            "High Score : ${getHighScore()}"// Set the text of tvHighScore to display the high score
    }

    // Function to get shared preferences
    private fun getPref(): SharedPreferences? {
        return getSharedPreferences("com.chirathi.funmaths", 0)
    }

    // Function to get the high score from shared preferences
    private fun getHighScore(): Int {
        return getPref()!!.getInt("score", 0)
    }
}