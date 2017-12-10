package com.example.chaosruler.a5secsms

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import android.util.Log
import java.lang.Thread.sleep

class sms_service : Service()
{

    inner class the_binder : Binder() {
        @Suppress("PropertyName")
        internal val service_label: sms_service
            get() = this@sms_service
    }

    private val SmsManager = android.telephony.SmsManager.getDefault()
    private val mp = MediaPlayer()

    private val binder = the_binder()
    private var isRunning:Boolean = false

    init
    {
        mp.setOnPreparedListener({
            mp.start()

            mp.setOnCompletionListener{
                mp.stop()
                 mp.reset()}
        })
    }
    override fun onBind(intent: Intent): IBinder?
    {

        return binder
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int
    {
        start_service(intent)
        return super.onStartCommand(intent, flags, startId)
    }


    override fun onDestroy()
    {
        mp.release()
        isRunning = false
        super.onDestroy()
    }

    private fun start_service(intent: Intent?)
    {
        Log.d("sms_service","On start called")
        if(intent!=null)
        {
            isRunning = true
            var phone_no: String? = intent.getStringExtra(getString(R.string.phone_no_key))
            if(phone_no!=null) {
                Thread({ keep_running(phone_no) }).start()
            }

        }
    }


    private fun keep_running(phone_no:String)
    {
        while(isRunning)
        {

            try
            {
                sleep((resources.getInteger(R.integer.sync_time)*resources.getInteger(R.integer.millis_in_sec)).toLong() )
            }
            catch (e:InterruptedException)
            {
                Log.d("sms_service","done sleeping, woke up!")
            }
            if(send_sms(phone_no,getString(R.string.message)))
            {
                play_sound()
            }
        }
    }



    public fun send_sms(phone_no:String,message:String):Boolean
    {

        return if(!phone_no.isEmpty() && !message.isEmpty())
        {
            SmsManager.sendTextMessage(phone_no, null, message, null, null)
            true
        }
        else
        {
            Log.d("sms_service", "error with either phone no or message is empty")
            false
        }
    }

    public fun play_sound()
    {
        try {

            if (mp.isPlaying) {
                Log.d("sms_service", "audio is playing still!")
                return
            }
            mp.setDataSource(getString(R.string.sound_link))
            mp.prepare()

        }
        catch (e:IllegalStateException)
        {
            Log.d("sms_service","Something is wrong")
        }
    }

}
