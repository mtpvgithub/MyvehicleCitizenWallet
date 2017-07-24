package com.mtpv.manohar.myvehicle_mywallet;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class Logout extends Activity {

	Button cancel, ok ;
	TextView text ;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_logout);
		this.setFinishOnTouchOutside(false);
		
		cancel = (Button)findViewById(R.id.cancel_logout);
		ok = (Button)findViewById(R.id.ok_logout);
		
		
		text = (TextView)findViewById(R.id.text);
		
		//LoginActivity.alert_string.equals("loginback");
		
	//	Log.i("LoginActivity.alert_string  ::::", ""+LoginActivity.alert_string);
		
		if (Vehicle_Wallet_Login.alert_string.equals("loginback")) {
			text.setText("Are you sure, \nYou want to Leave the Pending Challans!!");
			cancel.setText("No");
			ok.setText("Yes");

		}else if (Vehicle_Wallet_Login.alert_string.equals("exceed")) {
			text.setText("\n You have Exceeded Number of Visits \n on that Vehicle please try After 24hrs...! \n");
			cancel.setText("Cancel");
			ok.setText("Ok");
		}
		
		cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		
		ok.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				if (Vehicle_Wallet_Login.alert_string.equals("loginback")) {
//					Intent intent = new Intent(Intent.ACTION_MAIN);
//		        	intent.addCategory(Intent.CATEGORY_HOME);
//		        	finish();
//		        	startActivity(intent);
//
//				}else if (Vehicle_Wallet_Login.alert_string.equals("exceed")) {
//		        	finish();
//
//		        	Public_Acitivity.vehicle_no.setText("");
//					Public_Acitivity.table_layout.removeAllViews();
//					Public_Acitivity.scroll.setVisibility(View.GONE);
//				}


				Intent i =new Intent(Logout.this,DashboardActivity.class);
				i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(i);
				finish();
				
			}
		});
	}
	
}
