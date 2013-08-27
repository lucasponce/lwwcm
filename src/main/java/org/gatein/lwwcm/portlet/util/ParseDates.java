package org.gatein.lwwcm.portlet.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Logger;

public class ParseDates {

    private static final Logger log = Logger.getLogger(ParseDates.class.getName());

    static SimpleDateFormat today = new SimpleDateFormat("HH:mm:ss z");
    static SimpleDateFormat other = new SimpleDateFormat("d MMM yyyy");

    public static String parse(Calendar cal) {
        if (cal == null) return "";
        try {
            Calendar now = Calendar.getInstance();
            boolean sameDay = now.get(Calendar.YEAR) == cal.get(Calendar.YEAR) &&
                    now.get(Calendar.DAY_OF_YEAR) == cal.get(Calendar.DAY_OF_YEAR);
            String output = null;
            if (sameDay) {
                output = today.format(cal.getTime());
            } else {
                output = other.format(cal.getTime());
            }
            return output;
        } catch (Exception e) {
            log.warning("Error parsing cal: " + cal + ". Error: " + e.toString());
            e.printStackTrace();
        }
        return "";
    }
}
