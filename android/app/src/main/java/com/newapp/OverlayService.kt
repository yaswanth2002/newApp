package com.newapp

import android.app.Service
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast

class OverlayService : Service() {

    private lateinit var windowManager: WindowManager
    private lateinit var overlayView: LinearLayout

    override fun onCreate() {
        super.onCreate()

        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager

        // Programmatically create a LinearLayout for the overlay view
        overlayView = LinearLayout(this)
        overlayView.orientation = LinearLayout.VERTICAL
        overlayView.setBackgroundColor(0xFFFFFFFF.toInt()) // White background

        // Create and add a TextView
        val textView = TextView(this)
        textView.text = "Overlay message here"
        overlayView.addView(textView)

        // Create and add a Button to close the overlay
        val closeButton = Button(this)
        closeButton.text = "Close"
        overlayView.addView(closeButton)

        // Set the layout parameters for the overlay
        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            } else {
                WindowManager.LayoutParams.TYPE_PHONE
            },
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )

        // Set the position of the overlay
        params.gravity = Gravity.TOP or Gravity.LEFT
        params.x = 0
        params.y = 100

        // Add the overlay view to the window
        windowManager.addView(overlayView, params)

        // Set up a button click listener to close the overlay
        closeButton.setOnClickListener {
            Toast.makeText(this, "Overlay Closed", Toast.LENGTH_SHORT).show()
            stopSelf() // Close the overlay when the button is clicked
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::windowManager.isInitialized && ::overlayView.isInitialized) {
            windowManager.removeView(overlayView)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onBind(intent: Intent?) = null
}
