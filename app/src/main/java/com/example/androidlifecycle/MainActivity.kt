package com.example.androidlifecycle

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.hardware.usb.UsbManager
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.core.content.ContextCompat

class MainActivity : ComponentActivity() {

    private val TAG = "LifecycleLogger"
    private lateinit var lifecycleTextView: TextView
    private lateinit var restartButton: Button
    private lateinit var usbReceiver: UsbReceiver
    // Flag to track USB connection state
    private var isUsbConnected = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        Log.d(TAG, "MONANG:: Testing Branch onStop Commit2")

        lifecycleTextView = findViewById(R.id.lifecycleTextView)
        restartButton = findViewById(R.id.restartButton)

        Log.d(TAG, "MONANG:: onCreate called")
        lifecycleTextView.text = "onCreate called"

        // In MainActivity's onCreate method
        usbReceiver = UsbReceiver(this) // Pass MainActivity instance here
        val filter = IntentFilter().apply {
            addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED)
            addAction(UsbManager.ACTION_USB_DEVICE_DETACHED)
        }
        registerReceiver(usbReceiver, filter)


        restartButton.setOnClickListener {
            restartApp()
        }
    }

    // Method to handle USB connection state
    fun onUsbConnectionChanged(isConnected: Boolean) {
        isUsbConnected = isConnected
        Log.d(TAG, "USB connection state changed: $isConnected")

        // Optionally, restart the app if USB is detached
        if (!isConnected) {
            restartApp()
        }
    }

    // Called when USB connection changes (attached or detached)
    fun onConnectDev(device: android.hardware.usb.UsbDevice?, isConnect: Boolean) {
        try {
            Log.d(TAG, "MONANG:: USB device onConnectDev isUsbConnected=$isUsbConnected")

            // Update connection status
            isUsbConnected = isConnect

            // Log connection or disconnection
            if (isConnect) {
                Log.d(TAG, "MONANG:: USB device connected: ${device?.deviceName}")
            } else {
                Log.d(TAG, "MONANG:: USB device detached: ${device?.deviceName}")
            }

            // Just log the state, no restart logic here
            if (!isConnect) {
                Log.d(TAG, "MONANG:: USB device detached, not restarting app")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error in onConnectDev: ${e.message}")
        }
    }


    override fun onStart() {
        super.onStart()
        Log.d(TAG, "MONANG:: onStart called")
        lifecycleTextView.text = "onStart called"
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "MONANG:: onResume called")
        lifecycleTextView.text = "onResume called"
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "MONANG:: onPause called")
        lifecycleTextView.text = "onPause called"
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop called")
        lifecycleTextView.text = "onStop called"
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "MONANG:: onDestroy called")
        lifecycleTextView.text = "onDestroy called"
        try {
            unregisterReceiver(usbReceiver)
        } catch (e: IllegalArgumentException) {
            Log.e(TAG, "MONANG:: Receiver already unregistered: ${e.message}")
        }
    }

    private fun restartApp() {
        Log.d(TAG, "MONANG:: Restarting app")

        // Delay restart by 1 second to ensure USB device handling is finished
        Handler().postDelayed({
            // Get the launch intent for the app
            val intent = packageManager.getLaunchIntentForPackage(packageName)
            intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)

            if (intent != null) {
                startActivity(intent)
                Log.d(TAG, "MONANG:: Relaunching app")
            }
        }, 2000) // Delay for 1000 milliseconds (1 second)
    }



}
