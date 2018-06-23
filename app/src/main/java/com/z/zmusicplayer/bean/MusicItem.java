package com.z.zmusicplayer.bean;

import android.database.Cursor;
import android.provider.MediaStore;

import java.io.Serializable;


public class MusicItem implements Serializable{
    public String title;
    public String artist;
    public long size;
    public String data;
    public int duration;
    public String displayName;

    public MusicItem(String title, String artist, long size, String data, int duration,String displayName) {
        this.title = title;
        this.artist = artist;
        this.size = size;
        this.data = data;
        this.duration = duration;
        this.displayName = displayName;
    }

    public static MusicItem getMusicFromCursor(Cursor cursor){
        if(cursor == null){
            return null;
        }
        String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
        String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
        String data = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
        long size = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE));
        int duration = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
        if(title.contains("-")){
            String[] temp = title.split("-");
            artist = temp[0].trim();
            title = temp[1].trim();
        }
        String displayName = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
        return new MusicItem(title,artist,size,data,duration,displayName);
    }
}
