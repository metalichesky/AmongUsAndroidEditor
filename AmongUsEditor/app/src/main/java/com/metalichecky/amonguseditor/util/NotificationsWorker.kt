package com.metalichecky.amonguseditor.util

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.metalichecky.amonguseditor.R
import com.metalichecky.amonguseditor.ui.activity.MainActivity
import java.io.IOException
import java.net.URL


class NotificationsWorker(var context: Context, var workerParams: WorkerParameters) : CoroutineWorker(context, workerParams) {
    companion object {
        const val TITLE = "TITLE"
        const val TEXT = "TEXT"
        const val IMAGE_URL = "IMAGE_URL"
        const val LAUNCH_APP = "LAUNCH_APP"
        const val SHOW_IN_APP = "LAUNCH_APP"
    }

    override suspend fun doWork(): Result {
        showNotifications()
        return Result.success()
    }

    private fun showNotifications() {
        val icon = BitmapFactory.decodeResource(applicationContext.resources, R.mipmap.ic_launcher)
        val title = workerParams.inputData.getString(TITLE)
        val text = workerParams.inputData.getString(TEXT)
        val imageUrl = workerParams.inputData.getString(IMAGE_URL)
        val launchApp = workerParams.inputData.getString(LAUNCH_APP)?.toBoolean() ?: true
        val showInApp = workerParams.inputData.getString(SHOW_IN_APP)?.toBoolean() ?: true

        val intent = if (launchApp) {
            Intent(applicationContext, MainActivity::class.java).apply{
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                putExtra(MainActivity.EXTRA_MESSAGE_TITLE, title)
                putExtra(MainActivity.EXTRA_MESSAGE_TEXT, text)
                putExtra(MainActivity.EXTRA_SHOW_MESSAGE, showInApp)
            }
        } else {
            null
        }
        val pendingIntent = PendingIntent.getActivity(applicationContext, 0, intent, PendingIntent.FLAG_ONE_SHOT)

        val notificationBuilder = NotificationCompat.Builder(applicationContext, "channel_id")
            .setContentTitle(title)
            .setContentText(text)
            .setAutoCancel(true)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            .setContentIntent(pendingIntent)
            .setContentInfo(title)
            .setLargeIcon(icon)
//            .setColor(Color.BLUE)
            .setDefaults(Notification.DEFAULT_VIBRATE)
            .setSmallIcon(R.drawable.ic_launcher)

        try {
            if (imageUrl != null && imageUrl.isNotEmpty()) {
                val url = URL(imageUrl)
                val bigPicture = BitmapFactory.decodeStream(url.openConnection().getInputStream())
                notificationBuilder.setStyle(
                    NotificationCompat.BigPictureStyle().bigPicture(bigPicture).setSummaryText(text)
                )
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }


        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Notification Channel is required for Android O and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "channel_id", "channel_name", NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.description = "channel description"
            channel.setShowBadge(true)
            channel.canShowBadge()
            channel.enableLights(true)
            channel.lightColor = Color.RED
            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0, notificationBuilder.build())
    }
}