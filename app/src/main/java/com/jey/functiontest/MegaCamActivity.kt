package com.jey.functiontest

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Size
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.jey.functiontest.databinding.ActivityMegaCamBinding
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MegaCamActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMegaCamBinding
    private var imageCapture: ImageCapture? = null
    private var cameraLensFacing: Int = CameraSelector.LENS_FACING_BACK
    private lateinit var cameraExecutor: ExecutorService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMegaCamBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cameraExecutor = Executors.newSingleThreadExecutor()

        // Request camera permission
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                PERMISSION_REQUEST_CODE_CAMERA
            )
        }

        binding.btnSwitchCamera.setOnClickListener { toggleCameraLens() }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Set up the preview use case
            val preview = Preview.Builder()
                .setTargetResolution(Size(binding.viewFinder.width, binding.viewFinder.height))
                .build()

            // Set up the image capture use case
            imageCapture = ImageCapture.Builder().build()

            // Select the default camera based on the initial lens facing
            val cameraSelector = CameraSelector.Builder().requireLensFacing(cameraLensFacing).build()

            try {
                // Unbind any existing use cases before binding new ones
                cameraProvider.unbindAll()

                // Bind the camera use cases
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)

            } catch (exc: Exception) {
                // Handle errors here
                Toast.makeText(this, "Camera initialization failed: ${exc.message}", Toast.LENGTH_LONG).show()
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun toggleCameraLens() {
        cameraLensFacing = if (cameraLensFacing == CameraSelector.LENS_FACING_BACK) {
            CameraSelector.LENS_FACING_FRONT
        } else {
            CameraSelector.LENS_FACING_BACK
        }
        startCamera()
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
        imageCapture = null
    }

    companion object {
        private const val PERMISSION_REQUEST_CODE_CAMERA = 101
        private val REQUIRED_PERMISSIONS = arrayOf(android.Manifest.permission.CAMERA)
    }
}
