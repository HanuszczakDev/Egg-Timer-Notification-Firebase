/*
 * Copyright (C) 2019 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hanuszczak.eggtimernotificationsfcm.util

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.core.app.NotificationCompat
import com.hanuszczak.eggtimernotificationsfcm.MainActivity
import com.hanuszczak.eggtimernotificationsfcm.MyFirebaseMessagingService
import com.hanuszczak.eggtimernotificationsfcm.R
import com.hanuszczak.eggtimernotificationsfcm.receiver.SnoozeReceiver
import java.io.IOException
import java.net.URL

// Notification ID.
private val NOTIFICATION_ID = 0
private val REQUEST_CODE = 0
private val FLAGS = 0

// TODO: Step 1.1 extension function to send messages (GIVEN)
/**
 * Builds and delivers the notification.
 *
 * @param messageTitle FCM message body received.
 * @param messageBody FCM message body received.
 * @param messageImgUrl FCM message body received.
 * @param context, activity context.
 */
fun NotificationManager.sendNotification(messageTitle: String, messageBody: String, messageImgUrl: Uri, applicationContext: Context) {


    // TODO: Step 1.11 create intent
    val contentIntent = Intent(applicationContext, MainActivity::class.java)

    // TODO: Step 1.12 create PendingIntent
    val contentPendingIntent = PendingIntent.getActivity(
        applicationContext,
        NOTIFICATION_ID,
        contentIntent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

// TODO: Step 2.0 add style
//    val notificationImage = BitmapFactory.decodeResource(
//        applicationContext.resources,
//        R.drawable.cooked_egg
//    )
    val notificationImage: Bitmap = try {
        val url = URL(messageImgUrl.toString())
        BitmapFactory.decodeStream(url.openConnection().getInputStream())
    } catch (e: IOException) {
        Log.e(MyFirebaseMessagingService.TAG, "BitmapFactory.decodeStream: ${e.localizedMessage}")
        BitmapFactory.decodeResource(
            applicationContext.resources,
            R.drawable.cooked_egg
        )
    }

    val bigPicStyle = NotificationCompat.BigPictureStyle()
        .bigPicture(notificationImage)
        .bigLargeIcon(null)

    // TODO: Step 2.2 add snooze action
    val snoozeIntent = Intent(applicationContext, SnoozeReceiver::class.java)
    val snoozePendingIntent: PendingIntent =
        PendingIntent.getBroadcast(applicationContext, REQUEST_CODE, snoozeIntent, PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE)

    // TODO: Step 1.2 get an instance of NotificationCompat.Builder
    // Build the notification
    val builder = NotificationCompat.Builder(
        applicationContext,
        // TODO: Step 1.8 use the new 'breakfast' notification channel
        applicationContext.getString(R.string.egg_notification_channel_id)
    )
        // TODO: Step 1.3 set title, text and icon to builder
        .setSmallIcon(R.drawable.cooked_egg)
        .setContentTitle(messageTitle)
        .setContentText(messageBody)
        // TODO: Step 1.13 set content intent
        .setContentIntent(contentPendingIntent)
        // TODO: Step 2.1 add style to builder
        .setStyle(bigPicStyle)
        .setLargeIcon(notificationImage)
        // TODO: Step 2.3 add snooze action
        .addAction(
           R.drawable.egg_icon,
            applicationContext.getString(R.string.snooze),
            snoozePendingIntent
        )
        // TODO: Step 2.5 set priority
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setAutoCancel(true)

    // TODO Step 1.4 call notify
    // Deliver the notification
    notify(NOTIFICATION_ID, builder.build())
}

// TODO: Step 1.14 Cancel all notifications
/**
 * Cancels all notifications.
 *
 */
fun NotificationManager.cancelNotifications() {
    cancelAll()
}
