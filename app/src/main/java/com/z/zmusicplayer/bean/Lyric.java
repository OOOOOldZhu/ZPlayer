package com.z.zmusicplayer.bean;

/**
 *
 */

public class Lyric implements Comparable<Lyric>{
    //歌词开始演唱的时刻
    public int time;
    //歌词的文本内容
    public String text;

    public Lyric(int time, String text) {
        this.time = time;
        this.text = text;
    }

    @Override
    public int compareTo(Lyric o) {
        return this.time-o.time;
    }
}
