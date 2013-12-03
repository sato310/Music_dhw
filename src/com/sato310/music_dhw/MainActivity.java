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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends Activity {
	
	private Map<String, Integer> idsMap = new HashMap<String, Integer>();
	private int[] resIds = new int[]{R.raw.nc2422, R.raw.nc7400, R.raw.nc10100, R.raw.nc10812, R.raw.nc11577,
			R.raw.nc13447, R.raw.nc20349, R.raw.nc20612, R.raw.nc29204};
	
	private String[] titles = new String[] {"オルゴールBGM", "捨てられた雪原", "ループ用BGM008", "ループ用BGM026", "春の陽気",
			"亡き王女の為のセプテット", "おてんば恋娘", "お茶の時間", "Starting Japan"};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		List<String> titleList = new ArrayList<String>();
		
		ListView listView = (ListView)findViewById(R.id.listView1);

		
		for (int i = 0; i < titles.length; i++) {
			titleList.add(titles[i]);
			idsMap.put(titles[i], resIds[i]);
			
		}
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_singlechoice, titleList);
		listView.setAdapter(adapter);
		listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int arg2,
					long arg3) {
				Intent intent = new Intent(MainActivity.this, SampleService.class);
				intent.putExtra("titleNow", resIds[arg2]);
				intent.setAction("start");
				startService(intent);
				
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
