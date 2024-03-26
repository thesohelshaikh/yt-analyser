package com.thesohelshaikh.ytanalyser.ui.details

import android.annotation.SuppressLint
import java.net.MalformedURLException
import java.net.URL
import java.text.SimpleDateFormat
import java.time.Duration
import java.util.Calendar
import java.util.Date

object DurationsManger {
    /**
     * Youtube date is on ISO 8061 format.
     *
     * @param text duration string from youtube API
     * @return Date object from the parse time
     * @see java.time.Duration
     */
    fun parseTime(text: String?): ArrayList<Long> {
        val currentDuration = Duration.parse(text)
        val millis = currentDuration.toMillis()
        currentDuration.plus(currentDuration)
        val afterCal = Calendar.getInstance()
        afterCal.add(Calendar.MILLISECOND, Math.toIntExact(millis))
        return calculateAlternateDurations(Date(millis))
    }

    @SuppressLint("SimpleDateFormat")
    fun getDateAfter(millis: Long): String {
        val cal = Calendar.getInstance()
        val todayDate = cal[Calendar.DATE]
        cal.add(Calendar.MILLISECOND, millis.toInt())
        val sdf = if (todayDate == cal[Calendar.DATE]) {
            SimpleDateFormat("HH:mm:ss a")
        } else {
            SimpleDateFormat("HH:mm:ss a, dd-MM-yy")
        }
        cal[Calendar.DATE]
        val time = cal.time
        return sdf.format(time)
    }

    fun getPrettyDuration(millis: Long): String {
        var timeLeft = Duration.ofMillis(millis)
        val hours = timeLeft.toHours()
        timeLeft = timeLeft.minusHours(hours)
        val minutes = timeLeft.toMinutes()
        timeLeft = timeLeft.minusMinutes(minutes)
        val seconds = timeLeft.seconds
        val sb = StringBuilder()
        if (hours > 0) {
            sb.append(hours).append("h ")
        }
        if (minutes > 0) {
            sb.append(minutes).append("m ")
        }
        if (seconds > 0) {
            sb.append(seconds).append("s")
        }
        return sb.toString()
    }

    fun getIDfromURL(url: String): String? {
        if (!url.startsWith("http")) return null
        return try {
            val originalURL = URL(url)
            var id: String?
            if (url.startsWith("https://youtu.be/")) {
                id = originalURL.path
            } else {
                id = originalURL.query
                if (id == null) {
                    id = originalURL.path
                }
            }
            cleanID(id)
        } catch (e: MalformedURLException) {
            e.printStackTrace()
            null
        }
    }

    private fun cleanID(idToClean: String?): String {
        if (idToClean == null) return ""
        // desktop url
        var id = idToClean.substringBefore('&')
        id = if (id.startsWith("v")) {
            id.substring(2)
        } else {
            // mobile url, remove extra forward slash
            id.replace("\\/".toRegex(), "")
        }
        if (id.startsWith("list")) {
            id = id.substring(5)
                .substringBefore('&')
        }
        return id
    }

    @JvmStatic
    fun calculateAlternateDurations(actualDuration: Date): ArrayList<Long> {
        val time = actualDuration.time
        val durations = ArrayList<Long>()
        durations.add(time)
        durations.add((time / 1.25).toLong())
        durations.add((time / 1.75).toLong())
        durations.add((time / 1.5).toLong())
        durations.add(time / 2)
        durations.sortWith { o1, o2 -> o2.compareTo(o1) }
        return durations
    }

    fun parsePlaylistDurations(durations: ArrayList<String>): Long {
        var totalMillis: Long = 0
        for (duration in durations) {
            val millis = Duration.parse(duration).seconds * 1000
            totalMillis += millis
        }
        return totalMillis
    }

    fun getUrlFromId(id: String): String {
        return if (id.startsWith("PL")) {
            "https://www.youtube.com/playlist?list=$id"
        } else {
            "http://www.youtube.com/watch?v=$id"
        }
    }
}