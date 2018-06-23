package com.z.zmusicplayer.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.TextView;


import com.z.zmusicplayer.R;
import com.z.zmusicplayer.bean.Lyric;
import com.z.zmusicplayer.utils.LyricsParser;

import java.io.File;
import java.util.ArrayList;


public class LyricView extends TextView {

    private int viewHeight;
    private int viewWidth;
    private float bigTextSize;
    private float nomalTextSize;
    private float lineHeight;
    private int heightLightColor;
    private Rect bounds;
    private Paint paint;
    private ArrayList<Lyric> lyrics;
    /**
     * 记录当前正在演唱的歌词索引
     */
    private int currentIndex=0;
    private float centerY;
    private int currentTime;
    private int duration;
    private float passedPercent;

    public LyricView(Context context) {
        super(context);
    }

    public LyricView(Context context, AttributeSet attrs) {
      //  super(context, attrs);
        this(context, attrs,0);
    }

    public LyricView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initParam();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public LyricView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //当布局的时候 View的大小发生改变会走这个方法
        //获取View的宽度和高度
        viewHeight = h;
        viewWidth = w;

        String text = "测量歌词";
        paint.setTextSize(bigTextSize);
        paint.getTextBounds(text,0,text.length(),bounds);
        //获取文字高度
        int textHeight =bounds.height();
        //测量中间点的高度
        centerY = viewHeight/2+textHeight/2;
    }

    private void initParam() {
        //加载用到的文字大小
        bigTextSize = getResources().getDimension(R.dimen.big_Text_Size);
        nomalTextSize = getResources().getDimension(R.dimen.normal_Text_Size);
        lineHeight = getResources().getDimension(R.dimen.lineHeight);

        heightLightColor = getResources().getColor(R.color.colorMusicProgress);
        bounds = new Rect();
        paint = new Paint();
        paint.setAntiAlias(true);

//        lyrics = new ArrayList<>();
//        for(int i = 0;i<40;i++){
//            lyrics.add(new Lyric(i*2000,"我是歌词的第"+i+"行"));
//        }
    }
    @Override
    protected void onDraw(Canvas canvas) {
//        String text = "我是一行歌词歌词";
//
//        paint.setTextSize(bigTextSize);
//        paint.setColor(heightLightColor);
//        //x,y 把文字的左下角绘制到view的x,y这个坐标上
//        //创建一个矩形对象 用来保存文字的宽高
//         Rect bounds = new Rect()
//        paint.getTextBounds(text,0,text.length(),bounds);
//        //获取文字高度
//        int textHeight =bounds.height();
//        //获取文字的宽度
//        int textWidth = bounds.width();
//        float x = viewWidth/2-textWidth/2;
//        float centerY = viewHeight/2+textHeight/2;
//       canvas.drawText(text,x,centerY,paint);
//        text = "我是上一行歌词";
//        //修改为小字体
//        paint.setTextSize(nomalTextSize);
//        //修改颜色为白色
//        paint.setColor(Color.WHITE);
//        paint.getTextBounds(text,0,text.length(),bounds);
//        textWidth = bounds.width();
//        x =viewWidth/2-textWidth/2;
//        //y坐标是中间行的位置-行高
//        float y = centerY-lineHeight;
//        canvas.drawText(text,x,y,paint);
//        text = "我是下一行歌词";
//        paint.getTextBounds(text,0,text.length(),bounds);
//        textWidth = bounds.width();
//        x =viewWidth/2-textWidth/2;
//        //y坐标是中间行的位置+行高
//         y = centerY+lineHeight;
//        canvas.drawText(text,x,y,paint);
        if(lyrics!=null && lyrics.size()>0){
        canvas.translate(0,-getTransY());
        drawAllLyrics(canvas);
        }

    }

    /**
     * 计算画布移动的距离实现平滑滚动
     * @return
     */
    private float getTransY() {
        Lyric lyric = lyrics.get(currentIndex);
        int totalTime;
        if(currentIndex == lyrics.size()-1){
            //如果已经是最后一句了
            totalTime = duration-lyric.time;
        }else{
            //计算当前的歌词一共要唱多久
            totalTime = lyrics.get(currentIndex+1).time-lyric.time;
        }
        //当前歌词已经唱了多久
        int passedTime = currentTime-lyric.time;
        passedPercent = passedTime/(float)totalTime;
        float dy = passedTime * lineHeight/totalTime;
        return dy;
    }

    private void drawAllLyrics(Canvas canvas){
        for(int i = 0;i<lyrics.size();i++){
            drawSingleLineLyric(canvas,i);
        }
    }

    /**
     * 根据当前歌词在集合中的索引 以及 正在演唱的歌词索引 确定歌词应该绘制的位置 绘制一行歌词
     * @param canvas
     * @param index
     */

    private void drawSingleLineLyric(Canvas canvas, int index){
        Lyric lyric = lyrics.get(index);
        if(index == currentIndex){
            //说明正在演唱的就是这一行歌词
            //绘制到屏幕中心 用高亮的颜色 使用大的字体
            paint.setColor(heightLightColor);
            paint.setTextSize(bigTextSize);
            paint.getTextBounds(lyric.text,0,lyric.text.length(),bounds);
            float x = viewWidth/2-bounds.width()/2;
            //shader涂色 上颜色
            paint.setShader(new LinearGradient(x,centerY,x+bounds.width(),centerY,
                    new int[]{heightLightColor,Color.WHITE},new float[]{passedPercent,passedPercent+0.01f}, Shader.TileMode.CLAMP));
        }else{
            //说明是普通的歌词 用白色 小字体
            paint.setColor(Color.WHITE);
            paint.setTextSize(nomalTextSize);
            //普通歌词没有着色效果
            paint.setShader(null);
        }
        paint.getTextBounds(lyric.text,0,lyric.text.length(),bounds);
        float x = viewWidth/2-bounds.width()/2;
        //用当前歌词的索引-正在唱的索引 可以确定当前索引和正在唱的索引的差*行高 可以确定歌词改绘制到Y方向的坐标
        float y = centerY+(index-currentIndex)*lineHeight;

        canvas.drawText(lyric.text,x,y,paint);
    }

    /**
     * 根据当前正在演唱的时刻 确定正在演唱的歌词索引
     * @param currentTime
     */
    private void updateCurrentIndex(int currentTime){
        for(int i=0;i<lyrics.size();i++){
            if(i==lyrics.size()-1){
                currentIndex=i;
                break;
            }

            if(currentTime>lyrics.get(i).time&& currentTime<lyrics.get(i+1).time){
                currentIndex = i;
                break;
            }
        }
    }

    /**
     * 更新歌词View
     * @param currentTime 当前播放时刻
     * @param duration    歌曲的总时长
     */
    public void updateLyricView(int currentTime,int duration){
        this.duration = duration;
        this.currentTime = currentTime;
        //根据当前演唱的时刻更新 正在唱的歌词索引
        updateCurrentIndex(currentTime);
        //正在唱的歌词索引更新了之后 重绘界面
        invalidate();
    }

    public void loadLyric(File lyricFile){
        lyrics = LyricsParser.parserFromFile(lyricFile);
    }
}
