package com.horizon.cantor.util;

import java.text.SimpleDateFormat;

/**
 * @author : David.Song/Java Engineer
 * @date : 2016/1/27 15:40
 * @see
 * @since : 1.0.0
 */
public class DateUtil {

    private static SimpleDateFormat dateFormat =
            new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");

    public static String format(long time){
        return dateFormat.format(time);
    }
}
