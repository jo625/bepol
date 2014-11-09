package com.bepol.beaconNActivities;

import java.util.ArrayList;

import com.bepol.databases.PoisDbOpenHelper;

import com.bepol.R;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

/* 목적지 선택을 위한 액티비티 클래스 */

public class DestsetActivity extends Activity implements View.OnClickListener {
	private PoisDbOpenHelper pdoh; 				//Beacon DB
	private ArrayList<String> items = null;		
	private ArrayList<String> itemCpy = null;	
	private ArrayAdapter<String> adapter = null;
 	private EditText dest;                       //목적지 입력 텍스트
	private EditText dept;                       //출발지 입력 텍스트
	private ImageButton clearBtn;                //Clear 버튼
	private ListView lv;                         //목적지 리스트
	private Button DestSetBtn;					 //목적지 설정 버튼
	Toast starttoast;							 //Toast
	private String departure;					 //?
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_destset);
		
		/* DB OPEN */
		pdoh = new PoisDbOpenHelper(DestsetActivity.this);
		pdoh.open();
		
		/* Clear 버튼 */
		clearBtn = (ImageButton)findViewById(R.id.imageButton1);
		clearBtn.bringToFront();
		
		/* 목적지 선택 버튼 */
		DestSetBtn = (Button) findViewById(R.id.destSaveBtn);
		DestSetBtn.setOnClickListener(this);
		
		/* 목적지, dept */
		dest = (EditText)findViewById(R.id.destText);
		dept = (EditText)findViewById(R.id.editText1);
		
		
		Bundle bundle = getIntent().getExtras(); //출발지 받아옴 
		dest.setText("");
		
		/* 출발지 Set */
		departure = bundle.getString("dptr");
		//dept.setText(bundle.getString("dptr"));
		dept.setText(departure); //출발지 설정
		
		/* 목적지 리스트 lv, items, adapter */
		lv = (ListView)findViewById(R.id.listView1);
		items = new ArrayList<String>();
		adapter = new ArrayAdapter<String>(this, R.layout.pois_list_item, R.id.poi_name, items);
		
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(new ListViewItemOnClickListener());
		
		clearBtn.setOnTouchListener(new OnTouchListener() {
			/* Clear 버튼 선택 시 */
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction() == MotionEvent.ACTION_DOWN){ //누를 때
					clearBtn.setBackground(getResources().getDrawable(R.drawable.ic_selected));
				} else if(event.getAction() == MotionEvent.ACTION_UP){ //뗄 때
					dest.setText(""); 								   //목적지 초기화
					clearBtn.setVisibility(View.INVISIBLE);			   //버튼 안보이기
				}
				return false;
			}
		});
		
		
		/* 자동 검색 */
		dest.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				//DestsetActivity.this.adapter.getFilter().filter(s);
				items.clear();
				for(String ss : itemCpy){
					if(ss.contains(dest.getText().toString().toUpperCase()))
						items.add(ss);
				}
				adapter.notifyDataSetChanged();
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				if(dest.getText().length()==1){
					clearBtn.setBackground(getResources().getDrawable(R.drawable.ic_selected));
					clearBtn.setVisibility(View.VISIBLE);
				}else if(dest.getText().length()==0){
					clearBtn.setVisibility(View.INVISIBLE);
				}
			}
		});
		
		/* 리스트에 포이들 가져오기 */
		Cursor pois = pdoh.searchColumn();
		pois.moveToFirst();
		while(!pois.isAfterLast()){
			if(!pois.getString(0).equals(departure))	//출발지는 목록에서 제외
				items.add(pois.getString(0) + "\n" + pois.getString(1) + "\n" + pois.getString(2));
			pois.moveToNext();
			
		}
		pois.close();
		pdoh.close();
		
		itemCpy = new ArrayList<String>();
		itemCpy.addAll(items);		
	}
	
	/* 목적지 클릭했을 때  */
	private class ListViewItemOnClickListener implements AdapterView.OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
			// TODO Auto-generated method stub
			String[] token;
			token = (items.get(position)).split("\n");
			dest.setText(token[0]); //클릭한 목적지로 설정			
		}		
	}	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.destset, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {
		case R.id.destSaveBtn:
			Intent intent = new Intent();
			if(dept.getText().toString().equals(""))
			{
				starttoast = Toast.makeText(getApplicationContext(),"출발지가 설정되지 않았습니다", Toast.LENGTH_SHORT);
	  			starttoast.setGravity(Gravity.CENTER, 0, 0);
	  			starttoast.show();
	  			break;
			}
			intent.putExtra("StartOut", dept.getText().toString());
			intent.putExtra("DestOut", dest.getText().toString());
			this.setResult(RESULT_OK, intent);
			finish();
			break;
		}
	}	
}
