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
	private int[] resIds = new int[]{};
	
	private String[] titles = new String[] {"nc2422", "nc7400", "nc10100", "nc10812", "nc11577",
			"nc13447", "nc20349", "nc20612", "nc29204"};
	
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
