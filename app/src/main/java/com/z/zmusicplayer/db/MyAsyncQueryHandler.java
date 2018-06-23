package com.z.zmusicplayer.db;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

import com.z.zmusicplayer.adapter.MusicAdapter;


public class MyAsyncQueryHandler extends AsyncQueryHandler{
    public MyAsyncQueryHandler(ContentResolver cr) {
        super(cr);



    }

    @Override
    public void startQuery(int token, Object cookie, Uri uri, String[] projection, String selection, String[] selectionArgs, String orderBy) {
        super.startQuery(token, cookie, uri, projection, selection, selectionArgs, orderBy);
    }
    //token用来区分不同的查询， onQueryCompete可以处理多个查询，cookie  startQury方法传过来的一个adapter 通过adapter刷新界面
    // 第三个参数 异步查询之后返回的结果
    @Override
    protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
        super.onQueryComplete(token, cookie, cursor);
        //cookie
        //走到这个方法 ，异步查询结束
        MusicAdapter adapter= (MusicAdapter) cookie;
        adapter.changeCursor(cursor);
    }
}
