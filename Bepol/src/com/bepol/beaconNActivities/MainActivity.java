package com.bepol.beaconNActivities;


import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Surface;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bepol.R;
import com.bepol.databases.EdgesDbOpenHelper;
import com.bepol.databases.PoisDbOpenHelper;
import com.getDirection.DijkstraAlgorithm;
import com.getDirection.Edge;
import com.getDirection.Graph;
import com.getDirection.Vertex;


public class MainActivity extends ListActivity implements SensorEventListener,OnClickListener{
	private BluetoothAdapter mBluetoothAdapter;
	//private LeDeviceListAdapter mLeDeviceListAdapter;
	private BeaconInfo mBeaconInfo;
	
	//sensors
	private SensorManager mSensorManager;
	private Sensor mAccel;
	private Sensor mMagnet;

	private static final int REQUEST_ENABLE_BT = 1;
	private static final long SCAN_PERIOD = 1000000;
	boolean mCanSeeBeacon;
	private Handler mHandler;
	ImageView img;	//icon
	ImageView img2;	//hanback
	TranslateAnimation animation;
	/* icon's position now */
	public static float curX;
	public static float curY;
	/* Distance */
	TextView distanceText;
	TextView rotateText;
	Toast toast;

	float acceleration;
	float speedAfter;
	float speedBefore;
	float dis;

	static final int OFFSET_COUNT = 100;
	private static final int ACT_EDIT = 0;
	
	//private static final int ACT_EDIT2 = 0;
	//For calculate
	
	static float nTime;
	static double xValue;
	static double yValue;
	static double zValue;
	static double New_Acc_Value;
	static float[] Values;
	static double[] Norm_Acc;
	static double Acc_HPAvg;
	static double Acc_HPFiltered;
	static double Acc_LPFiltered;
	static double prev_filteredAcc;
	static double min_filteredAcc;
	static double max_filteredAcc;
	static float prev_time;
	static float cur_time;
	static double m;
	static double interceptX;
	static int check;
	static int count;
	static int move_count;
	static double stepLength;
	static int stepCnt;

	private ArrayList<Double> myList = new ArrayList<Double>();
	private float step_size_weinberg;
	private float k_weinberg;
	private float dis_weinberg;
	private float prev_distance;

	/* 방향 */
	private Button btnComplete;
	Dialog calDialog;
	private Display display;
	private float inputR[];
	private float I[];
	private ArrayList<Float> myOrient = new ArrayList<Float>();
	static AlertDialog alertDialog;

	/* 모드, 목적지 */
	static String MODE = "PED";
	public static String depart = "";
	
	static float destX = .0f;
	static float destY = .0f;

	/* 네비 */
	private static DisplayMetrics displaymetrics;
	private static float height;
	private static float width;
	private static String start;
	private static String dest;
	public MyView navi;
	private static int navicount=0;
	private static int navicount2=0;
	

	
	
	//Dialog FinishDialog;
	private Button btnComplete2;
	Toast navitoast;
	Toast StartToast;
	//Intent destSettingIntent = new Intent(MainActivity.this, DestsetActivity.class);
	//Intent NaviIntent = new Intent(MainActivity.this, MyView.class);

	/* Dijkstra Test */
	private List<Vertex> nodes;
	private List<Edge> edges;	
	private PoisDbOpenHelper pdoh;
	private EdgesDbOpenHelper edoh;

	private String listNode = "";
	private String listEdge = "";

	TextView nodeList;
	TextView edgeList;

	Graph graph;
	DijkstraAlgorithm dijkstra;

	static LinkedList<Vertex> path;

	/*	시작화면	*/
	static int flag = 1;
	private void SettingBLE(){	//Set Bluetooth manager and adapter 
		final BluetoothManager bluetoothManager =
				(BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
		mBluetoothAdapter = bluetoothManager.getAdapter();
	}
	private void EnableBluetooth(){	//Is Bluetooth available? 
		if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
			Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
			
		}
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		distanceText = (TextView)findViewById(R.id.textView1);
		rotateText = (TextView)findViewById(R.id.rotateText);
		mBeaconInfo = new BeaconInfo(this, (ImageView)findViewById(R.id.imageView1), distanceText, rotateText);
		//list_beacon need for show informations
		ListView listView = (ListView)findViewById(R.id.list_beacons);
		listView.setAdapter(mBeaconInfo);
		mHandler = new Handler();
		SettingBLE();
		EnableBluetooth();
		img = (ImageView)findViewById(R.id.imageView1);
		//img.setPivotX(0.5f);
		//img.setPivotY(0.5f);
		img2 = (ImageView)findViewById(R.id.imageView2);
		img2.setX(300.0f);
		img2.setY(500.0f);
		animation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT,0,Animation.RELATIVE_TO_SELF,0.04f,
				Animation.RELATIVE_TO_PARENT,0,Animation.RELATIVE_TO_SELF,0.12f);
		animation.setDuration(100);
		animation.setFillAfter(true);
		img2.startAnimation(animation);
		/* Sensor Register */
		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		mAccel = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		mMagnet = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

		//X,Y,Z Values of Accelerometer
		Norm_Acc = new double[10];
		initValues();
		display = ((WindowManager) this.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();


		calDialog = new Dialog(MainActivity.this);
		calDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		calDialog.setContentView(R.layout.activity_calibrate_dialog);
		calDialog.getWindow().setTitleColor(Color.BLACK);
		calDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.WHITE));
		btnComplete = (Button)calDialog.findViewById(R.id.btnCalComp);
		btnComplete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				calDialog.cancel();
				initValues();
			}
		});
		calDialog.show();

		/* 네비 */
		displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		height = displaymetrics.heightPixels;
		width = displaymetrics.widthPixels;

		navi = new MyView(this);
		navi.setHeight(height);
		navi.setWidth(width);


		alertDialog = new AlertDialog.Builder(MainActivity.this).create();
		alertDialog.setTitle("목적지에 도착했습니다!");
		alertDialog.setMessage("경로 안내를 종료합니다.");
		alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "확인", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				alertDialog.dismiss();
				dest="";
				navi.canvas2.drawColor(Color.TRANSPARENT, Mode.CLEAR);
			}
		});


		/* Dijkstra */
		nodes = new ArrayList<Vertex>();
		edges = new ArrayList<Edge>();

		pdoh = new PoisDbOpenHelper(MainActivity.this);
		pdoh.open();
		edoh = new EdgesDbOpenHelper(MainActivity.this);
		edoh.open();

		Cursor pois = pdoh.searchColumn();
		pois.moveToFirst();
		while(!pois.isAfterLast()){
			//listNode += pois.getString(3)+"\n";
			Vertex location = new Vertex(pois.getString(3), pois.getString(0), pois.getFloat(1), pois.getFloat(2));
			nodes.add(location);
			pois.moveToNext();
		}
		pois.close();
		pdoh.close();

		Cursor eois = edoh.searchColumn();
		eois.moveToFirst();
		while(!eois.isAfterLast()){
			//listEdge += eois.getString(0)+"\n";			
			Edge lane = new Edge(eois.getString(0), nodes.get(eois.getInt(1)), nodes.get(eois.getInt(2)), eois.getInt(3));
			edges.add(lane);
			eois.moveToNext();
		}
		eois.close();
		edoh.close();

		graph = new Graph(nodes, edges);
		dijkstra = new DijkstraAlgorithm(graph);

		edgeList = (TextView) findViewById(R.id.listEdge);

		/*	for(Vertex test : nodes){
			listNode += test+"\n";
		}

		for(Edge test : edges){
			listEdge += test+"\n";
		}
		 */
		nodeList = (TextView) findViewById(R.id.listNode);
		edgeList = (TextView) findViewById(R.id.listEdge);

		nodeList.setText(listNode);
		edgeList.setText(listEdge);

		
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		switch (requestCode) {
		case ACT_EDIT:
			if (resultCode == RESULT_OK) {
				/* Dijkstra */
				pdoh = new PoisDbOpenHelper(MainActivity.this);
				pdoh.open();
				start = data.getStringExtra("StartOut");
				navi.setStart(start);
				Cursor startPoint = pdoh.searchPoi(start);
				startPoint.moveToFirst();
				dijkstra.execute(nodes.get(startPoint.getInt(3)-1));

				startPoint.close();

				dest = data.getStringExtra("DestOut");
				navi.setDest(dest);
				Cursor EndPoint = pdoh.searchPoi(dest);
				EndPoint.moveToFirst();
				path = dijkstra.getPath(nodes.get(EndPoint.getInt(3)-1));

				EndPoint.close();
				pdoh.close();

				listNode = "";

				for(Vertex test : path){
					listNode += test.getX() + "\n";
				}
				MyView.setPath(path);

				edgeList.setText(listNode);
			}
			break;
		case REQUEST_ENABLE_BT :
			if(resultCode == RESULT_CANCELED)
				finish();
			break;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		menu.clear();
		if(MODE.equals("NAVI")){
			menu.add(0, 2, Menu.NONE, "목적지 설정");
			menu.add(0, 1, Menu.NONE, "경로 안내 종료");
		}else if(MODE.equals("PED")){	//보행자 모드일 때
			menu.add(1, 2, Menu.NONE, "목적지 설정");
			menu.add(1, 1, Menu.NONE, "경로 안내 종료");
		}
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		/*case 0:
        	MODE = "PED";
            break;*/
		case 1:	//안내 종료
			MODE = "NAVI";
			navi.canvas2.drawColor(Color.TRANSPARENT, Mode.CLEAR);
			navi.setStart("");
			navi.setDest("");
			navicount = 0;
			break;
		case 2:	//새로운 목적지 설정
			if(!(navi.getDest().equals(""))){	//목적지가 이미 설정되어 있을 때
				navitoast = Toast.makeText(getApplicationContext(),"경로 안내 중입니다", Toast.LENGTH_SHORT);
				navitoast.setGravity(Gravity.CENTER, 0, 0);
				navitoast.show();
				return true;
			}
			Intent destSettingIntent = new Intent(MainActivity.this, DestsetActivity.class);
			
			if(depart.equals("")){
				StartToast = Toast.makeText(getApplicationContext(),"출발지가 설정되지 않았습니다", Toast.LENGTH_SHORT);
				StartToast.setGravity(Gravity.CENTER, 0, 0);
				StartToast.show();
				return true;
			}
			destSettingIntent.putExtra("dptr", depart);
			//startActivity(destSettingIntent);
			startActivityForResult(destSettingIntent, ACT_EDIT);
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	/* 거리를 다시 재야할 때 초기화하는 변수들 */
	public void initValues()
	{
		xValue = .0;				yValue = .0;				zValue = .0;

		New_Acc_Value = .0;
		Acc_HPAvg = 9.8;			Acc_HPFiltered=.0;			Acc_LPFiltered = .0;

		count = 0;					move_count = 0;				stepCnt = 0;
		stepLength = .0f;

		for(int i=0; i<10; i++)		Norm_Acc[i]=.0;

		nTime = .0f;				cur_time = 0;				prev_time = 0;
		m = 0;						check = 0;
		interceptX = .0;

		min_filteredAcc = .0;		max_filteredAcc = .0;

		step_size_weinberg = 0;		k_weinberg = 0.55f;		prev_distance = 0;
		dis_weinberg = 0;

		dest = "";
	}
	protected void onResume(){
		super.onResume();

		mSensorManager.registerListener(this, mAccel, SensorManager.SENSOR_DELAY_GAME);
		mSensorManager.registerListener(this, mMagnet, SensorManager.SENSOR_DELAY_UI);
		scanLeDevice(true);        
	}
	protected void onPause(){
		super.onPause();
		scanLeDevice(false);
		mSensorManager.unregisterListener(this);

	}
	protected void onDestroy(){
		super.onDestroy();
		scanLeDevice(false);
		mSensorManager.unregisterListener(this);
		navi.setStart("");
		navi.setDest("");

	}

	private void scanLeDevice(final boolean enable) {
		if (enable) {
			// Stops scanning after a pre-defined scan period.
			mHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					mBluetoothAdapter.stopLeScan(mLeScanCallback);
					invalidateOptionsMenu();
				}
			}, SCAN_PERIOD);

			mBluetoothAdapter.startLeScan(mLeScanCallback);
		} else {
			mBluetoothAdapter.stopLeScan(mLeScanCallback);
		}
		invalidateOptionsMenu();
	}

	// Device scan callback.
	private BluetoothAdapter.LeScanCallback mLeScanCallback =
			new BluetoothAdapter.LeScanCallback() {

		@Override
		public void onLeScan(final BluetoothDevice device, final int rssi, final byte[] scanRecord) {
			runOnUiThread(new Runnable() {
				// TODO: Add this signal to a list of signals found for this scan
				@Override
				public void run() {
					String major = IntToHex2(Byte.toString(scanRecord[25]),Byte.toString(scanRecord[26]));
					String minor = IntToHex2(Byte.toString(scanRecord[27]),Byte.toString(scanRecord[28]));
					if (!mBeaconInfo.hasDeviceWithAddr(device.getAddress())) {
						// Add the BLE signal to our list adapter
						//BeaconSignal(String address, int rssi, int major, int minor)
						mBeaconInfo.add(new BeaconSignal(device.getAddress(), rssi, major, minor));
					}
					else {
						// Update the stored signal with the given address
						mBeaconInfo.updateRssiForDevice(device.getAddress(), rssi);
						mBeaconInfo.updateMajorForDevice(device.getAddress(), major);
					}
				}
			});
		}
	};

	private String IntToHex2(String b1, String b2) {
		//exchage front with behind
		//byte[] hexbyte = {Byte.parseByte(b2), Byte.parseByte(b1)};
		byte[] hexbyte = {Byte.parseByte(b1), Byte.parseByte(b2)};
		String sb = "";
		String hexaDecimal;

		for(int x = 0 ; x < hexbyte.length ; x++)
		{
			hexaDecimal = "0" + Integer.toHexString(0xff & hexbyte[x]);
			sb += hexaDecimal.substring(hexaDecimal.length()-2);
		}
		// 16 system Strings To integer
		return String.valueOf(Integer.parseInt(sb.toString(),16));
	}

	static class ViewHolder {
		TextView deviceAddress;
		TextView deviceRssi;
		TextView deviceMajor;
		TextView deviceMinor;
	}

	float[] mGeomagnetic;
	float[] mGravity;
	//float[] mRot;
	
	@Override
	public void onSensorChanged(SensorEvent event) {
		float azimut;
		// TODO Auto-generated method stub
		if(count==0){
			toast = Toast.makeText(getApplicationContext(),"8자로 흔들어주세요", Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER, 0, 0);
			//toast.show();
		}
		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
			//mGravity = event.values;
			mGravity = lowPass(event.values.clone(), mGravity, 0.2f);
		}
		if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD){
			//mGeomagnetic = event.values;
			mGeomagnetic = lowPass(event.values.clone(), mGeomagnetic, 0.5f);
		}
		if (mGravity != null && mGeomagnetic != null) {
			inputR = new float[16];
			I = new float[16];
			boolean success = SensorManager.getRotationMatrix(inputR, I, mGravity, mGeomagnetic);
			if (success) {
				//remapCordinateSystem();
				float orientation[] = new float[3];
				SensorManager.getOrientation(inputR, orientation);
				// 회전 각도 변경
				azimut = (float)(Math.toDegrees(orientation[0])+88); // orientation contains: azimut, pitch and roll
				if(azimut<0)
					azimut += 360;
				img.setRotation(azimut);
				img2.setRotation(azimut);
			}
		}

		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

			if(distanceText.getText().equals("chngd")){
				distanceText.setText("0");
				initValues();
			}
			if(count>OFFSET_COUNT) {
				//toast.cancel();

				Values = event.values;

				//High-pass Filter
				New_Acc_Value = Math.sqrt(Values[0]*Values[0]+Values[1]*Values[1]+Values[2]*Values[2]);
				Acc_HPAvg = (New_Acc_Value + (Acc_HPAvg - New_Acc_Value)*0.99);
				Acc_HPFiltered = New_Acc_Value - Acc_HPAvg;

				//Low-pass Filter(Average of recent 10 data)
				if(check<10){
					Norm_Acc[check] = Acc_HPFiltered;
					check++;
				}
				else if(check==10){
					Acc_LPFiltered = 0.1*(Norm_Acc[0]+Norm_Acc[1]+Norm_Acc[2]+Norm_Acc[3]+Norm_Acc[4]+Norm_Acc[5]+Norm_Acc[6]+Norm_Acc[7]+Norm_Acc[8]+Norm_Acc[9]);		
					myList.add(Acc_LPFiltered);	//store all of acc value before the step					
					check++;
				}
				else if(check>10){
					Vector_Shift(Norm_Acc, Acc_HPFiltered);
					Acc_LPFiltered = (0.1*(Norm_Acc[0]+Norm_Acc[1]+Norm_Acc[2]+Norm_Acc[3]+Norm_Acc[4]+Norm_Acc[5]+Norm_Acc[6]+Norm_Acc[7]+Norm_Acc[8]+Norm_Acc[9]));               
					myList.add(Acc_LPFiltered);	//store all of acc value before the step
				}
				if(Acc_LPFiltered<min_filteredAcc)
					min_filteredAcc = Acc_LPFiltered;
				if(Acc_LPFiltered>max_filteredAcc)
					max_filteredAcc = Acc_LPFiltered;
				if(count==101){	//first value
					prev_filteredAcc = Acc_LPFiltered;
					prev_time = nTime;
				}
				else{	//not the first value
					if((prev_filteredAcc*Acc_LPFiltered<0)&&(prev_filteredAcc<Acc_LPFiltered)&&(min_filteredAcc<-0.5||max_filteredAcc>0.5)){
						move_count++;
						stepCnt++;	//one step
						if(move_count>1){	//two move = one step
							double opd = Math.pow(Collections.max(myList)- Collections.min(myList), 1.0/4.0);
							step_size_weinberg = (float) (k_weinberg * opd);
							dis_weinberg  += step_size_weinberg;
							move_count = 1;
							myList.clear();
						}
						min_filteredAcc = .0;
						max_filteredAcc = .0;
						if((int)prev_distance<(int)dis_weinberg){	//moved distance is more than 1m
							mBeaconInfo.moveForward(img.getRotation());
							prev_distance = dis_weinberg;
							

							
							//종료안내, 비콘없는 부분//L
							if(dest.equals("4133(기초전자실습실뒤)") ){
								if((mBeaconInfo.getX()>=0.3378f && mBeaconInfo.getX()<=0.392f) && 
										(mBeaconInfo.getY()>=0.601f && mBeaconInfo.getY()<=0.682f))
									navicount++;
								if(navicount>2){
									depart = "4133(기초전자실습실뒤)";
									//navi.setStart("4133(기초전자실습실뒤)");
									navi.setDest("");
									navicount=0;
									alertDialog.show();
								}
							}
							//M
							/*else if(dest.equals("4105(멀티미디어실습실앞)") || dest.equals("4109(모바일응용)") ){  
								if((mBeaconInfo.getX()>=0.139f && mBeaconInfo.getX()<=0.167f) && 
										(mBeaconInfo.getY()>=0.507f && mBeaconInfo.getY()<=0.5851f))
									navicount++;
								if(navicount>2){
									depart = MyView.getDest();
									navi.setDest("");
									navicount=0;
									alertDialog.show();
								}
							}*/
							//N
							else if(dest.equals("화장실") ){  
								if((mBeaconInfo.getX()>=0.192f && mBeaconInfo.getX()<=0.287f) && 
										(mBeaconInfo.getY()>=0.3759f && mBeaconInfo.getY()<=0.4191f))
									navicount++;
								if(navicount>2){
									depart = "화장실";
									navi.setDest("");
									navicount=0;
									alertDialog.show();
								}
							}
							
							//O
							else if(dest.equals("4110") ){  
								if((mBeaconInfo.getX()>=0.139f && mBeaconInfo.getX()<=0.167f) && 
										(mBeaconInfo.getY()>=0.29f && mBeaconInfo.getY()<=0.3491f))
									navicount++;
								if(navicount>1){
									depart = "4110";
									navi.setDest("");
									navicount=0;
									alertDialog.show();
								}
							}
							//P
							else if(dest.equals("4127(전자기계실습실앞)") ){  
								if((mBeaconInfo.getX()>=0.3378f && mBeaconInfo.getX()<=0.392f) && 
										(mBeaconInfo.getY()>=0.222f && mBeaconInfo.getY()<=0.3f))
									navicount++;
								if(navicount>2){
									navi.setStart("");
									navi.setDest("");
									navicount=0;
									alertDialog.show();
								}
							}
						
							
							
						}
					}
					prev_filteredAcc = Acc_LPFiltered;
					prev_time = cur_time;
				}
				//set textView value 
				distanceText.setText(String.format("%.3f", dis_weinberg));
			}
			count++;
			nTime += 0.02f;
		}
	}
	@SuppressWarnings("static-access")
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		if(accuracy<=mSensorManager.SENSOR_STATUS_ACCURACY_LOW){

		}
		// TODO Auto-generated method stub

	}
	static void Vector_Shift(double[] x, double y){
		for(int i=0; i<x.length-1; i++)
			x[i] = x[i+1];

		x[x.length-1] = y;
		
		
		
	}
	//111111
	public static void setDepart(String Depart){
		depart = Depart;
	}

	/**
	 * @see http
	 *      ://en.wikipedia.org/wiki/Low-pass_filter#Algorithmic_implementation
	 * @see http
	 *      ://developer.android.com/reference/android/hardware/SensorEvent.html
	 *      #values
	 */

	//final static ALPHA;

	protected float[] lowPass(float[] input, float[] output, float alpha)
	{
		if (output == null)
			return input;

		for (int i = 0; i < input.length; i++)
		{
			output[i] = output[i] + alpha * (input[i] - output[i]);
		}
		return output;
	}

	private void remapCordinateSystem(){
		int rotation = display.getRotation();
		if (rotation == Surface.ROTATION_0) // Default display rotation is portrait
			SensorManager.remapCoordinateSystem(inputR, SensorManager.AXIS_X, SensorManager.AXIS_Y, inputR);
		else if (rotation == Surface.ROTATION_180) // Default display rotation is reverse portrait
			SensorManager.remapCoordinateSystem(inputR, SensorManager.AXIS_MINUS_X, SensorManager.AXIS_MINUS_Y, inputR);
		else if (rotation == Surface.ROTATION_90) // Default display rotation is landscape
			SensorManager.remapCoordinateSystem(inputR, SensorManager.AXIS_Y, SensorManager.AXIS_MINUS_X, inputR);
		else if (rotation == Surface.ROTATION_270)// Default display rotation is reverse landscape
			SensorManager.remapCoordinateSystem(inputR, SensorManager.AXIS_MINUS_Y, SensorManager.AXIS_X, inputR);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.btnCalComp:
			calDialog.dismiss();
		}
	}
}
