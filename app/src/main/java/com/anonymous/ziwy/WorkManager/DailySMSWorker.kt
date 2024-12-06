package com.anonymous.ziwy.WorkManager

import android.content.Context
import android.net.Uri
import androidx.work.Worker
import androidx.work.WorkerParameters

class DailySMSWorker(private val context: Context, params: WorkerParameters) : Worker(context, params) {

    override fun doWork(): Result {
        val messages =
            (readMessages(context = context, type = "inbox") + readMessages(
                context = context,
                type = "sent"
            )).sortedBy { it.date }

        println("620555 DailySMSWorker messages: $messages")
        return Result.success()
    }

    private fun readMessages(context: Context, type: String): List<SMSMessage> {
        val messages = mutableListOf<SMSMessage>()
        val cursor = context.contentResolver.query(
            Uri.parse("content://sms/$type"),
            null,
            null,
            null,
            null,
        )
        cursor?.use {
            val indexMessage = it.getColumnIndex("body")
            val indexSender = it.getColumnIndex("address")
            val indexDate = it.getColumnIndex("date")
            val indexRead = it.getColumnIndex("read")
            val indexType = it.getColumnIndex("type")
            val indexThread = it.getColumnIndex("thread_id")
            val indexService = it.getColumnIndex("service_center")

            while (it.moveToNext()) {
                messages.add(
                    SMSMessage(
                        message = it.getString(indexMessage),
                        sender = it.getString(indexSender),
                        date = it.getLong(indexDate),
                        read = it.getString(indexRead).toBoolean(),
                        type = it.getInt(indexType),
                        thread = it.getInt(indexThread),
                        service = it.getString(indexService) ?: ""
                    )
                )
            }
        }
        return messages
    }

}