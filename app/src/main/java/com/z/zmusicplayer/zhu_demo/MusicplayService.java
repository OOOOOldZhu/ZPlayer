package com.z.zmusicplayer.zhu_demo;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class MusicplayService extends Service {


	private MediaPlayer player;

	@Override
	public IBinder onBind(Intent arg0) {
		return new MyBinder();
	}

	@Override
	public void onCreate() {
		super.onCreate();
		//当service创建的时候 就准备 MediaPlayer对象
		player = new MediaPlayer();
		try {
			//设置数据源
			//MusicplayService.this.getResources().getAssets();"assets/"+names[i]
			player.setDataSource("mnt/sdcard/nw.mp3");
			//开始准备
			player.prepare();
			//    player.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.d("zzz", "onCreate: "+e.getMessage());
			e.printStackTrace();
		}
	}

	public class MyBinder extends Binder{

		/**
		 * 跟句当前播放状态 在播放和暂停之间切换
		 */
		public void playPause(){
			//判断MediaPlayer播放状态 是否处于播放中
			if(player.isPlaying()){
				//如果正在播放 就暂停
				player.pause();
			}else{
				//如果处于暂停状态 就开始播放
				player.start();
			}
		}

		/**
		 * 返回当前音乐播放器的播放状态
		 * @return
		 */
		public boolean isPlaying(){
			return player.isPlaying();
		}

		/**
		 * 获取当前音乐的总时长(毫秒值)
		 * @return
		 */
		public int getDuration(){
			return player.getDuration();
		}

		/**
		 * 获取当前播放的位置
		 * @return
		 */
		public int getCurrentPosition(){
			return player.getCurrentPosition();
		}

		/**
		 * 移动到指定位置 播放
		 * @param position
		 */
		public void seekTo(int position){
			player.seekTo(position);
		}
	}
}
