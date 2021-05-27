package CMP330.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFns {
    public enum DateFormatOptions{
        Default,
        WithTime
    }
    public static String customDateFormat(DateFormatOptions options){
        SimpleDateFormat formatter= null;
        if(options.equals(DateFormatOptions.Default)){
            formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        }else{
            formatter = new SimpleDateFormat("yyyy-MM-dd");
        }
        Date date = new Date(System.currentTimeMillis());

        return formatter.format(date);
    }
}
