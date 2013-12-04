package com.sato310.music_dhw;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	private Map<String, Integer> idsMap = new HashMap<String, Integer>();
	private int[] resIds = new int[]{R.raw.nc2422, R.raw.nc7400, R.raw.nc10100, R.raw.nc10812, R.raw.nc11577,
			R.raw.nc13447, R.raw.nc20349, R.raw.nc20612, R.raw.nc29204};
	
	private String[] titles = new String[] {"オルゴールBGM", "捨てられた雪原", "ループ用BGM008", "ループ用BGM026", "春の陽気",
			"亡き王女の為のセプテット", "おてんば恋娘", "お茶の時間", "Starting Japan"};
	
	private int setRes = 0;
	// 何曲目かの変数
	private int setResNo;
	private String setMusicTitle;
	private Intent intent;
	private TextView titleText;
	
	// 再生中かどうかの判定
	private boolean playing = false;
	private ListView listView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		final List<String> titleList = new ArrayList<String>();
		
		listView = (ListView)findViewById(R.id.listView1);

		// 1.タイトル みたいな表示
		for (int i = 0; i < titles.length; i++) {
			titleList.add((i + 1) + "." + titles[i]);
			idsMap.put(titles[i], resIds[i]);
		}
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_singlechoice, titleList);
		listView.setAdapter(adapter);
		listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int arg2,
					long arg3) {
				setRes = resIds[arg2];
				setResNo = arg2;
				setMusicTitle = titleList.get(arg2);
			}
		});
		
		titleText = (TextView)findViewById(R.id.title);
		
		// 再生ボタンが押された時の処理
		final ImageButton playButton = (ImageButton)findViewById(R.id.play);
		playButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// setMusicが0の時
				if (setRes == 0) {
					Toast.makeText(MainActivity.this, "曲を選択して下さい", Toast.LENGTH_SHORT).show();
				} else {
					// 曲が停止中に再生ボタンが押された時
					if (playing == false) {
						intent = new Intent(MainActivity.this, SampleService.class);
						intent.putExtra("titleNow", setRes);
						intent.setAction("start");
						startService(intent);
						
						titleText.setText(setMusicTitle);
						// 画像を一時停止画像に変更
						playButton.setImageResource(R.drawable.pause);
						// playingをtrueに変更
						playing = true;
					} else {
						intent.putExtra("titleNow", setRes);
						intent.setAction("start");
						startService(intent);
						
						// 画像を再生画像に変更
						playButton.setImageResource(R.drawable.play);
						// playingをtrueに変更
						playing = false;
					}
				}
			}
		});
		
		// 停止ボタンが押された時の処理
		ImageButton stopButton = (ImageButton)findViewById(R.id.stop);
		stopButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				intent.setAction("stop");
				startService(intent);
				
				// 画像を再生画像に変更
				playButton.setImageResource(R.drawable.pause);
				// playingをtrueに変更
				playing = false;
			}
		});
		
		// 次の曲ボタンが押された時の処理
		ImageButton nextButton = (ImageButton)findViewById(R.id.next);
		nextButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 再生中の曲が最後だったら最初の曲に戻す
				if (setResNo == (resIds.length - 1)) {
					setResNo = 0;
				} else {
					setResNo++;
				}
				setRes = resIds[setResNo];
				setMusicTitle = titles[setResNo];

				intent.putExtra("titleNow", setRes);
				intent.setAction("next");
				startService(intent);
				
				titleText.setText(setMusicTitle);
				
				// 画像を一時停止画像に変更
				playButton.setImageResource(R.drawable.pause);
				// playingをfalseに変更
				playing = true;

				// ラジオボタンのチェック位置を変更する
				listView.setItemChecked(setResNo, true);
			}
		});
		
		// 前の曲ボタンが押された時の処理
		ImageButton previousButton = (ImageButton)findViewById(R.id.previous);
		previousButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 再生中の曲が最初だったらトーストに表示させる
				if (setResNo == 0) {
					setResNo = 0;
					Toast.makeText(MainActivity.this, "最初の曲です。", Toast.LENGTH_SHORT).show();
				} else {
					setResNo--;
					setRes = resIds[setResNo];
					setMusicTitle = titles[setResNo];

					intent.putExtra("titleNow", setRes);
					intent.setAction("next");
					startService(intent);
					
					titleText.setText(setMusicTitle);
					// 画像を一時停止画像に変更
					playButton.setImageResource(R.drawable.pause);
					// playingをfalseに変更
					playing = true;

					// ラジオボタンのチェック位置を変更する
					listView.setItemChecked(setResNo, true);
				}
			}
		});
	}
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
