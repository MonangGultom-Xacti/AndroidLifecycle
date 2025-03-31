package com.example.androidlifecycle

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbManager
import android.util.Log

class UsbReceiver(private val mainActivity: MainActivity) : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        when (intent?.action) {
            UsbManager.ACTION_USB_DEVICE_ATTACHED -> {
                val device: UsbDevice? = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE)
                Log.d("UsbReceiver", "USB device attached: ${device?.deviceName}")
                mainActivity.onConnectDev(device, true)  // Calling onConnectDev with true (connected)
            }
            UsbManager.ACTION_USB_DEVICE_DETACHED -> {
                val device: UsbDevice? = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE)
                Log.d("UsbReceiver", "USB device detached: ${device?.deviceName}")
                mainActivity.onConnectDev(device, false)  // Calling onConnectDev with false (detached)
            }
        }
    }
}

