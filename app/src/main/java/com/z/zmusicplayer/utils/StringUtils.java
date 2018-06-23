package com.z.zmusicplayer.utils;


public class StringUtils {

    /**
     * 格式化字符串 传入毫秒值 返回 mm:ss这样的格式
     * @param time
     * @return
     */
    public static String formatPlayTime(int time){
//        1:22:33   3:44
        int hour = 60*60*1000;
        int minute = 60*1000;
        int second = 1000;

        int h = time/hour;
        int min = time%hour/minute;
        int sec = time%minute/second;

        if(h>0){
           //时间超出1小时了
            return String.format("%02d:%02d:%02d",h,min,sec);
        }else{
            return String.format("%02d:%02d",min,sec);
        }


    }
}
