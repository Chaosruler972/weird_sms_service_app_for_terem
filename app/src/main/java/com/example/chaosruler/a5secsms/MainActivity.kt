package com.example.chaosruler.a5secsms

import android.Manifest
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import android.content.pm.PackageManager
import android.support.v4.content.ContextCompat
import android.text.InputType
import android.support.v4.app.ActivityCompat
import android.widget.Button


class MainActivity : AppCompatActivity()
{
    private var serv_intent:Intent? = null
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        request_premission_subroutine(Manifest.permission.READ_PHONE_STATE,resources.getInteger(R.integer.premission_phone))
    }


    private fun fail_subroutine()
    {
        main_phoneno.inputType = InputType.TYPE_CLASS_TEXT
        main_phoneno.isEnabled = false
        main_phoneno.setText(getString(R.string.insufficent_pre))
        main_stop_btn.visibility = Button.INVISIBLE
        main_start_btn.visibility = Button.INVISIBLE
    }

    private fun init_subroutine()
    {
        init_buttons()
    }

    private fun init_service()
    {
        var service_intent = Intent(this,sms_service::class.java)
        service_intent.putExtra(getString(R.string.phone_no_key),main_phoneno.text.toString())
        serv_intent = service_intent
        startService(service_intent)
    }

    private fun request_premission_subroutine(premission:String,code:Int)
    {
        val result = ContextCompat.checkSelfPermission(this, premission)
        if (result == PackageManager.PERMISSION_GRANTED)
        {
            request_next_permission(code)
        }
        else
        {
            ActivityCompat.requestPermissions(this, arrayOf(premission), code)

        }
    }

    private fun request_next_permission(code:Int)
    {
        when(code)
        {
            resources.getInteger(R.integer.premission_phone)->
            {
                request_premission_subroutine(Manifest.permission.SEND_SMS,resources.getInteger(R.integer.permission_sms))
            }
            resources.getInteger(R.integer.permission_sms)->
            {
                init_subroutine()
            }
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray)
    {
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
        {
            request_next_permission(requestCode)
        }
        else
        {
            fail_subroutine()
        }
    }
    private fun init_buttons()
    {
        main_stop_btn.isEnabled = false
        /*
        start subroutine
         */
        main_start_btn.setOnClickListener({
            if(main_phoneno.text.isEmpty())
            {
                Toast.makeText(this,getString(R.string.empty_phone),Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            main_phoneno.isEnabled=false // user can't change phone number now
            main_start_btn.isEnabled = false
            main_stop_btn.isEnabled = true
            init_service()
        })

        /*
            stop subroutine
         */
        main_stop_btn.setOnClickListener({
            stopService(serv_intent)
            serv_intent = null
            main_phoneno.isEnabled = true // user can change phone number now
            main_start_btn.isEnabled = true
            main_stop_btn.isEnabled = false
        })


    }

}
