package com.sato310.music_dhw;

import java.util.ArrayList;
import java.util.Collections;

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

	private ArrayList<Integer> resIdList;
	public static int[] resIds = new int[] { R.raw.nc2422, R.raw.nc7400,
			R.raw.nc10100, R.raw.nc10812, R.raw.nc11577, R.raw.nc13447,
			R.raw.nc20349, R.raw.nc20612, R.raw.nc29204 };

	public static String[] titles = new String[] { "オルゴールBGM", "捨てられた雪原",
			"ループ用BGM008", "ループ用BGM026", "春の陽気", "亡き王女の為のセプテット", "おてんば恋娘",
			"お茶の時間", "Starting Japan" };

	public static int setRes = 0;
	// 何曲目かの変数
	private int setResNo;
	public static String setMusicTitle;
	private Intent intent;
	public static TextView titleText;

	// 再生中かどうかの判定
	private boolean playing = false;
	public static ListView listView;
	private ImageButton playButton;

	// シャッフルするかどうかの判定
	private boolean shufflePlay = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		final ArrayList<String> titleList = new ArrayList<String>();
		resIdList = new ArrayList<Integer>();
		for (int i = 0; i < resIds.length; i++) {
			resIdList.add(resIds[i]);
		}

		listView = (ListView) findViewById(R.id.listView1);

		// 1.タイトル みたいな表示
		for (int i = 0; i < titles.length; i++) {
			titleList.add((i + 1) + "." + titles[i]);
		}

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.select_dialog_singlechoice, titleList);
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

		// シャッフルボタンの判定
		final ImageButton shuffleButton = (ImageButton) findViewById(R.id.shuffle);
		shuffleButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (shufflePlay) {
					shuffleButton.setImageResource(R.drawable.shuffle);
					shufflePlay = false;
				} else {
					shuffleButton.setImageResource(R.drawable.repeat);
					// シャッフルボタンが押されたら配列をシャッフルする
					Toast.makeText(MainActivity.this, "シャッフルしました。",
							Toast.LENGTH_SHORT).show();
					Collections.shuffle(resIdList);
					shufflePlay = true;
				}

			}
		});

		titleText = (TextView) findViewById(R.id.title);

		playButton = (ImageButton) findViewById(R.id.play);
		playButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// setMusicが0の時
				if (setRes == 0) {
					Toast.makeText(MainActivity.this, "曲を選択して下さい",
							Toast.LENGTH_SHORT).show();
				} else {
					intent = new Intent(MainActivity.this, SampleService.class);
					intent.putIntegerArrayListExtra("resIdList", resIdList);
					intent.putExtra("setRes", setRes);
					intent.putExtra("setResNo", setResNo);
					intent.setAction("start");
					startService(intent);

					// 曲が停止中に再生ボタンが押された時
					if (playing == false) {
						titleText.setText(setMusicTitle);
						// 画像を一時停止画像に変更
						playButton.setImageResource(R.drawable.pause);
						// playingをtrueに変更
						playing = true;
					} else {
						// 画像を再生画像に変更
						playButton.setImageResource(R.drawable.play);
						// playingをtrueに変更
						playing = false;
					}
				}
			}
		});

		// 停止ボタンが押された時の処理
		ImageButton stopButton = (ImageButton) findViewById(R.id.stop);
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
		ImageButton nextButton = (ImageButton) findViewById(R.id.next);
		nextButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (shufflePlay) {
					if (setResNo == (resIds.length - 1)) {
						Collections.shuffle(resIdList);
						setResNo = 0;
					} else {
						setResNo++;
					}
					setRes = resIdList.get(setResNo);

					intent.putExtra("setRes", setRes);
					intent.putExtra("setResNo", setResNo);
					intent.putIntegerArrayListExtra("resIdList", resIdList);
					intent.setAction("next");
					startService(intent);
					for (int i = 0; i < resIds.length; i++) {
						if (setRes == resIds[i]) {
							setResNo = i;
							setMusicTitle = titles[setResNo];
						}
					}
				} else {
					// 再生中の曲が最後だったら最初の曲に戻す
					if (setResNo == (resIds.length - 1)) {
						setResNo = 0;
					} else {
						setResNo++;
					}
					setRes = resIds[setResNo];
					setMusicTitle = titles[setResNo];

					intent.putExtra("setRes", setRes);
					intent.putExtra("setResNo", setResNo);
					intent.setAction("next");
					startService(intent);
				}

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
		ImageButton previousButton = (ImageButton) findViewById(R.id.previous);
		previousButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (shufflePlay) {
					// 再生中の曲が最初だったらトーストに表示させる
					if (setResNo == 0) {
						Collections.shuffle(resIdList);
						setResNo = (resIds.length - 1);
					} else {
						setResNo--;
					}
					setRes = resIdList.get(setResNo);

					intent.putExtra("setRes", setRes);
					intent.putExtra("setResNo", setResNo);
					intent.putIntegerArrayListExtra("resIdList", resIdList);
					intent.setAction("next");
					startService(intent);
					for (int i = 0; i < resIds.length; i++) {
						if (setRes == resIds[i]) {
							setResNo = i;
							setMusicTitle = titles[setResNo];
						}
					}

				} else {
					// 再生中の曲が最初だったらトーストに表示させる
					if (setResNo == 0) {
						setResNo = 0;
						Toast.makeText(MainActivity.this, "最初の曲です。",
								Toast.LENGTH_SHORT).show();
					} else {
						setResNo--;
						setRes = resIds[setResNo];
						setMusicTitle = titles[setResNo];

						intent.putExtra("setRes", setRes);
						intent.putExtra("setResNo", setResNo);
						intent.setAction("next");
						startService(intent);
					}
				}
				titleText.setText(setMusicTitle);
				// 画像を一時停止画像に変更
				playButton.setImageResource(R.drawable.pause);
				// playingをfalseに変更
				playing = true;

				// ラジオボタンのチェック位置を変更する
				listView.setItemChecked(setResNo, true);

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
