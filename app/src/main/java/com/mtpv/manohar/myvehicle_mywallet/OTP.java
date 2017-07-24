package com.mtpv.manohar.myvehicle_mywallet;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.mtpv.manohar.myvehicle_mywallet.Services.ServiceHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


@SuppressLint("DefaultLocale")
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class OTP extends Activity {
	
	final int PROGRESS_DIALOG = 0;
	final int OTP_CNFRMTN_DIALOG = 7;
	
	EditText otp_input ;
	Button otp_cancel, ok_dialog ;
	
	public static String otp_number ="", reg_No, email_id, Mobile_No, OTP_date, pass_word, OTP_No, Verify_status , close_Decision = "" , UserName="";

	@SuppressLint("InlinedApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_otp);
		this.setFinishOnTouchOutside(false);
		
		otp_input = (EditText)findViewById(R.id.otp_input);
		ok_dialog = (Button)findViewById(R.id.ok_dialog);
		otp_cancel = (Button)findViewById(R.id.cancel_dialog);
		
		 reg_No = RegisterActivity.vehicleNo.getText().toString() ;
		 email_id = RegisterActivity.email.getText().toString();
		 pass_word = RegisterActivity.password.getText().toString();
		 Mobile_No = RegisterActivity.mobile.getText().toString();
		 OTP_date = RegisterActivity.present_date_toSend;
		 OTP_No = RegisterActivity.otpValue;
		 UserName = RegisterActivity.name.getText().toString().trim();
		 Verify_status = "";
		 
		 Log.i("reg_No ::::", ""+reg_No);
		 Log.i("Mobile_No", ""+Mobile_No);
		 Log.i("OTP_date", ""+OTP_date);
		 Log.i("OTP_No", ""+OTP_No);
		 Log.i("Verify_status", ""+Verify_status);
		
		 otp_cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				TextView title = new TextView(OTP.this);
				title.setText("My Vehicle My Wallet");
				title.setBackgroundColor(Color.parseColor("#0E244D"));
				title.setGravity(Gravity.CENTER);
				title.setTextColor(Color.WHITE);
				title.setTextSize(10);
				title.setTypeface(title.getTypeface(), Typeface.BOLD);
				//title.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dialog_logo, 0, R.drawable.dialog_logo, 0);
				title.setPadding(20, 0, 20, 0);//l t r b
				title.setHeight(150);
				
				String otp_message = "Are you sure, Without Verifying OTP you can't Register ???" ;
				
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(OTP.this, AlertDialog.THEME_HOLO_LIGHT);
				alertDialogBuilder.setCustomTitle(title);
				//alertDialogBuilder.setIcon(R.drawable.dialog_logo);
				alertDialogBuilder.setMessage(otp_message);
				alertDialogBuilder.setCancelable(false);
				alertDialogBuilder.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								 close_Decision = "Y" ;
								 finish();
							}
						});

				alertDialogBuilder.setNegativeButton("No",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								close_Decision = "N" ;
							}
						});
				
			        AlertDialog alertDialog = alertDialogBuilder.create();
			        alertDialog.show();
			      
			        alertDialog.getWindow().getAttributes();

			        TextView textView = (TextView) alertDialog.findViewById(android.R.id.message);
			        title.setTextSize(getResources().getDimension(R.dimen.text_size));
			        textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
			        textView.setGravity(Gravity.CENTER);
			        
			        Button btn1 = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
			        title.setTextSize(getResources().getDimension(R.dimen.text_size));
			        btn1.setTextColor(Color.WHITE);
			        btn1.setTypeface(btn1.getTypeface(), Typeface.BOLD);
			        btn1.setBackgroundResource(R.drawable.buttonback);
		      
			        Button btn2 = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
			        title.setTextSize(getResources().getDimension(R.dimen.text_size));
			        btn2.setTextColor(Color.WHITE);
			        btn2.setTypeface(btn2.getTypeface(), Typeface.BOLD);
                    btn2.setBackgroundResource(R.drawable.buttonback);
			
			        
			        if (close_Decision.equals("N")) {
						
					}else if(close_Decision.equals("Y")){
						finish();
					}
			}
		});
		 
		ok_dialog.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (otp_input.getText().toString().trim().equals(OTP_No)) {
					Verify_status = "Y";
					if (isOnline()) {
						//Log.i("***OTP CONFIRMATION STATUS", ""	+ Verify_status);
						//SpotChallan.otp_status = "verify";
						//new Async_otpverify().execute();
						new Async_Register().execute();
					} else {
						showToast("Validation","Please check your network connection!");
					}
				}else{
					Verify_status = "N";
					otp_input.setError(Html.fromHtml("<font color='red'>Please Enter Valid OTP!</font>"));
					otp_input.requestFocus();
					//showToast("Entered Wrong OTP");
					otp_input.setText("");
				}
			}
		});
		
	}
	
	public Boolean isOnline() {
		ConnectivityManager conManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo nwInfo = conManager.getActiveNetworkInfo();
		return nwInfo != null;
	}
	
	class Async_otpverify extends AsyncTask<Void, Void, String>{
        ProgressDialog dialog;

		@SuppressWarnings("deprecation")
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		//	showDialog(PROGRESS_DIALOG);
            dialog = new ProgressDialog(OTP.this);
            dialog.setTitle("Verifying  Details");
            dialog.setMessage("Please wait....");
            dialog.setCancelable(false);
            dialog.show();
		}
		
		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub
			otp_number = otp_input.getText().toString().trim();
		//	ServiceHelper.verifyOTP(reg_No , Mobile_No,OTP_date, ""+ OTP_No, Verify_status);//33
			
			return null;
		}
		
		@SuppressWarnings("deprecation")
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			//removeDialog(PROGRESS_DIALOG);
            dialog.dismiss();
		//	Log.i("ServiceHelper.otp_verify_resp :::::", ""+ServiceHelper.otp_verify_resp);
			if (ServiceHelper.otp_verify_resp.equals("0")) {
				showToast("Server Response","Entered Wrong OTP");
				otp_input.setText("");
			}else{
				showToast("Server Response","OTP Verified Successfully");
				new Async_Register().execute();
			}
			
		}
	}
	
	class Async_Register extends AsyncTask<Void, Void, String>
	{
        ProgressDialog dialog;

        @SuppressWarnings("deprecation")
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		//	showDialog(PROGRESS_DIALOG);
            dialog = new ProgressDialog(OTP.this);
            dialog.setTitle("Registering  Details");
            dialog.setMessage("Please wait....");
            dialog.setCancelable(false);
            dialog.show();
		}
		
		@Override
		protected String doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			/*public String registerUser(String userName, String regn_no,String mobileNo,
		            String emailId,String password,String imeie,String key,
		            String date, String otp,String verify_status);*/
			ServiceHelper.registration(""+UserName, ""+reg_No, ""+ Mobile_No, ""+email_id, ""+pass_word, ""+RegisterActivity.IMEI, "", OTP_date, OTP_No,Verify_status);
			return null;
		}

		@SuppressWarnings("deprecation")
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
		//	removeDialog(PROGRESS_DIALOG);
            dialog.dismiss();

			try {
				String regresponse=null;
				JSONObject jsonObj = new JSONObject(ServiceHelper.Opdata_Chalana);
				// Getting JSON Array node
				JSONArray jsonArray = jsonObj.getJSONArray("Registration Details");
				// looping through All Witness
				for (int i = 0, size = jsonArray.length(); i < size; i++){
					JSONObject c = jsonArray.getJSONObject(i);
					//witness_id_code
					regresponse=c.getString("Registration Response");
				}


				if(regresponse.equalsIgnoreCase("Registration Successfull"))
				{

					showToast("Server Response",regresponse);

					Intent i=new Intent(OTP.this,Vehicle_Wallet_Login.class);
					startActivity(i);
					finish();

				}
				else
				{
					showToast("Server Response",regresponse);

				}

			}catch (JSONException e)
			{
				e.printStackTrace();
			}
		}
	}

	void showToast(String title ,String Message)
	{
		LayoutInflater inflator=getLayoutInflater();
		View toastlayout=inflator.inflate(R.layout.my_toast,(ViewGroup)findViewById(R.id.toast_root_view));
		TextView toast_header=(TextView)toastlayout.findViewById(R.id.toast_header);
		toast_header.setText(title);
		TextView toast_body=(TextView)toastlayout.findViewById(R.id.toast_body);
		toast_body.setText(Message);
		Toast toast=new Toast(getApplicationContext());
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.setDuration(toast.LENGTH_LONG);
		toast.setView(toastlayout);
		toast.show();
	}


	@SuppressWarnings("deprecation")
	@Override
	protected Dialog onCreateDialog(int id) {
		// TODO Auto-generated method stub
		switch (id) {
		case PROGRESS_DIALOG:
			ProgressDialog pd = ProgressDialog.show(this, "", "",	true);
			pd.setContentView(R.layout.custom_progress_dialog);
			pd.setCancelable(false); 
			
			return pd;

		default:
			break;
		}
		return super.onCreateDialog(id);
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		showToast("Validation","Please Click on Cancel Button to go Back ..!");
	}
}
