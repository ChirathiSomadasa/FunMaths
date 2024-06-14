package com.chirathi.funmaths

import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    // Declaring variables for views
    private lateinit var title: TextView
    private lateinit var tvIcon: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            // Set the window to fullscreen
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        requestWindowFeature(Window.FEATURE_NO_TITLE)// Request to hide the title bar
        setContentView(R.layout.activity_main)
        title = findViewById(R.id.title) // Initialize the views
        tvIcon = findViewById<TextView>(R.id.tvIcon) // Initialize the views
        tvIcon.setY(dpToPx(50).toFloat()) // Move the tvIcon TextView down by 50 density-independent pixels (dp)
        // Set initial properties for the title TextView
        title.setAlpha(0f)
        title.setScaleX(0f)
        title.setScaleY(0f)
        // Animate the tvIcon TextView to its original position
        tvIcon.animate().translationY(0f).translationX(0f).setDuration(800).withEndAction {
            // Animate the title TextView to scale up and fade in
            title.animate().scaleY(1f).scaleX(1f).alpha(1f).setDuration(800).withEndAction {
                navigateToHomeActivity()// After animation completion, navigate to the HomeActivity
            }
        }
    }

    // Function to navigate to the HomeActivity
    private fun navigateToHomeActivity() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }

    // Function to convert density-independent pixels (dp) to pixels (px)
    private fun dpToPx(dp: Int): Int {
        return (dp * Resources.getSystem().displayMetrics.density).toInt()
    }
}
