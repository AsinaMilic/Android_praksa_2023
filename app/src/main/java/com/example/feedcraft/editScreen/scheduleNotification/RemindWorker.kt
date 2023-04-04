package com.example.feedcraft

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.feedcraft.editScreen.scheduleNotification.NotificationHelper

//https://dev.to/blazebrain/building-a-reminder-app-with-local-notifications-using-workmanager-api-385f

class ReminderWorker(val context: Context, params: WorkerParameters) : Worker(context, params){
    override fun doWork(): Result {
        NotificationHelper(context).createNotification(
            inputData.getString("title").toString(),
            inputData.getString("message").toString())

        return Result.success()
    }
}