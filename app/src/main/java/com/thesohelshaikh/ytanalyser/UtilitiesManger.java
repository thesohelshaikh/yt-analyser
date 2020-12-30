package com.thesohelshaikh.ytanalyser;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UtilitiesManger {
    private static final Pattern PATTERN =
            Pattern.compile("([-+]?)P(?:([-+]?[0-9]+)D)?" +
                            "(T(?:([-+]?[0-9]+)H)?(?:([-+]?[0-9]+)M)?(?:([-+]?[0-9]+)(?:[.,]([0-9]{0,9}))?S)?)?",
                    Pattern.CASE_INSENSITIVE);

    public static Date parseTime(String text) {
        Matcher matcher = PATTERN.matcher(text);
        if (matcher.matches()) {
            // check for letter T but no time sections
            if (!"T".equals(matcher.group(3))) {
                boolean negate = "-".equals(matcher.group(1));
                Long dayMatch = parseNumber(matcher.group(2));
                Long hourMatch = parseNumber(matcher.group(4));
                Long minuteMatch = parseNumber(matcher.group(5));
                Long secondMatch = parseNumber(matcher.group(6));
                Long fractionMatch = parseNumber(matcher.group(7));
                if (dayMatch != null || hourMatch != null || minuteMatch != null || secondMatch != null) {
                    long daysAsSecs = dayMatch;
                    long hoursAsSecs = hourMatch;
                    long minsAsSecs = minuteMatch;
                    long seconds = secondMatch;
                    long nanos = fractionMatch;

                    long totalSeconds = daysAsSecs + hoursAsSecs + minsAsSecs + seconds + nanos;
                    return new Date(totalSeconds);
                }
            }
        }
        return null;
    }

    private static long parseNumber(String parsed) {
        // regex limits to [-+]?[0-9]+
        if (parsed == null) {
            return 0;
        }
        return Long.parseLong(parsed);
    }
}
