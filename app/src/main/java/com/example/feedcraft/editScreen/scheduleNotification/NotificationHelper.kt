package com.example.feedcraft.editScreen.scheduleNotification

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.feedcraft.R
import com.example.feedcraft.UIApplication
import com.example.feedcraft.mainScreen.MainActivity

//https://dev.to/blazebrain/building-a-reminder-app-with-local-notifications-using-workmanager-api-385f

class NotificationHelper(val context: Context) {
    private val CHANNEL_ID = "reminder_channel_id"
    private val NOTIFICATION_ID = 1
    private fun createNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_ID, NotificationManager.IMPORTANCE_DEFAULT ).apply {
                description = "Reminder Channel Description"
            }
            val notificationManager =  context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun createNotification(title: String, message: String){
        createNotificationChannel()

        val intent = Intent(context, MainActivity:: class.java).apply{
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)
        val icon = if(UIApplication.addPictureInNotification) UIApplication.tempBitmap else null
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher)
            .setLargeIcon(icon)
            .setContentTitle(title)
            .setContentText(message)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        if (icon!=null)
            notification.setStyle(NotificationCompat.BigPictureStyle().bigPicture(icon).bigLargeIcon(null))

        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.i("notification", "not granted")
            return
        }
        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, notification.build())

    }
}