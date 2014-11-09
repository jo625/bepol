package com.bepol.beaconNActivities;

import java.util.ArrayList;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.webkit.WebView.FindListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
/* 한글 주석 테스트 */
/* ListView's Adapter */
public class BeaconInfo extends BaseAdapter {
	private Context mContext;
	private ArrayList<BeaconSignal> mSignals;
	private BeaconSignal curBeacon = null;
	private MainActivity mainactivity;
	/* Image Animation */
	public float curX;
	public float curY;
	public float nextX;
	public float nextY;
	public ImageView img;
	public TranslateAnimation animation;
	TextView rotateText;
	/* Distance */
	float distance;
	TextView distanceText;
	
	/* 네비 */
	public static String Start;
	
	/* 기기 구분 */
	static int threshold;
	
	/* 변수 초기화 */
	public BeaconInfo(Context c, View v, View t, View t2){
		mContext = c;
		mSignals = new ArrayList<BeaconSignal>();
		img = (ImageView)v;
		distanceText = (TextView)t;
		rotateText = (TextView)t2;
		curX = img.getX();
        curY = img.getY();
        nextX = curX;
        nextY = curY;
        String model = Build.MODEL;
        if(model.equals("SHV-E300S"))	//상희
        	threshold = 40;
        else if(model.equals("SM-G900K"))	//옥영
        	threshold = 85;
        else if(model.endsWith("LG-F260S"))	//한
        	threshold = 64;
        else
        	threshold = 56;
	}
	
	/* BeconSignal 객체 추가 */
	public void add(BeaconSignal b){
		mSignals.add(b);
		this.notifyDataSetChanged();
	}
	
	/* 추가된 Signal 개수 반환 */
	@Override
	public int getCount() {
		if(mSignals == null)
			return 0;
		return mSignals.size();
	}
	
	/* 출발지 반환  */
	public String getStart(){
		return Start;
	}

	/* Item 반환 */
	@Override
	public Object getItem(int position) {
		if(mSignals == null)
			return null;
		return mSignals.get(position);
	}

	/* ID 반환 */
	@Override
	public long getItemId(int position) {
		return position;
	}
	
	/* view 반환 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		TextView textView;
		int i;
        // General ListView optimization code.
        if (convertView == null) {
            textView = new TextView(mContext);
        } else {
            textView = (TextView)convertView;
        }

        BeaconSignal cur = mSignals.get(position);
        for(i=0 ; i<mSignals.size() ; i++){
        	if(i==position)
        		continue;
        	if((cur.getRssi()<mSignals.get(i).getRssi()))  
        		break;
        }
        /*if(cur.getMajor().equals("0")&&cur.getMinor().equals("0")){
        	cur.setName("White 1 !");
        }
        else if(cur.getMajor().equals("1")&&cur.getMinor().equals("0")){
        	cur.setName("Black 1 !");
        }
        else if(cur.getMajor().equals("2")&&cur.getMinor().equals("0")){
        	cur.setName("Gray 1 !");
        }
        else if(cur.getMajor().equals("0")&&cur.getMinor().equals("1")){
        	cur.setName("White 2 !");
        }
        else if(cur.getMajor().equals("1")&&cur.getMinor().equals("1")){
        	cur.setName("Black 2 !");
        }
        else if(cur.getMajor().equals("2")&&cur.getMinor().equals("1")){
        	cur.setName("Gray 2 !");
        }*/
        //if rssi is bigger than -65 and different from previous beacon, must initialize the distance
        //한백이 64
        //상희 56
        if(i==mSignals.size()&&cur.getRssi()>-threshold){
        	if(cur.getRssi()>-threshold){
	        	if(!cur.equals(curBeacon)||curBeacon==null){
		        	if(curBeacon==null){
		        		curBeacon = cur;
		        		distanceText.setText("chngd");	//changed
		        		//rotateText.setText(cur.getMajor());
		        	}
	        	}
	        	textView.setBackgroundColor(Color.MAGENTA);
	        	moveImage(cur.getMajor(), cur.getMinor());
        	}else
        		curBeacon = null;
        }else{
        	textView.setBackgroundColor(Color.WHITE);
        }
        textView.setText("Address : "+cur.getAddress()+"\n"+"RSSI : "+cur.getRssi()+
        		"\nMajor : "+cur.getMajor()+"\nMinor : "+cur.getMinor()+"\nName : "+cur.getName());
        return textView;
	}

	public boolean hasDeviceWithAddr(String addr){
		for(BeaconSignal s : mSignals) {
			if (s.getAddress().equals(addr))
				return true;
		}
		return false;
	}
	
	public void updateRssiForDevice(String addr, int rssi){
		for(BeaconSignal s : mSignals) {
			if (s.getAddress().equals(addr)){
				s.setRssi(rssi);
				this.notifyDataSetChanged();
			}
		}
	}
	
	public void updateMajorForDevice(String addr, String major){
		for(BeaconSignal s : mSignals) {
			if (s.getAddress().equals(addr)){
				s.setMajor(major);
				this.notifyDataSetChanged();
			}
		}
	}
	
	public void updateMinorForDevice(String addr, String minor){
		for(BeaconSignal s : mSignals) {
			if (s.getAddress().equals(addr)){
				s.setMinor(minor);
				this.notifyDataSetChanged();
			}
		}
	}
	
	public void moveImage(String major, String minor){
		if(animation==null||animation.hasEnded()){
			if(major.equals("0")){	
	        	animation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT,curX,Animation.RELATIVE_TO_PARENT,0.589f,
	        					Animation.RELATIVE_TO_PARENT,curY,Animation.RELATIVE_TO_PARENT,0.728f);
	        	curX = 0.589f;
	        	curY = 0.728f;
	        	if(MyView.getDest().equals("4147")){
	        		mainactivity.alertDialog.show();
	        		MyView.setDest("");
	        	}
	        	
	        }
	        else if(major.equals("1")){	
	        	animation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT,curX,Animation.RELATIVE_TO_PARENT,0.364f,
	        					Animation.RELATIVE_TO_PARENT,curY,Animation.RELATIVE_TO_PARENT,0.728f);
	        	curX = 0.364f;
	        	curY = 0.728f;
	        	if(MyView.getDest().equals("4147")){
	        		mainactivity.alertDialog.show();
	        		MyView.setDest("");
	        	}
	        	
	        }
	        else if(major.equals("2")){	
	        	animation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT,curX,Animation.RELATIVE_TO_PARENT,0.140f,
	        					Animation.RELATIVE_TO_PARENT,curY,Animation.RELATIVE_TO_PARENT,0.728f);
	        	curX = 0.140f;
	        	curY = 0.728f;
	        	if(MyView.getDest().equals("4147")){
	        		mainactivity.alertDialog.show();
	        		MyView.setDest("");
	        	}
	        	
	        }
	        else if(major.equals("3")){	
	        	animation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT,curX,Animation.RELATIVE_TO_PARENT,0.140f,
	        					Animation.RELATIVE_TO_PARENT,curY,Animation.RELATIVE_TO_PARENT,0.412f);
	        	curX = 0.140f;
	        	curY = 0.412f;
	        	if(MyView.getDest().equals("4147")){
	        		mainactivity.alertDialog.show();
	        		MyView.setDest("");
	        	}
	        	
	        }
	        else if(major.equals("4")){	
	        	animation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT,curX,Animation.RELATIVE_TO_PARENT,0.364f,
	        					Animation.RELATIVE_TO_PARENT,curY,Animation.RELATIVE_TO_PARENT,0.412f);
	        	curX = 0.364f;
	        	curY = 0.412f;
	        	if(MyView.getDest().equals("4147")){
	        		mainactivity.alertDialog.show();
	        		MyView.setDest("");
	        	}
	        	
	        }
	        else if(major.equals("5")){	
	        	animation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT,curX,Animation.RELATIVE_TO_PARENT,0.140f,
	        					Animation.RELATIVE_TO_PARENT,curY,Animation.RELATIVE_TO_PARENT,0.188f);
	        	curX = 0.140f;
	        	curY = 0.188f;
	        	if(MyView.getDest().equals("4147")){
	        		mainactivity.alertDialog.show();
	        		MyView.setDest("");
	        	}
	        }
	        else if(major.equals("6")){	
	        	animation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT,curX,Animation.RELATIVE_TO_PARENT,0.249f,
	        					Animation.RELATIVE_TO_PARENT,curY,Animation.RELATIVE_TO_PARENT,0.094f);
	        	curX = 0.249f;
	        	curY = 0.094f;
	        	if(MyView.getDest().equals("4147")){
	        		mainactivity.alertDialog.show();
	        		MyView.setDest("");
	        	}
	        	
	        }
	        else if(major.equals("7")){	
	        	animation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT,curX,Animation.RELATIVE_TO_PARENT,0.364f,
	        					Animation.RELATIVE_TO_PARENT,curY,Animation.RELATIVE_TO_PARENT,0.094f);
	        	curX = 0.364f;
	        	curY = 0.094f;
	        	if(MyView.getDest().equals("4147")){
	        		mainactivity.alertDialog.show();
	        		MyView.setDest("");
	        	}
	        	
	        }
	        else if(major.equals("8")){	
	        	animation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT,curX,Animation.RELATIVE_TO_PARENT,0.612f,
	        					Animation.RELATIVE_TO_PARENT,curY,Animation.RELATIVE_TO_PARENT,0.094f);
	        	curX = 0.612f;
	        	curY = 0.094f;
	        	if(MyView.getDest().equals("4147")){
	        		mainactivity.alertDialog.show();
	        		MyView.setDest("");
	        	}
	        	
	        }
	        else if(major.equals("9")){	
	        	animation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT,curX,Animation.RELATIVE_TO_PARENT,0.827f,
	        					Animation.RELATIVE_TO_PARENT,curY,Animation.RELATIVE_TO_PARENT,0.094f);
	        	curX = 0.827f;
	        	curY = 0.094f;
	        	if(MyView.getDest().equals("4147")){
	        		mainactivity.alertDialog.show();
	        		MyView.setDest("");
	        	}
	        	
	        }
	        else if(major.equals("10")){	
	        	animation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT,curX,Animation.RELATIVE_TO_PARENT,0.818f,
	        					Animation.RELATIVE_TO_PARENT,curY,Animation.RELATIVE_TO_PARENT,0.728f);
	        	curX = 0.818f;
	        	curY = 0.728f;
	        	if(MyView.getDest().equals("4147")){
	        		mainactivity.alertDialog.show();
	        		MyView.setDest("");
	        	}
	        	
	        }
	        else{
	        	animation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT,curX,Animation.RELATIVE_TO_PARENT,0.0f,
    					Animation.RELATIVE_TO_PARENT,curY,Animation.RELATIVE_TO_PARENT,0.0f);
		    	curX = 0.0f;
		    	curY = 0.0f;
		    	if(MyView.getDest().equals("4147")){
		    		mainactivity.alertDialog.show();
		    		MyView.setDest("");
		    	}
	        }
	        animation.setDuration(500);
	        animation.setFillAfter(true);
	        img.startAnimation(animation);
		}
	}
	
	
	public float getX()
	{
		return curX;
	}
	public float getY()
	{
		return curY;
	}
	
	/* icon's rotate angle now */
	public void moveForward(float angle){
		angle = (int)angle;
		if(animation==null||animation.hasEnded()){
			nextX = curX;
			nextY = curY;
			if((angle>=315||angle<45)){	//front
				nextY = curY - 0.016f;
			}else if(angle>=45&&angle<135){
				nextX = curX + 0.026f;
			}else if(angle>=135&&angle<225){
				nextY = curY +0.016f;
			}else if(angle>=225&&angle<315){
				nextX = curX - 0.026f;
			}
			//블록
			if((nextX>=0.135f&&nextX<=0.95f)&&(nextY>=0.079f&&nextY<=0.8f)){ // 범위 내
				if(!((nextX>=0.41f&&nextX<=0.95f)&&(nextY>=0.15f&&nextY<=0.65f)	// NOT 들어오면 안되는 곳
						||((nextX>=0.2f&&nextX<=0.3f)&&((nextY>=0.45f&&nextY<=0.68f)||(nextY>=0.19f&&nextY<=0.38f))))){	
					animation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT,curX,Animation.RELATIVE_TO_PARENT,nextX,
	    					Animation.RELATIVE_TO_PARENT,curY,Animation.RELATIVE_TO_PARENT,nextY);
					animation.setDuration(300);
			        animation.setFillAfter(true);
			        img.startAnimation(animation);
			        curX = nextX;
			        curY = nextY;
				}
			}
		}
	}
}
