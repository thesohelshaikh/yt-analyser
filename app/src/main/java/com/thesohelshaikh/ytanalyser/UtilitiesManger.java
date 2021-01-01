package com.thesohelshaikh.ytanalyser;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class UtilitiesManger {

    /**
     * Youtube date is on ISO 8061 format. Could have used Duration but did manually since there
     * is API restriction.
     *
     * @param text duration string from youtube API
     * @return Date object from the parse time
     * @see java.time.Duration
     */
    public static ArrayList<Long> parseTime(String text) {
        Duration currentDuration = Duration.parse(text);
        long millis = currentDuration.toMillis();
        currentDuration.plus(currentDuration);
        Calendar afterCal = Calendar.getInstance();
        afterCal.add(Calendar.MILLISECOND, Math.toIntExact(millis));
        return calculateAlternateDurations(new Date(millis));
    }

    public static String getDateAfter(long millis) {
        Calendar cal = Calendar.getInstance();
        int todayDate = cal.get(Calendar.DATE);
        cal.add(Calendar.MILLISECOND, (int) millis);
        SimpleDateFormat sdf;
        if (todayDate == cal.get(Calendar.DATE)) {
            sdf = new SimpleDateFormat("HH:mm:ss");
        } else {
            sdf = new SimpleDateFormat("HH:mm:ss, dd-MM-yy");
        }
        cal.get(Calendar.DATE);
        Date time = cal.getTime();
        return sdf.format(time);
    }

    public static String getPrettyDuration(long millis) {
        Duration timeLeft = Duration.ofMillis(millis);
        long hours = timeLeft.toHours();
        timeLeft = timeLeft.minusHours(hours);
        long minutes = timeLeft.toMinutes();
        timeLeft = timeLeft.minusMinutes(minutes);
        long seconds = timeLeft.getSeconds();
        StringBuilder sb = new StringBuilder();
        if (hours > 0) {
            sb.append(hours).append("h ");
        }
        if (minutes > 0) {
            sb.append(minutes).append("m ");
        }
        if (seconds > 0) {
            sb.append(seconds).append("s");
        }
        return sb.toString();
    }

    public static String getIDfromURL(String url) {
        if (url.startsWith("http")) {
            try {
                URL originalURL = new URL(url);
                String id = originalURL.getQuery();
                if (id == null) {
                    id = originalURL.getPath();
                }
                return cleanID(id);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        return url;
    }

    public static String cleanID(String id) {
        // desktop url
        if (id.startsWith("v")) {
            id = id.substring(2);
        } else {
            // mobile url, remove extra forward slash
            id = id.replaceAll("\\/", "");
        }
        return id;
    }

    public static ArrayList<Long> calculateAlternateDurations(Date actualDuration) {
        long time = actualDuration.getTime();
        ArrayList<Long> durations = new ArrayList<>();
        durations.add(time);
        durations.add((long) (time / 1.25));
        durations.add((long) (time / 1.75));
        durations.add((long) (time / 1.5));
        durations.add((time / 2));
        return durations;
    }

}
