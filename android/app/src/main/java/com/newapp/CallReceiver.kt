package com.newapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.TelephonyManager

class CallReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action.equals(TelephonyManager.ACTION_PHONE_STATE_CHANGED)) {
            val state = intent.getStringExtra(TelephonyManager.EXTRA_STATE)
            if (state == TelephonyManager.EXTRA_STATE_RINGING) {
                // Start the OverlayService when a call is ringing
                val serviceIntent = Intent(context, OverlayService::class.java)
                context.startService(serviceIntent)
            }
        }
    }
}
