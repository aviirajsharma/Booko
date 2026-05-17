package com.avirajsharma.booko.data.repository

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.avirajsharma.booko.data.local.BookDao
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class DownloadCompletedReceiver : BroadcastReceiver() {

    @Inject
    lateinit var bookDao: BookDao

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == DownloadManager.ACTION_DOWNLOAD_COMPLETE) {
            val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1L)
            if (id != -1L) {
                scope.launch {
                    bookDao.updateDownloadStatus(id, true)
                }
            }
        }
    }
}
