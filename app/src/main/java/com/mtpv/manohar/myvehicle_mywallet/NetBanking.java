package com.mtpv.manohar.myvehicle_mywallet;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.apache.http.util.EncodingUtils;

@SuppressWarnings("deprecation")
@SuppressLint("SetJavaScriptEnabled")
public class NetBanking extends Activity implements LocationListener {

	WebView webview;
	@SuppressWarnings("unused")
	private ProgressDialog progress;
	final int PROGRESS_DIALOG = 1;
	
	private static String METHOD_NAME = "MobileRequestProcessor";
	private static String NameSPACE = "http://service.et.mtpv.com";
	@SuppressWarnings("unused")
	private static final String SOAP_ACTION = NameSPACE + METHOD_NAME;
	
	public static String Response_string = "" ;
	
	LocationManager m_locationlistner;
	Location location;

	double latitude = 0.0;
	double longitude = 0.0;
	String provider = "";
	String IMEI = "", sim_no = "";
	
	/* GPS VALUES */
	// flag for GPS status
	boolean isGPSEnabled = false;
	// flag for network status
	boolean isNetworkEnabled = false;
	boolean canGetLocation = false;
	// The minimum distance to change Updates in meters
	private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters
	// The minimum time between updates in milliseconds
	private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute
	
	public static String Registration_No,
	 Imei_No,
	 Sim_No,
	 Mobile_No,
	 latitude_val,
	 longitude_val,
	 email_id,
	 service_cd;
	public static String RegNo,MobileNo, EmailID, Complete_Result, postData;
	//private static final String URL_STRING = "https://www.echallan.org/publicview/MobileRequestProcessor.do?";
	//@SuppressWarnings("unused")
	//private static final String URL_STRING = "125.16.1.70:8080/publicview/MobileRequestProcessor.do";
	public static String service_code = "Qa21WsEd4Fhkj53T7fhg65UyT";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_net_banking);
		
		TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		IMEI = getDeviceID(telephonyManager);
		
		if (telephonyManager.getSimState() != TelephonyManager.SIM_STATE_ABSENT) {
			Sim_No = "" + telephonyManager.getSimSerialNumber();
		} else {
			Sim_No = "";
		}
		
		
		webview = (WebView) findViewById(R.id.webview);
		
		getLocation();
		
		RegNo=getIntent().getStringExtra("RegNo");
		MobileNo=getIntent().getStringExtra("MobileNo");
		EmailID=getIntent().getStringExtra("EmailID");
		

		RegNo = Pendingchallansbyregno.vehicle_no_details.getText().toString().trim() ;
		
		//Log.i("Public_Acitivity.vehicle_no", ""+RegNo);


		if ("1".equals(Pendingchallansbyregno.regNoFLG)) {
			postData = "regn_no="+RegNo+"&license_no=&aadhar_no=&sim_no="+Sim_No+"&mobile_no="+PopupDetails.MobileNo.getText().toString().trim()+"&lattitude="+(""+latitude)+"&longitude="+(""+longitude)+"&imei="+IMEI+"&service_code="+service_code+"&email="+EmailID;
			
			//Log.e("postData AT REGN NO ****************::", ""+postData);
			
		}else if ("1".equals(Pendingchallansbyregno.dlFLG)) {
			postData = "regn_no=&license_no="+RegNo+"&aadhar_no=&sim_no="+Sim_No+"&mobile_no="+PopupDetails.MobileNo.getText().toString().trim()+"&lattitude="+(""+latitude)+"&longitude="+(""+longitude)+"&imei="+IMEI+"&service_code="+service_code+"&email="+EmailID;

			//Log.e("postData ar DL****************::", ""+postData);
			
		}else if ("1".equals(Pendingchallansbyregno.aadhaarFLG)) {
			postData = "regn_no=&license_no=&aadhar_no="+RegNo+"&sim_no="+Sim_No+"&mobile_no="+PopupDetails.MobileNo.getText().toString().trim()+"&lattitude="+(""+latitude)+"&longitude="+(""+longitude)+"&imei="+IMEI+"&service_code="+service_code+"&email="+EmailID;
			
			//Log.e("postData at aadhaar****************::", ""+postData);

		}
		System.out.println("postData :"+postData);
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				new Async_NetBanking().execute();				
			}
		});
		
	}
	
	public class Async_NetBanking extends AsyncTask<Void, Void, String>
	{
		ProgressDialog dialog = new ProgressDialog(getApplicationContext());
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					showDialog(PROGRESS_DIALOG);		
				}
			});
			
		}
		
		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub
			runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
						WebSettings webSettings = webview.getSettings();
						webSettings.setJavaScriptEnabled(true);

						webview.setWebViewClient(new WebViewClient());
						webview.postUrl("http://125.16.1.70:8080/publicview/MobileRequestProcessor.do", EncodingUtils.getBytes(postData, "utf-8"));
						//webview.postUrl("https://www.echallan.org/publicview/MobileRequestProcessor.do", EncodingUtils.getBytes(postData, "utf-8"));
						//webview.postUrl("https://www.echallan.org/publicview/MobileRequestProcessor.do", EncodingUtils.getBytes(postData, "BASE64"));
						
						//webview.postUrl("http://125.16.1.70:8080/publicviewT/MobileRequestProcessor.do", EncodingUtils.getBytes(postData, "utf-8"));
						
						
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
			return null;
		}
		
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					removeDialog(PROGRESS_DIALOG);		
				}
			});
			
			
		}
	}
	

	private void getLocation() {
		try {
			m_locationlistner = (LocationManager) this.getSystemService(LOCATION_SERVICE);

			// getting GPS status
			isGPSEnabled = m_locationlistner.isProviderEnabled(LocationManager.GPS_PROVIDER);

			// getting network status
			isNetworkEnabled = m_locationlistner.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

			if (!isGPSEnabled && !isNetworkEnabled) {
				// no network provider is enabled
				latitude = 0.0;
				longitude = 0.0;
			} else {
				this.canGetLocation = true;
				// First get location from Network Provider
				if (isNetworkEnabled) {
					m_locationlistner.requestLocationUpdates(
							LocationManager.NETWORK_PROVIDER,
							MIN_TIME_BW_UPDATES,
							MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
					Log.d("Network", "Network");
					if (m_locationlistner != null) {
						location = m_locationlistner.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
						if (location != null) {
							latitude = location.getLatitude();
							longitude = location.getLongitude();
						} else {
							latitude = 0.0;
							longitude = 0.0;
						}
					}
				}
				if (isGPSEnabled) {
					if (location == null) {
						m_locationlistner.requestLocationUpdates(
								LocationManager.GPS_PROVIDER,
								MIN_TIME_BW_UPDATES,
								MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
						Log.d("GPS Enabled", "GPS Enabled");
						if (m_locationlistner != null) {
							location = m_locationlistner.getLastKnownLocation(LocationManager.GPS_PROVIDER);
							if (location != null) {
								latitude = location.getLatitude();
								longitude = location.getLongitude();
							} else {
								latitude = 0.0;
								longitude = 0.0;
							}
						}
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	
	String getDeviceID(TelephonyManager phonyManager) {

		String id = phonyManager.getDeviceId();
		if (id == null) {
			id = "not available";
		}

		int phoneType = phonyManager.getPhoneType();
		switch (phoneType) {
		case TelephonyManager.PHONE_TYPE_NONE:
			return id;

		case TelephonyManager.PHONE_TYPE_GSM:
			return id;

		case TelephonyManager.PHONE_TYPE_CDMA:
			return id;

		default:
			return "UNKNOWN:ID=" + id;
		}

	}
	
	
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
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(NetBanking.this);
			  alertDialogBuilder.setTitle("E-Challan Application");
			  alertDialogBuilder.setIcon(R.drawable.exit_logo);
		      alertDialogBuilder.setMessage("Are you sure,You want to go Back...!");
		      
		      alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
		         @Override
		         public void onClick(DialogInterface arg0, int arg1) {
		        	 Intent back = new Intent(getApplicationContext(),Pendingchallansbyregno.class);
		        	 startActivity(back);
		        	 
		         }
		      });
		      alertDialogBuilder.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
		         @Override
		         public void onClick(DialogInterface dialog, int which) {
		        	
		         }
		      });
		      AlertDialog alertDialog = alertDialogBuilder.create();
		      alertDialog.show();
		
	    }

	@Override
	public void onLocationChanged(Location location) {
		if (location != null) {
			latitude = (float) location.getLatitude();
			longitude = (float) location.getLongitude();
			// speed = location.getSpeed();
		} else {
			latitude = 0.0;
			longitude = 0.0;
		}
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}
	
}
