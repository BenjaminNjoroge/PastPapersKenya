package com.pastpaperskenya.app.presentation

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.widget.RemoteViews
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import com.pastpaperskenya.app.R
import com.pastpaperskenya.app.business.model.mpesa.PaymentFcm
import com.pastpaperskenya.app.business.util.Constants
import com.pastpaperskenya.app.presentation.main.MainActivity
import com.pastpaperskenya.app.presentation.main.cart.checkout.MpesaListener
import okhttp3.internal.notify


class MessagingService constructor(
    private val mpesaListener: MpesaListener): FirebaseMessagingService() {

    val channel_id= Constants.NOTIFICATION_CHANNEL


    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        val title= message.data.get("title")
        val content= message.data.get("body")
        val defaultSound= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val intent = Intent(this, MainActivity::class.java)
        intent.flags= Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP

        val pendingIntent= PendingIntent.getActivity(applicationContext,
            0,intent, PendingIntent.FLAG_UPDATE_CURRENT)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            checkNotificationChannel("1")
        }

        val notification= NotificationCompat.Builder(applicationContext, "1")
            .setSmallIcon(R.drawable.small_logo)
            .setContentTitle(title)
            .setContentText(content)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setSound(defaultSound)

        val notificationManager= getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(1,notification.build())

        //FirebaseMessaging.getInstance().unsubscribeFromTopic(topicId)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun checkNotificationChannel(channel_id: String) {
        val notificationChannel= NotificationChannel(channel_id, getString(R.string.app_name),
            NotificationManager.IMPORTANCE_HIGH
        )
        notificationChannel.description= "CHANNEL_DESCRIPTION"
        notificationChannel.enableLights(true)
        notificationChannel.enableVibration(true)

        val notificationManager= getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(notificationChannel)
    }



}