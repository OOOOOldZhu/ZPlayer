package com.z.zmusicplayer.activity;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.z.zmusicplayer.service.MusicplayerService;
import com.z.zmusicplayer.R;
import com.z.zmusicplayer.bean.Constant;
import com.z.zmusicplayer.bean.MusicItem;
import com.z.zmusicplayer.utils.StringUtils;
import com.z.zmusicplayer.view.LyricView;

import java.io.File;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class MusicPlayerActivity extends AppCompatActivity {

    @InjectView(R.id.iv_back)
    ImageView ivBack;

    TextView tvTitle;
    @InjectView(R.id.tv_artist)
    TextView tvArtist;
    @InjectView(R.id.iv_wave)
    ImageView ivWave;
    @InjectView(R.id.tv_playedTime)
    TextView tvPlayedTime;
    @InjectView(R.id.sb_progress)
    SeekBar sbProgress;
    @InjectView(R.id.iv_playmode)
    ImageView ivPlaymode;
    @InjectView(R.id.iv_pre)
    ImageView ivPre;
    @InjectView(R.id.iv_playpause)
    ImageView ivPlaypause;
    @InjectView(R.id.iv_next)
    ImageView ivNext;
    @InjectView(R.id.iv_playList)
    ImageView ivPlayList;
    @InjectView(R.id.lyric)
    LyricView lyricView;
    @InjectView(R.id.rl_top)
    RelativeLayout rl_top;
    private static final int UPDATE_PLAY_TIME = 676;
    private static final int UPDATE_LYRIC = 459;
    private MusicplayerService.MusicController music;
    private String duration;
    private MyReceiver receiver;
    private MyConn conn;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case UPDATE_PLAY_TIME:
                    updatePlayTime();
                    break;
                case UPDATE_LYRIC:
                    updateLyric();
                    break;
            }
        }
    };

    private void updateLyric() {
        lyricView.updateLyricView(music.getCurrentPosition(),music.getDuration());
        handler.sendEmptyMessageDelayed(UPDATE_LYRIC,100);
    }

    private void updatePlayTime() {
        String current = StringUtils.formatPlayTime(music.getCurrentPosition());
        tvPlayedTime.setText(current+"/"+duration);
        sbProgress.setProgress(music.getCurrentPosition());
        handler.sendEmptyMessageDelayed(UPDATE_PLAY_TIME,500);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            //window.setNavigationBarColor(Color.TRANSPARENT);
        }
        ButterKnife.inject(this);
        initService();
        initReceiver();

        sbProgress.setOnSeekBarChangeListener(new MyOnSeekBarChangeListener());

        //获取状态栏高
        int statusBarHeight1 = -1;
        //获取status_bar_height资源的ID
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            statusBarHeight1 = getResources().getDimensionPixelSize(resourceId);
        }
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) rl_top.getLayoutParams();
        lp.topMargin = statusBarHeight1;
        rl_top.setLayoutParams(lp);

    }

    private void initReceiver() {
        receiver = new MyReceiver();
        IntentFilter filter = new IntentFilter(Constant.ACTIOIN_START_PLAY);
        filter.addAction(Constant.ACTIOIN_MUSIC_PLAY);
        filter.addAction(Constant.ACTIOIN_MUSIC_PAUSE);
        registerReceiver(receiver,filter);
    }

    private void initService() {
        //用混合的方式开启服务
        //Intent intent = new Intent(getApplicationContext(), MusicplayerService.class);
        //用Fragment开启当前activity的意图 开启服务 只需要修改intent的class setClass方法
        //里面的数据原封不动传给service
        Intent intent = getIntent();
        intent.setClass(getApplicationContext(), MusicplayerService.class);
        startService(intent);
        conn = new MyConn();
        bindService(intent, conn, BIND_AUTO_CREATE);

    }

    @OnClick({R.id.iv_back, R.id.iv_playmode, R.id.iv_pre, R.id.iv_playpause, R.id.iv_next, R.id.iv_playList})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                break;
            case R.id.iv_playmode:
                //①修改currentplaymode
                MusicplayerService.CURRENT_PLAY_MODE = (MusicplayerService.CURRENT_PLAY_MODE+1)%3;
                //②根据当前播放模式修改界面图标
                updatePlayModeIcon();
                //③用sp保存当前的播放模式
                getSharedPreferences("player_config",MODE_PRIVATE).edit().
                        putInt("playMode",MusicplayerService.CURRENT_PLAY_MODE).commit();
                break;
            case R.id.iv_pre:
                music.preNext(MusicplayerService.PLAY_PRE);
                break;
            case R.id.iv_playpause:
                music.playPause();
                break;
            case R.id.iv_next:
                music.preNext(MusicplayerService.PLAY_NEXT);
                break;
            case R.id.iv_playList:
                break;
        }
    }

    /**
     * 根据当前的播放模式 修改播放模式的图标
     */
    private void updatePlayModeIcon() {
        switch (MusicplayerService.CURRENT_PLAY_MODE){
            case MusicplayerService.PLAY_MODE_REPEAT_LIST:
                ivPlaymode.setImageResource(R.drawable.selector_playmode_listrepeat);
                break;
            case MusicplayerService.PLAY_MODE_REPEAT_SINGLE:
                ivPlaymode.setImageResource(R.drawable.selector_playmode_single);
                break;
            case MusicplayerService.PLAY_MODE_SHUFFLE:
                ivPlaymode.setImageResource(R.drawable.selector_playmode_shuffle);
                break;
        }
    }


    private class MyConn implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //当调用bindservice开启服务之后 onBind有返回的时候会执行这个方法
            //第二个参数IBinder servic 就是onBind返回值 通过这个IBinder可以调用service方法
            music = (MusicplayerService.MusicController) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            //只有当serivce所在进程 异常退出的时候才会走onServiceDisconnected
        }
    }

    private class MyReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(Constant.ACTIOIN_START_PLAY.equals(action)){
//                更新界面
                updateUI();
                //更新播放时间的显示
                updatePlayTime();
                updateLyric();
            }else if(Constant.ACTIOIN_MUSIC_PAUSE.equals(action)){
                //说明音乐暂停
                ivPlaypause.setImageResource(R.drawable.selector_pause);
                //停止更新时间
                handler.removeCallbacksAndMessages(null);
            }else if(Constant.ACTIOIN_MUSIC_PLAY.equals(action)){
                ivPlaypause.setImageResource(R.drawable.selector_play);
                //继续播放接着更新时间
                updatePlayTime();
                updateLyric();
            }
        }
    }

    private void updateUI() {
        MusicItem currentMusic = music.getCurrentMusic();
        tvTitle.setText(currentMusic.title);
        tvArtist.setText(currentMusic.artist);
        duration = StringUtils.formatPlayTime(currentMusic.duration);
        tvPlayedTime.setText("00:00/"+duration);
        //让帧动画播放起来
        AnimationDrawable drawable = (AnimationDrawable) ivWave.getBackground();
        drawable.start();
        //给进度条设置最大进度
        sbProgress.setMax(currentMusic.duration);
        //更新播放模式的图标
        updatePlayModeIcon();
        //更新播放暂停图标
        updatePlayIcon();
        //beijingbeijing.mp3  woxiangxin.mp3
        //通过歌曲的名字找到对应的歌词文件名
        String lyricFileName = currentMusic.displayName.split("\\.")[0]+".txt";
        //创建歌词file对象
        File lyricFile = new File(Environment.getExternalStorageDirectory(),lyricFileName);
        //通过歌词的file加载歌词
        lyricView.loadLyric(lyricFile);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(music!=null){
        updatePlayTime();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //页面退出的时候注销广播接收者
        unregisterReceiver(receiver);
        //解除跟服务之间的绑定
        unbindService(conn);
    }

    private void updatePlayIcon(){
        if(music.isPlaying()){
            ivPlaypause.setImageResource(R.drawable.selector_play);
        }else{
            ivPlaypause.setImageResource(R.drawable.selector_pause);
        }
    }

    private class MyOnSeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            //当进度发生改变的时候会走个方法
            //progress当前的进度
            if(fromUser){
                //用户用手拖动进度条 再去修改进度
                music.seekTo(progress);
            }

        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            //开始操作进度条
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            //结束操作进度条
        }
    }
}
