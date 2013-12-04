package com.sato310.music_dhw;

import java.util.ArrayList;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.IBinder;
import android.widget.Toast;

public class SampleService extends Service implements OnCompletionListener {

	private MediaPlayer mediaPlayer;
	private int setResNo;
	private ArrayList<Integer> resIdList;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		int rawId = intent.getIntExtra("setRes", 0);
		setResNo = intent.getIntExtra("setResNo", 0);
		resIdList = intent.getIntegerArrayListExtra("resIdList");
		// rawIdが0以外の時のみ実行
		if (rawId != 0) {
			// 前のActivityから"start"という文字列が渡された時
			if (intent.getAction().equals("start")) {
				// mediaPlayerが何もない状態であればcreateする
				if (mediaPlayer == null) {
					mediaPlayer = MediaPlayer.create(this, rawId);
				}
				if (mediaPlayer.isPlaying()) {
					mediaPlayer.pause();
					mediaPlayer = MediaPlayer.create(this, rawId);
				} else {
					mediaPlayer = MediaPlayer.create(this, rawId);
					mediaPlayer.start();
				}
			} else if (intent.getAction().equals("next")) {
				// mediaPlayerが何もない状態であればcreateする
				if (mediaPlayer == null) {
					mediaPlayer = MediaPlayer.create(this, rawId);
				}
				if (mediaPlayer.isPlaying()) {
					mediaPlayer.pause();
					mediaPlayer = MediaPlayer.create(this, rawId);
					mediaPlayer.start();
				} else {
					mediaPlayer = MediaPlayer.create(this, rawId);
					mediaPlayer.start();
				}
			} else if (intent.getAction().equals("stop")) {
				// 曲が再生中なら停止する
				if (mediaPlayer.isPlaying()) {
					mediaPlayer.stop();
					// mediaPlayerのメモリを開放する
					mediaPlayer.release();
					// mediaPlayerの変数も無くす
					mediaPlayer = null;
				}
			}
			// 曲の再生が終了した時に呼ばれる
			mediaPlayer.setOnCompletionListener(this);
		}
		return super.onStartCommand(intent, flags, startId);
	}

	// 再生終了イベントが発生すると呼ばれるメソッド, 次の曲へ移動
	@Override
	public void onCompletion(MediaPlayer mp) {
		setResNo++;
		mediaPlayer = mediaPlayer = MediaPlayer.create(this,
				resIdList.get(setResNo));
		mediaPlayer.start();
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
