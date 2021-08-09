package com.febrian.covidapp

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.febrian.covidapp.home.HomeFragment.Companion.TAG
import com.huawei.hms.push.HmsMessageService
import com.huawei.hms.push.RemoteMessage

class PushKitService : HmsMessageService() {

    override fun onMessageReceived(message: RemoteMessage?) {
        super.onMessageReceived(message)

        Log.i(TAG, "onMessageReceived is called")

        // Check whether the message is empty.
        if (message == null)
        {
            Log.e(TAG, "Received message entity is null!")
            return
        }

        // Obtain the message content.
        Log.i(TAG, """getData: ${message.data}        
            getFrom: ${message.from}        
            getTo: ${message.to}        
            getMessageId: ${message.messageId}
            getSendTime: ${message.sentTime}           
            getDataMap: ${message.dataOfMap}
            getMessageType: ${message.messageType}   
            getTtl: ${message.ttl}        
            getToken: ${message.token}""".trimIndent())

        val judgeWhetherIn10s = false
        // If the message is not processed within 10 seconds, create a job to process it.
        if (judgeWhetherIn10s) {
            startWorkManagerJob(message)
        } else {
            // Process the message within 10 seconds.
            processWithin10s(message)
        }
    }
    private fun startWorkManagerJob(message: RemoteMessage?) {
        Log.d(TAG, "Start new Job processing.")
    }
    private fun processWithin10s(message: RemoteMessage?) {
        Log.d(TAG, "Processing now.")
    }
}