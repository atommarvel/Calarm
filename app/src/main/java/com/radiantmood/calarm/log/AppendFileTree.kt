package com.radiantmood.calarm.log

import android.app.Activity
import com.radiantmood.calarm.calarm
import com.radiantmood.calarm.core.SimpleActivityLifecycleCallbacks
import timber.log.Timber
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardOpenOption.APPEND
import java.text.SimpleDateFormat
import java.util.*

/**
 * TODO:
 *  - don't let the log file get infinitely large
 *  - add a simple way to view the last ~100 log statements in-app
 */
class AppendFileTree : Timber.Tree() {

    private val path = Paths.get(calarm.filesDir.absolutePath, "calarm.log").apply {
        createFileIfNotExisting(this)
    }
    private val buffer = Files.newBufferedWriter(path, APPEND)

    init {
        flushOnStop()
    }

    private fun flushOnStop() {
        calarm.registerActivityLifecycleCallbacks(object : SimpleActivityLifecycleCallbacks() {
            override fun onActivityStopped(p0: Activity) {
                buffer.flush()
            }
        })
    }

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        if (tag?.endsWith(TAG_SUFFIX) == true) {
            val dateTime = logDateFormat.format(Calendar.getInstance().time)
            val line = "$dateTime~ $message"
            buffer.appendLine(line)
        }
    }

    private fun createFileIfNotExisting(path: Path) {
        if (Files.notExists(path)) {
            Files.createFile(path);
        }
    }

    companion object {
        const val TAG_SUFFIX = "appendToFile"
        val logDateFormat = SimpleDateFormat("MM/dd HH:mm:ss:SSSS", Locale.US)
    }
}