package com.sato310.music_dhw;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

public class SampleService extends Service {

	private MediaPlayer mediaPlayer;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		int rawId = intent.getIntExtra("titleNow", 0);
		// 前のActivityから"start"という文字列が渡された時
		if (intent.getAction().equals("start")) {
			// mediaPlayerが何もない状態であればcreateする
			if (mediaPlayer == null) {
				mediaPlayer = MediaPlayer.create(this, rawId);
			}
			if (mediaPlayer.isPlaying()) {
				mediaPlayer.stop();
				mediaPlayer = MediaPlayer.create(this, rawId);
			}
			mediaPlayer.start();
		}
		return super.onStartCommand(intent, flags, startId);
	}
	
	// サービスがなくなる時に呼ばれるメソッド
	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mediaPlayer != null) {
			// mediaPlayerのメモリを開放する
			mediaPlayer.release();
			// mediaPlayerの変数も無くす
			mediaPlayer = null;
		}
	}

}
