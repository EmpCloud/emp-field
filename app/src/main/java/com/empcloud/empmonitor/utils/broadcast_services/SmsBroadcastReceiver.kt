package com.empcloud.empmonitor.utils.broadcast_services
import android.content.ActivityNotFoundException
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.SmsMessage
import android.util.Log
import android.widget.Toast
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Status

class SmsBroadcastReceiver : BroadcastReceiver() {

    interface SmsBroadcastListener {
        fun onSmsRetrieved(consentIntent: Intent)
        fun onTimeout()
//        fun onMessageRecieved(message: String?)


    }

    var listener: SmsBroadcastListener? = null

    override fun onReceive(context: Context, intent: Intent) {
        if (SmsRetriever.SMS_RETRIEVED_ACTION == intent.action) {
            Log.e("MainActivity", "inside reciever1")
            val extras = intent.extras
            if (extras != null) {
                Log.e("MainActivity", "inside reciever2 $extras")
//                val status = extras.get(SmsRetriever.EXTRA_STATUS) as? Status
                val status = extras[SmsRetriever.EXTRA_STATUS] as? Status

                status?.let {
                    Log.e("MainActivity", "inside reciever3")
                    Log.e("MainActivity", status.statusCode.toString())
                    when (it.statusCode) {

                        CommonStatusCodes.SUCCESS -> {
                            // Retrieve the SMS message
                            val consentIntent = extras.getParcelable<Intent>(SmsRetriever.EXTRA_CONSENT_INTENT)
                            Log.e("MainActivity", consentIntent.toString())
                            try {
                                consentIntent?.let { intent ->
                                    Log.e("MainActivity", "send intent")
                                    try {
                                        listener?.onSmsRetrieved(consentIntent)
                                    } catch (e: ActivityNotFoundException) {
                                        Log.e("MainActivity", "Activity Not Found: ${e.localizedMessage}")
                                    }

                                }
                            }catch (e: ActivityNotFoundException){

                                e.printStackTrace()
                            }


//                            if (extras != null){
//                                val message = extras.getString(SmsRetriever.EXTRA_SMS_MESSAGE)
//                                if (message != null)  listener?.onMessageRecieved(message)
//                                else
//                                    extras?.keySet()?.forEach { key ->
//                                        Log.d("SMSRetriever", "Key: $key Value: ${extras[key]}")
//                                    }
//                                    Toast.makeText(context,"Message cannot be extracted",Toast.LENGTH_SHORT).show()
//
//                            } else {
//                                Toast.makeText(context,"Extras is null",Toast.LENGTH_SHORT).show()
//                            }
                        }
                        CommonStatusCodes.TIMEOUT -> {
                            listener?.onTimeout()
                        }
                        else -> {
                            Log.e("SmsBroadcastReceiver", "Unexpected status code: ${status.statusCode}")
                        }
                    }
                }
            }
        }
    }


}
