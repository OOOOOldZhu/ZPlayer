package com.z.zmusicplayer.zhu_demo;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.z.zmusicplayer.R;

public class Zhu_Activity extends Activity {
	private static final int UPDATA_PROGRESS = 0;
	private MusicplayService.MyBinder musicController;
	private MyConnection conn;
	private ImageButton ib_play;
	private SeekBar sb_progress;
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
				case UPDATA_PROGRESS:
					//调用更新进度的方法
					updatePlayingProgress();
					break;

				default:
					break;
			}
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_demo);

		ib_play = (ImageButton) findViewById(R.id.ib_play);
		sb_progress = (SeekBar) findViewById(R.id.sb_progress);
		//跟seekbar设置一个进度变化的监听
		sb_progress.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				//用户停止操作进度条
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				//用户开始操作进度条
			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
										  boolean fromUser) {
				//当进度条进度发生改变的时候会走这个方法
				//第二个参数 当前进度条的进度
				//第三个参数 fromUser 当前进度改变是否是由用户拖动进度条产生的
//                如果是用户操作 返回true 如果是代码方式操作返回false
				if(fromUser){
					musicController.seekTo(progress);
				}
			}
		});

		Intent service = new Intent(getApplicationContext(), MusicplayService.class);
		//混合方式开启服务
		startService(service);
		conn = new MyConnection();
		bindService(service, conn, BIND_AUTO_CREATE);
	}

	@Override
	protected void onResume() {
		super.onResume();
		System.out.println("onResume");
		if(musicController!=null){
			updatePlayingProgress();
		}
	}

	private class MyConnection implements ServiceConnection{

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			musicController = (MusicplayService.MyBinder) service;
			System.out.println("onServiceConnected");
			//当获取到musicController(IBinder)对象之后 就可以更新图标的状态
			updatePlayIcon();
			//设置音乐播放的总时长
			sb_progress.setMax(musicController.getDuration());
			//更新当前的进度
			sb_progress.setProgress(musicController.getCurrentPosition());
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {

		}

	}

	public void play(View v){
		//播放暂停 状态切换
		musicController.playPause();
		//根据当前播放状态 更新图标状态
		updatePlayIcon();
//        MediaPlayer player = new MediaPlayer();
//        try {
//            player.setDataSource("mnt/sdcard/xpg.mp3");
//            player.prepare();
//            player.start();
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
	}

	/**
	 * 根据当前播放状态 更新图标状态
	 */
	private void updatePlayIcon(){
		if(musicController.isPlaying()){
			ib_play.setImageResource(R.drawable.btn_audio_pause);
			handler.sendEmptyMessage(UPDATA_PROGRESS);
		}else{
			ib_play.setImageResource(R.drawable.btn_audio_play);
			//把更新进度的消息移除掉
			handler.removeMessages(UPDATA_PROGRESS);
		}
	}

	/**
	 * 更新播放进度
	 */
	private void updatePlayingProgress(){
		System.out.println("updatePlayingProgress");
		//获取当前播放进度 设置到进度条上
		sb_progress.setProgress(musicController.getCurrentPosition());
		//通过handler通知 500ms之后 执行这个任务
		handler.sendEmptyMessageDelayed(UPDATA_PROGRESS, 500);
	}
	@Override
	protected void onPause() {
		super.onPause();
		//移除更新界面的消息
		handler.removeMessages(UPDATA_PROGRESS);
		//如果参数传null 可以移除所有消息
		handler.removeCallbacksAndMessages(null);
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		unbindService(conn);
		handler.removeMessages(UPDATA_PROGRESS);
		musicController = null;
	}
}
