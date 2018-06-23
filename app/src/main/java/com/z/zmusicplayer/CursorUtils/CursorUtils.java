package com.z.zmusicplayer.CursorUtils;

import android.database.Cursor;
import android.util.Log;

/**
 * Created by z on 2014/6/25.
 */

public class CursorUtils {
    public static  void printCusor(Cursor cursor){
        if(cursor==null){
            return;
        }
        //游标移动到第0个之前，从索引为0开始查找
        cursor.moveToPosition(-1);
        //cursor.moveToNext()  有下一个元素，返回值就是true
        while (cursor.moveToNext()){
            //getColumnCount 获取当前行有多少列
            for (int i = 0; i < cursor.getColumnCount(); i++) {
                //sqlite数据库 存的都是字符串, getString都没问题，不管存的是int，还是boolean系统会自动转黄成string
                Log.d("zhu","列名:"+cursor.getColumnName(i)+"第n列 对应的名:"+cursor.getString(i));
            }
        }
    }
}
