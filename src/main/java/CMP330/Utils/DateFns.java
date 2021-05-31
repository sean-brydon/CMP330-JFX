package CMP330.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFns {
    public enum DateFormatOptions {
        Default,
        WithTime
    }

    public static String customDateFormat(DateFormatOptions options) {
        SimpleDateFormat formatter = null;

        formatter = new SimpleDateFormat("dd-MM-YYYY");

        Date date = new Date(System.currentTimeMillis());

        return formatter.format(date);
    }
}
