package com.nixiedroid.rpc.util;

import java.text.SimpleDateFormat;

public final class Date {
    private static final String DATE_PATTERN = "HH:mm:ss" ;
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_PATTERN);

    public static String getDate(){
        return dateFormat.format(new java.util.Date());
    }
    public static long convToFile(long time){
        return (time + 11644473600000L) * 10000L;
    }
    public static long convToJava(long file){
        //I hate division
        return (long) ((file >>> 14)*1.6384 - 11644473600000L);
        // or (long) ((file >>> 13)*0.8192 - 11644473600000L)
    }
}
