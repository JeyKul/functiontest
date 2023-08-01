package com.jey.functiontest

import android.Manifest
import android.app.PendingIntent.getActivity
import android.content.Intent
import android.content.pm.PackageManager
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.View
import android.view.WindowManager
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.provider.Settings
import android.provider.Settings.System




class MainActivity : AppCompatActivity() {
    companion object {
        private const val PERMISSION_REQUEST_CODE = 100
        private const val PERMISSION_REQUEST_CODE_WRITE_SETTINGS = 101
    }

    private val permissionLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Settings.System.canWrite(this)) {
                // Write settings permission already granted, proceed with the app
                // Add your code here to handle the app functionality
            } else {
                // User did not grant write settings permission, take appropriate action
                // You can show a message or take appropriate action here
            }
        } else {
            // For devices with API level less than 23, write settings permission is already granted
            // Proceed with the app functionality
        }
    }

    private fun checkWriteSettingsPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.System.canWrite(this)) {
                // Request the user to grant write settings permission
                val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS).apply {
                    data = Uri.parse("package:$packageName")
                }
                permissionLauncher.launch(intent)
            } else {
                // Write settings permission already granted, proceed with the app
                // Add your code here to handle the app functionality
            }
        } else {
            // For devices with API level less than 23, write settings permission is already granted
            // Proceed with the app functionality
        }
    }

    private val PERMISSIONS = arrayOf(
        Manifest.permission.WRITE_SETTINGS,
        Manifest.permission.CAMERA,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
        // Add any other permissions your app requires
    )

private var mediaPlayer: MediaPlayer? = null
    private val handler = Handler()
    private var isVibrationRunning = false
    private val vibrator: Vibrator? by lazy {
        getSystemService(VIBRATOR_SERVICE) as Vibrator?
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnRed = findViewById<Button>(R.id.btnRed)
        val btnGreen = findViewById<Button>(R.id.btnGreen)
        val btnBlue = findViewById<Button>(R.id.btnBlue)
        val btnReceiver = findViewById<Button>(R.id.btnReceiver)
        val btnVibration = findViewById<Button>(R.id.btnVibration)
        val btnMegaCam = findViewById<Button>(R.id.btnMegaCam)
        val btnImageTest = findViewById<Button>(R.id.btnImageTest)


        // Set click listeners for buttons
        btnRed.setOnClickListener { openRedScreen() }
        btnGreen.setOnClickListener { openGreenScreen() }
        btnBlue.setOnClickListener { openBlueScreen() }
        btnReceiver.setOnClickListener {
            if (mediaPlayer == null || !mediaPlayer!!.isPlaying) {
                startAudioPlayback()
            } else {
                stopAudio()
            }
        }
        btnVibration.setOnClickListener {
            toggleVibration()
        }
        btnMegaCam.setOnClickListener { openMegaCam() }
        btnImageTest.setOnClickListener { openImageTest() }
        checkWriteSettingsPermission()
        if (!arePermissionsGranted()) {
            requestPermissions()
        } else {
        }
    }

    private fun arePermissionsGranted(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PERMISSIONS.all { permission ->
                ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
            }
        } else {
            // On devices with API level less than 23, permissions are granted at install time
            true
        }
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {
                // Check if all permissions were granted
                if (grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                    // All permissions granted, proceed with the app
                    // Add your code here to handle the app functionality that requires the permissions
                } else {
                    // Some permissions were denied, handle as needed
                }
            }
        }
    }


    private val brightnessSettingsContract = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Settings.System.canWrite(this)) {
            // User granted write settings permission, proceed with the app
            // Add your code here to handle the app functionality
        } else {
            // User did not grant write settings permission, take appropriate action
            // You can show a message or take appropriate action here
        }

    }

    private fun startVibration() {
        // Check if the device has a vibrator and if the vibrator service is available
        if (vibrator != null && vibrator!!.hasVibrator()) {
            // Define the vibration pattern (2 seconds on, 2 seconds off)
            val pattern = longArrayOf(50, 20, 50, 20, 50, 50, 20, 50, 20, 50, 50, 20, 50, 20, 50, 50, 20, 50, 20, 50, 50, 20, 50, 20, 50, 50, 20, 50, 20, 50, 50, 20, 50, 20, 50, 50, 20, 50, 20, 50, 50, 20, 50, 20, 50, 50, 20, 50, 20, 50, 50, 20, 50, 20, 50, 50, 20, 50, 20, 50, 50, 20, 50, 20, 50)

            // Start the vibration with the defined pattern and -1 for repeating indefinitely
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val vibrationEffect = VibrationEffect.createWaveform(pattern, -1)
                vibrator!!.vibrate(vibrationEffect)
            } else {
                // For devices with API level less than 26
                @Suppress("DEPRECATION")
                vibrator!!.vibrate(pattern, -1)
            }
        }
    }

    private fun stopVibration() {
        vibrator?.cancel()
    }

    private fun toggleVibration() {
        if (isVibrationRunning) {
            stopVibration()
        } else {
            startVibration()
        }
        isVibrationRunning = !isVibrationRunning
    }

    private fun openRedScreen() {
        val intent = Intent(this, RedActivity::class.java)
        startActivity(intent)
    }

    private fun openGreenScreen() {
        val intent = Intent(this, GreenActivity::class.java)
        startActivity(intent)
    }

    private fun openBlueScreen() {
        val intent = Intent(this, BlueActivity::class.java)
        startActivity(intent)
    }

    private fun openImageTest() {
        val intent = Intent(this, ImageTestActivity::class.java)
        startActivity(intent)
    }


    private fun openMegaCam() {
        val intent = Intent(this, MegaCamActivity::class.java)
        startActivity(intent)
    }

    private fun startAudioPlayback() {
        if (mediaPlayer == null) {
            try {
                mediaPlayer = MediaPlayer()
                mediaPlayer?.setDataSource(this, Uri.parse("android.resource://$packageName/${R.raw.mp3_1khz}"))
                mediaPlayer?.isLooping = true
                mediaPlayer?.setAudioAttributes(AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_VOICE_COMMUNICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                    .build())
                mediaPlayer?.prepare()
                mediaPlayer?.start()

                val audioManager = getSystemService(AUDIO_SERVICE) as AudioManager
                audioManager.mode = AudioManager.MODE_IN_COMMUNICATION
                audioManager.isSpeakerphoneOn = false
            } catch (e: Exception) {
                e.printStackTrace()
                // Handle the exception, show a message, or take appropriate action
            }
        }
    }

    private fun stopAudio() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null

        val audioManager = getSystemService(AUDIO_SERVICE) as AudioManager
        audioManager.mode = AudioManager.MODE_NORMAL
        audioManager.isSpeakerphoneOn = true
    }
}
