package com.febrian.covidapp.news.utils

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.BitmapFactory
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.febrian.covidapp.MainActivity
import com.febrian.covidapp.R
import com.febrian.covidapp.home.HomeFragment
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class SendNotification(val resources : Resources) : BroadcastReceiver() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(c: Context?, intent: Intent?) {
        val jakartaZone = ZoneId.of("Asia/Jakarta")
        val asiaJakartaCurrentDate = ZonedDateTime.now(jakartaZone)

        val asiaJakarta = asiaJakartaCurrentDate.format(DateTimeFormatter.ofPattern("HH:mm"))
        val intent = Intent(c, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(c, 0, intent, 0)
        if (asiaJakarta == "17.30") {

            val mNotificationManager =
                c?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val mBuilder = c.let {
                NotificationCompat.Builder(it, HomeFragment.CHANNEL_ID)
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(R.drawable.ic_baseline_notifications_24)
                    .setLargeIcon(
                        BitmapFactory.decodeResource(
                            resources,
                            R.drawable.ic_baseline_notifications_24
                        )
                    )
                    .setContentTitle("Data Covid Updated!")
                    .setContentText("Look at the covid data update")
                    .setSubText("Covid Update")
                    .setAutoCancel(true)
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                /* Create or update. */
                val channel = NotificationChannel(
                    HomeFragment.CHANNEL_ID,
                    HomeFragment.CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT
                )
                channel.description = HomeFragment.CHANNEL_NAME
                mBuilder.setChannelId(HomeFragment.CHANNEL_ID)
                mNotificationManager.createNotificationChannel(channel)
            }

            val notification = mBuilder.build()
            mNotificationManager.notify(HomeFragment.NOTIFICATION_ID, notification)
        }
    }

    fun setRepeat(context: Context){
       val alarm : AlarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, SendNotification::class.java)

        val calendar = Calendar.getInstance()
        calendar[Calendar.HOUR_OF_DAY] = 17
        calendar[Calendar.MINUTE] = 25
        calendar[Calendar.SECOND] = 0

        val pendingIntent = PendingIntent.getBroadcast(context, 1, intent, 0)

        alarm.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }
}