package no.ssb.timeuse.surveyservice.communicationlog;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MessageBuilderPattern {
    private static final Pattern pattern = Pattern.compile("(\\{\\w*})");


    public static Matcher getMatcher(String message) {
        return pattern.matcher(message);
    }
}
