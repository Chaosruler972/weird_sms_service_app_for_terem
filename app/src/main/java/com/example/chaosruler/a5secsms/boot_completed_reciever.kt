package com.example.chaosruler.a5secsms

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

/**
 * Created by chaosruler on 12/10/17.
 */
class boot_completed_reciever : BroadcastReceiver()
{
    override fun onReceive(context: Context?, intent: Intent?)
    {
        if(intent!=null && context!=null)  // autocast to Intent and Context
        {
            if (context.getString(R.string.boot_completed) == intent.action)
            {
                var service_intent = Intent(context,sms_service::class.java)
                service_intent.putExtra(context.getString(R.string.phone_no_key),context.getString(R.string.default_phone_no))
                context.startService(service_intent)
            }
        }
    }
}