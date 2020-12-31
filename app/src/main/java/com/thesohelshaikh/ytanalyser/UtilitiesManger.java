package com.thesohelshaikh.ytanalyser;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UtilitiesManger {
    private static final Pattern PATTERN =
            Pattern.compile("([-+]?)P(?:([-+]?[0-9]+)D)?" +
                            "(T(?:([-+]?[0-9]+)H)?(?:([-+]?[0-9]+)M)?(?:([-+]?[0-9]+)(?:[.,]" +
                            "([0-9]{0,9}))?S)?)?",
                    Pattern.CASE_INSENSITIVE);
    private static final int SECONDS_PER_MINUTE = 60;
    private static final int SECONDS_PER_HOUR = SECONDS_PER_MINUTE * 60;
    private static final int SECONDS_PER_DAY = SECONDS_PER_HOUR * 24;

    /**
     * Youtube date is on ISO 8061 format. Could have used Duration but did manually since there
     * is API restriction.
     *
     * @param text duration string from youtube API
     * @return Date object from the parse time
     * @see java.time.Duration
     */
    public static HashMap<String, Long> parseTime(String text) {
        Matcher matcher = PATTERN.matcher(text);
        if (matcher.matches()) {
            // check for letter T but no time sections
            if (!"T".equals(matcher.group(3))) {
                boolean negate = "-".equals(matcher.group(1));
                String dayMatch = matcher.group(2);
                String hourMatch = matcher.group(4);
                String minuteMatch = matcher.group(5);
                String secondMatch = matcher.group(6);
                String fractionMatch = matcher.group(7);
                if (dayMatch != null || hourMatch != null || minuteMatch != null || secondMatch != null) {
                    long daysAsSecs = parseNumber(dayMatch, SECONDS_PER_DAY);
                    long hoursAsSecs = parseNumber(hourMatch, SECONDS_PER_HOUR);
                    long minsAsSecs = parseNumber(minuteMatch, SECONDS_PER_MINUTE);
                    long seconds = parseNumber(secondMatch, 1);
                    long nanos = parseNumber(fractionMatch, seconds < 0 ? -1 : 1);

                    long totalSeconds = daysAsSecs + hoursAsSecs + minsAsSecs + seconds + nanos;
                    Date d = new Date(totalSeconds);
                    return calculateAlternateDurations(d);
//                    String dateString = getDateString(d);
//                    return d;
                }
            }
        }
        return null;
    }

    private static long parseNumber(String parsed, int multiplier) {
        // regex limits to [-+]?[0-9]+
        if (parsed == null) {
            return 0;
        }
        return Long.parseLong(parsed) * multiplier;
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

    public static HashMap<String, Long> calculateAlternateDurations(Date actualDuration) {
        // 1.25, 1.5, 1.75, 2
        // TODO : sort list
        long time = actualDuration.getTime();
        HashMap<String, Long> durations = new HashMap<>();
        durations.put("1", time);
        durations.put("1.25", (long) (time / 1.25));
        durations.put("1.5", (long) (time / 1.75));
        durations.put("1.75", (long) (time / 1.5));
        durations.put("2", (long) (time / 2));
        return durations;
    }

    public static String getDateString(Date date) {
        // TODO
        Calendar referenceCalendar = Calendar.getInstance();
        Calendar durationCalendar = Calendar.getInstance();
        referenceCalendar.setTime(new Date(0));
        durationCalendar.setTime(new Date(0));
        long totalSeconds = date.getTime();
        durationCalendar.set(Calendar.SECOND, (int) totalSeconds);
        referenceCalendar.get(Calendar.SECOND);
        return null;
    }
}
