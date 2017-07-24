package com.mtpv.manohar.myvehicle_mywallet;

import java.util.regex.Pattern;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class PopupDetails extends Activity {

	public static EditText MobileNo, EmailID ;
	public static Button submit, cancel ;
	  private boolean isValidEmaillId(String email){

		    return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
		              + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
		              + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
		              + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
		              + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
		              + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches();
	}
	  
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_popup_details);
		 this.setFinishOnTouchOutside(false);
		MobileNo = (EditText)findViewById(R.id.et_mobileNo);
		EmailID = (EditText)findViewById(R.id.et_emailid);
		
		MobileNo.setText("");
		EmailID.setText("");
		
		submit = (Button) findViewById(R.id.submit);
		cancel = (Button) findViewById(R.id.cancel);
		
		/*MobileNo.setText("9494345434");
    	EmailID.setText("kirankumar.mtpv@gmail.com");*/
		
		submit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (MobileNo.getText().toString().trim().equals("")) {
					MobileNo.setError(Html.fromHtml("<font color='red'>Please Enter Mobile Number!</font>"));
					MobileNo.requestFocus();
				}else if (MobileNo.getText().toString().trim().length()>0 && MobileNo.getText().toString().trim().length()!=10 && !validateMobileNo(MobileNo.getText().toString().trim())) {
					MobileNo.setError(Html.fromHtml("<font color='red'>Please Enter Valid Mobile Number!</font>"));
					MobileNo.requestFocus();
				}else if (EmailID.getText().toString().trim().equals("")) {
					EmailID.setError(Html.fromHtml("<font color='red'>Please Enter Email Id!</font>"));
					EmailID.requestFocus();
				}else if (EmailID.getText().toString().trim()!=null && EmailID.getText().toString().trim().length()>1
						&&!isValidEmaillId(EmailID.getText().toString().trim())) {
					EmailID.setError(Html.fromHtml("<font color='red'>Please Enter Valid Email Id!</font>"));
					EmailID.requestFocus();
				}
				else{
					
					LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

			        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
			        	Intent browserIntent = new Intent(PopupDetails.this, NetBanking.class);
						finish();					
						browserIntent.putExtra("RegNo", ""+Pendingchallansbyregno.vehicle_no.getText().toString().trim());
						browserIntent.putExtra("MobileNo", ""+MobileNo.getText().toString().trim());
						browserIntent.putExtra("EmailID", ""+EmailID.getText().toString().trim());
						startActivity(browserIntent);
		            	overridePendingTransition(R.anim.fade_enter,R.anim.fade_leave);
			            
			        }else{
			            showGPSDisabledAlertToUser();
			        }
					}
				}
			});
		
		cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}
	
	
	private void showGPSDisabledAlertToUser(){
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(PopupDetails.this);
	      
        alertDialog.setTitle("GPS is settings");
  
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");
  
        alertDialog.setPositiveButton("Enable GPS", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });
  
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
            }
        });
  
        alertDialog.show();
    }
	
	/***********************Mobile Number Validation Method***************************/
	protected boolean validateMobileNo(String mobileNo) {
		boolean flg=false;
		try {
			if(mobileNo!=null &&  mobileNo.trim().length()==10
				&&( "7".equals(mobileNo.trim().substring(0,1)) 
				|| "8".equals(mobileNo.trim().substring(0,1))
				|| "9".equals(mobileNo.trim().substring(0,1)))){
				flg=true;
			}else if(mobileNo!=null &&  mobileNo.trim().length()==11 && "0".equals(mobileNo.trim().substring(0,1)) ){
				flg=true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			flg=false;
			
		}
		return flg;
	}
	/***********************Mobile Number Validation Method Ends***************************/
}
