package com.empcloud.empmonitor.utils.broadcast_services

import android.content.ActivityNotFoundException
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat.startActivityForResult
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Status
import java.security.MessageDigest
import java.util.regex.Pattern

class OtpBroadcastReciever:BroadcastReceiver() {

    var otpListener: OTPReceiveListener? = null
    override fun onReceive(context: Context?, intent: Intent?) {

        Log.d("ResponseChecking","Inside Reciever")
        if (SmsRetriever.SMS_RETRIEVED_ACTION == intent?.action) {
            Log.d("ResponseChecking", "SMS retrieved action received")
            val extras = intent.extras
            val status = extras?.get(SmsRetriever.EXTRA_STATUS) as Status
            Log.d("ResponseChecking", "$status")
            when (status.statusCode) {

                CommonStatusCodes.SUCCESS -> {

                    val message = extras.get(SmsRetriever.EXTRA_SMS_MESSAGE) as String
                    message.let {

                        val pattern = Pattern.compile("\\d{6}")
                        val matcher = pattern.matcher(it)
                        if (matcher.find()){

                            val otp = matcher.group(0)
                            Log.d("ResponseChecking",message)
                            if (otp != null) {
                                otpListener?.onOTPReceived(otp)
                            }

                        }
                    }


                }

                CommonStatusCodes.TIMEOUT -> {

                    Toast.makeText(context, "Failed to recieve otp", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }


    fun initListener(listener: OTPReceiveListener) {
        otpListener = listener
    }

    interface OTPReceiveListener {
        fun onOTPReceived(otp: String)
        fun onOTPTimeOut()

    }




}