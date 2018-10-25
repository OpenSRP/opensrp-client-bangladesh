package util;

import android.util.Log;

/**
 * Created by raihan on 6/27/18.
 */
public class Logger {

    public static void largeLog(String tag, String content) {

        if (content.length() > 4000) {
            Log.d(tag, content.substring(0, 4000));
            largeLog(tag, content.substring(4000));
        } else {
            Log.d(tag, content);
        }
    }

    public static void largeErrorLog(String tag, String content) {

        if (content.length() > 4000) {
            Log.e(tag, content.substring(0, 4000));
            largeErrorLog(tag, content.substring(4000));
        } else {
            Log.e(tag, content);
        }
    }

}