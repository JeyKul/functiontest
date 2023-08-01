package com.jey.functiontest

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import android.provider.Settings
import android.view.View.SYSTEM_UI_FLAG_FULLSCREEN

@Suppress("DEPRECATION")
class RedActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setMaxBrightness()
        // Set the background color to red
        window.decorView.setBackgroundColor(Color.RED)
        window.decorView.systemUiVisibility = SYSTEM_UI_FLAG_FULLSCREEN

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        // Hide the ActionBar
        supportActionBar?.hide()

        // Make sure to set the content view after updating the background color
        setContentView(R.layout.activity_color)
    }
    private fun setMaxBrightness() {
        val brightness = 255 // Maximum brightness value (0 to 255)
        val settings = Settings.System.CONTENT_URI
        Settings.System.putInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS, brightness)
    }
    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}
