package com.jey.functiontest

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity

@Suppress("DEPRECATION")
class GreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set the background color to red
        window.decorView.setBackgroundColor(Color.GREEN)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        // Hide the ActionBar
        supportActionBar?.hide()

        // Make sure to set the content view after updating the background color
        setContentView(R.layout.activity_color)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

}
