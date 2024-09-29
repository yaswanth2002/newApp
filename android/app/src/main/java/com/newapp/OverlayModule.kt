package com.newapp

import android.app.Service
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.bridge.Promise
import android.content.Context

class OverlayModule(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {

    override fun getName(): String {
        return "OverlayModule"
    }

    // Check if overlay permission is granted
    @ReactMethod
    fun canDrawOverlays(promise: Promise) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            promise.resolve(Settings.canDrawOverlays(reactApplicationContext))
        } else {
            promise.resolve(true)
        }
    }

    // Return the package name to React Native
    @ReactMethod
    fun getPackageName(promise: Promise) {
        promise.resolve(reactApplicationContext.packageName)
    }

    // Request the overlay permission if not granted
    @ReactMethod
    fun requestOverlayPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:" + reactApplicationContext.packageName)
            )
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            reactApplicationContext.startActivity(intent)
        }
    }

    // Start the OverlayService
    @ReactMethod
    fun startOverlayService() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Settings.canDrawOverlays(reactApplicationContext)) {
                val serviceIntent = Intent(reactApplicationContext, OverlayService::class.java)
                serviceIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                reactApplicationContext.startService(serviceIntent)
            } else {
                Toast.makeText(reactApplicationContext, "Overlay permission is required", Toast.LENGTH_LONG).show()
            }
        }
    }

    // Stop the OverlayService
    @ReactMethod
    fun stopOverlayService() {
        val serviceIntent = Intent(reactApplicationContext, OverlayService::class.java)
        reactApplicationContext.stopService(serviceIntent)
    }
}
