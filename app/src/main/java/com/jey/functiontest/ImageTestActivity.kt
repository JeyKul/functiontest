package com.jey.functiontest

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class ImageTestActivity : AppCompatActivity() {

    private lateinit var imageView: ImageView
    private val images = listOf(
        R.drawable.acc_test_image,  // Replace with your image resource IDs
        R.drawable.bing_chilling,  // Add more image resource IDs as needed
        R.drawable.nishiki_yakuza,
        R.drawable.kato
    )

    private var currentImageIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_test)

        imageView = findViewById(R.id.imageView)
        imageView.setImageResource(images[currentImageIndex])

        imageView.setOnClickListener {
            showNextImage()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("currentImageIndex", currentImageIndex)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        currentImageIndex = savedInstanceState.getInt("currentImageIndex", 0)
        imageView.setImageResource(images[currentImageIndex])
    }

    private fun showNextImage() {
        currentImageIndex = (currentImageIndex + 1) % images.size
        imageView.setImageResource(images[currentImageIndex])
    }
}