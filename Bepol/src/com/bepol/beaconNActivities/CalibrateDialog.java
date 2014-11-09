package com.bepol.beaconNActivities;

import com.bepol.R;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/* 칼리브레이트 다이얼로그를 띄우기 위한 클래스 */

public class CalibrateDialog extends DialogFragment{
	@Override
	public Dialog onCreateDialog(Bundle savedIntanceState){
		AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
		LayoutInflater mLayoutInflater = getActivity().getLayoutInflater();
		mBuilder.setView(mLayoutInflater.inflate(R.layout.activity_calibrate_dialog,null));
		return mBuilder.create();
	}
	
	@Override
	public void onStop(){
		super.onStop();
	}
}
