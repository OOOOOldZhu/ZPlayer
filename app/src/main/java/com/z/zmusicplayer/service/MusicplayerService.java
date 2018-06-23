package com.z.zmusicplayer.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.z.zmusicplayer.R;
import com.z.zmusicplayer.activity.MusicPlayerActivity;
import com.z.zmusicplayer.bean.Constant;
import com.z.zmusicplayer.bean.MusicItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;


public class MusicplayerService extends Service {

    private static final int UPDATE_UI = 44;
    private static final int CANCEL_NOTIFICATION = 55;
    private static final int PLAY_PAUSE = 66;
    private ArrayList<MusicItem> musicItems;
    /**
     * 当前播放的音乐在列表中的索引
     */
    private int position = -1;
    private MediaPlayer player;
    public static final int PLAY_PRE = 202;
    public static final int PLAY_NEXT = 827;

    public static final int PLAY_MODE_REPEAT_LIST = 0;
    public static final int PLAY_MODE_REPEAT_SINGLE = 1;
    public static final int PLAY_MODE_SHUFFLE = 2;

    public static int CURRENT_PLAY_MODE = 0;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MusicController();
    }

    public class MusicController extends Binder {
        public boolean isPlaying(){
            return player.isPlaying();
        }
        /**
         * 根据当前播放状态切换播放暂停的状态
         */
        public void playPause(){
           MusicplayerService.this.playPause();
        }

        /**
         * 获取当前正在播放的音乐文件对象
         * @return
         */
        public MusicItem getCurrentMusic(){
            return musicItems.get(position);
        }

        /**
         * 获取当前的播放进度
         *
         */

        public int getCurrentPosition(){
            return player.getCurrentPosition();
        }

        public void seekTo(int position){
            player.seekTo(position);
        }

        /**
         * 上一首下一首切换的方法
         */
        public void preNext(int preNext){
            //调用外部类的同名方法
          MusicplayerService.this.preNext(preNext);
        }

        public int getDuration(){
            return player.getDuration();
        }
    }

    public void playPause(){
        if(player.isPlaying()){
            player.pause();
            sendBroadcast(new Intent("com.z.musicpause"));
        }else{
            player.start();
            sendBroadcast(new Intent("com.z.musicplay"));
        }
    }

    /**
     * 上一首下一首切换的方法
     */
    public void preNext(int preNext){
        //① 根据当前的播放模式修改 当前播放索引 就是position
        switch (CURRENT_PLAY_MODE){
            case PLAY_MODE_REPEAT_SINGLE:
                //单曲循环 不需要修改position
                break;
            case PLAY_MODE_SHUFFLE:
                //随机循环 生成随机数
                Random random = new Random();
                int temp =  random.nextInt(musicItems.size());
                //判断当前的索引 和生成的索引 不一样的时候 保存新的索引
                while(temp == position){
                    temp = random.nextInt(musicItems.size());
                }
                position = temp;
                break;
            case PLAY_MODE_REPEAT_LIST:
                if(preNext == PLAY_NEXT){
                    //播放下一首 把当前索引+1
                    position = (position+1)%musicItems.size();
                }else if(preNext == PLAY_PRE){
                    //播放上一首 把当前索引-1
                    position = position==0?musicItems.size()-1:(position-1);
                }
                break;
        }
        //②用当前播放的position 找到新的musicItem重置mediaplayer 播放
        startPlay();
    }

    @Override
    public void onCreate() {
        super.onCreate();
       CURRENT_PLAY_MODE = getSharedPreferences("player_config",MODE_PRIVATE).getInt("playMode",0);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //获取意图之后 判断究竟是从通知中来 还是从activiy中来
        if(intent.getBooleanExtra("fromNotifation",false)){
            int operation = intent.getIntExtra("operation", 22);
            switch (operation){
                case PLAY_NEXT:
                    preNext(PLAY_NEXT);
                    break;
                case PLAY_PRE:
                    preNext(PLAY_PRE);
                    break;
                case UPDATE_UI:
                    //通知activity更新界面
                    sendBroadcast(new Intent(Constant.ACTIOIN_START_PLAY));
                    break;
                case CANCEL_NOTIFICATION:
                    NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    manager.cancel(1);
                    break;
                case PLAY_PAUSE:
                    //控制音乐播放暂停
                    playPause();
                    //更新通知图标
                    sendCustomBigNotification();
                    break;
            }
        }else{
            //走到else里说明不是从通知中来的
            //通过intent获取音乐列表 以及当前点击的位置
            musicItems = (ArrayList<MusicItem>) intent.getSerializableExtra("musics");
            //获取到点击的位置 保存到一个临时变量
            int temp = intent.getIntExtra("position",0);
            if(temp == position){
                //如果temp和positiong(当前播放的位置)相当
                //说明点击的跟当前播放的是同一首歌曲 只需要更新界面
                sendBroadcast(new Intent(Constant.ACTIOIN_START_PLAY));
            }else{
                position = temp;
                startPlay();

            }
        }



        return super.onStartCommand(intent, flags, startId);
    }

    private void startPlay() {
        MusicItem musicItem = musicItems.get(position);
        if(player==null){
            player = new MediaPlayer();
            //给当前mediaplayer设置准备好的监听
            player.setOnPreparedListener(new MyOnPreparedListener());
            //给当前mediaplayer设置一首歌曲播放结束的监听
            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    //说明当前歌曲播放完了
                    preNext(PLAY_NEXT);
                }
            });
        }else{
            sendBroadcast(new Intent(Constant.ACTIOIN_MUSIC_PAUSE));
            player.reset();
        }
        try {
            //设置媒体资源的路径
            player.setDataSource(musicItem.data);
            //异步准备
            player.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class MyOnPreparedListener implements MediaPlayer.OnPreparedListener {
        @Override
        public void onPrepared(MediaPlayer mp) {
            //更新播放界面
            sendBroadcast(new Intent(Constant.ACTIOIN_START_PLAY));
            //异步准备完成之后 会走到这个方法中
            player.start();
            sendCustomBigNotification();
        }
    }

    /**
     * 发系统自带样式的通知
     */
    private void sendNotification() {
        Notification.Builder builer = new Notification.Builder(this);
        //5.0之后 状态栏的图标颜色 统一 不会显示复杂的颜色  tickerText 状态栏中显示的提示文字 也不会显示了
        builer.setSmallIcon(R.mipmap.music_default_bg);
//      builer.setTicker()
       // builer.setOngoing(true);//不能被删除掉 需要注意 设置为true之后 一定给用户删除的方法
        builer.setAutoCancel(true);//当用户点一下之后自动消失 资讯类的推送基本都是AutoCancel
        builer.setContentTitle(musicItems.get(position).title);
        builer.setContentText(musicItems.get(position).artist);
        builer.setContentInfo(musicItems.get(position).duration+"");

        Notification notification = builer.build();
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.notify(1,notification);
        //通过NotificationManager 传入id可以把通知取消掉
       // manager.cancel(1);
    }

    private void sendCustomNotification(){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.mipmap.music_default_bg);
        builder.setContent(getRemoteViews());
        Notification notification = builder.build();
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.notify(1,notification);
    }

    private void sendCustomBigNotification(){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.mipmap.music_default_bg);
        builder.setContent(getRemoteViews());
        if(Build.VERSION.SDK_INT>=16){
            builder.setCustomBigContentView(getBigRemoteViews());
        }
        Notification notification = builder.build();
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.notify(1,notification);
    }

    private RemoteViews getBigRemoteViews() {
        RemoteViews remoteViews = new RemoteViews(getPackageName(),R.layout.notification_big);
        remoteViews.setTextViewText(R.id.tv_artist,musicItems.get(position).artist);
        remoteViews.setTextViewText(R.id.tv_title,musicItems.get(position).title);
        if(player.isPlaying()){
            remoteViews.setImageViewResource(R.id.notification_playpause,R.drawable.selector_play);
        }else{
            remoteViews.setImageViewResource(R.id.notification_playpause,R.drawable.selector_pause);
        }

        remoteViews.setOnClickPendingIntent(R.id.notification_play_next,getNextPendingIntent());
        remoteViews.setOnClickPendingIntent(R.id.notification_play_pre,getPrePendingIntent());
        remoteViews.setOnClickPendingIntent(R.id.rl_notification,getActivityPendingIntent());
        remoteViews.setOnClickPendingIntent(R.id.iv_cancel,getCancelPendingIntent());
        remoteViews.setOnClickPendingIntent(R.id.notification_playpause,getPlayPausePendingIntent());
        return remoteViews;
    }

    private PendingIntent getPlayPausePendingIntent() {
        Intent intent= new Intent(getApplicationContext(),MusicplayerService.class);
        //携带参数 表示这个意图是从通知中来
        intent.putExtra("fromNotifation",true);
        intent.putExtra("operation",PLAY_PAUSE);
        PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(),4,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        return pendingIntent;
    }

    private PendingIntent getCancelPendingIntent() {
        Intent intent= new Intent(getApplicationContext(),MusicplayerService.class);
        //携带参数 表示这个意图是从通知中来
        intent.putExtra("fromNotifation",true);
        intent.putExtra("operation",CANCEL_NOTIFICATION);
        PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(),5,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        return pendingIntent;
    }

    private RemoteViews getRemoteViews() {
        RemoteViews remoteViews = new RemoteViews(getPackageName(),R.layout.notification_normal);
        remoteViews.setTextViewText(R.id.tv_artist,musicItems.get(position).artist);
        remoteViews.setTextViewText(R.id.tv_title,musicItems.get(position).title);

        remoteViews.setOnClickPendingIntent(R.id.notification_play_next,getNextPendingIntent());
        remoteViews.setOnClickPendingIntent(R.id.notification_play_pre,getPrePendingIntent());
        remoteViews.setOnClickPendingIntent(R.id.rl_notification,getActivityPendingIntent());

        return remoteViews;
    }

    private PendingIntent getActivityPendingIntent() {
        Intent intent= new Intent(getApplicationContext(),MusicPlayerActivity.class);
        //携带参数 表示这个意图是从通知中来
        intent.putExtra("fromNotifation",true);
        intent.putExtra("operation",UPDATE_UI);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),3,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        return pendingIntent;
    }

    /**
     * 播放上一首的延迟意图
     * @return
     */
    private PendingIntent getPrePendingIntent() {
        Intent intent= new Intent(getApplicationContext(),MusicplayerService.class);
        //携带参数 表示这个意图是从通知中来
        intent.putExtra("fromNotifation",true);
        intent.putExtra("operation",PLAY_PRE);
        PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(),1,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        return pendingIntent;
    }

    /**
     * 播放下一首的延迟意图
     * @return
     */
    private PendingIntent getNextPendingIntent() {
        Intent intent= new Intent(getApplicationContext(),MusicplayerService.class);
        //携带参数 表示这个意图是从通知中来
        intent.putExtra("fromNotifation",true);
        intent.putExtra("operation",PLAY_NEXT);
        PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(),2,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        return pendingIntent;
    }
}
