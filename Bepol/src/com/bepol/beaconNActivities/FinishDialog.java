package com.bepol.beaconNActivities;

import com.bepol.R;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;

/* 도착했을 때 Dialog를 띄어주는 클래스 */

public class FinishDialog extends DialogFragment {
	
	@Override
	public Dialog onCreateDialog(Bundle savedIntanceState){
		AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
		LayoutInflater mLayoutInflater = getActivity().getLayoutInflater();
		mBuilder.setView(mLayoutInflater.inflate(R.layout.activity_finish_dialog,null));
		return mBuilder.create();
	}
	
	@Override
	public void onStop(){
		super.onStop();
	}

}
