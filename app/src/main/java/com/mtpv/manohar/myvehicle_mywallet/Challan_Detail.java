
package com.mtpv.manohar.myvehicle_mywallet;

import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class Challan_Detail extends Activity implements OnInitListener {
	
	TextView owner_name, vehicle_no_details, echallan_no, date, time, place_violation,
			ps_limits, name_of_violation, fine_amnt, charges, total_amnt, ch_vehicle_txt ;
	
	Button netbanking_btn;
	ImageView image ;
	
	TableLayout challan_details_table;
	
	LinearLayout owner_layout ;
	
	TableRow tr;
	
	 private LinearLayout mlLayoutRequestError = null;
	    private Handler mhErrorLayoutHide = null;

	private boolean mbErrorOccured = false;
	private boolean mbReloadPressed = false;
	
	final int PROGRESS_DIALOG = 1;
	WebviewLoader webviewloader;
	WebView webView_image_spotchallan_xml;
	
	LinearLayout webView_layout ;
	
	String ImageData = ""  ;
	private TextToSpeech tts;
	String text ;
	LinearLayout layout_DL_Aadhaar ;
	TextView DL_Aadhaar_label_txt, DL_Aadhaar_text , unit_name;
	
    
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@SuppressWarnings({ "unused" })
	@SuppressLint({ "SetJavaScriptEnabled", "SimpleDateFormat" })
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_challan__detail);
		//tts = new TextToSpeech(this, this);
		
		owner_layout = (LinearLayout)findViewById(R.id.owner_layout); 
		
		owner_layout.setVisibility(View.GONE);
		
		owner_name = (TextView)findViewById(R.id.owner_name);
		vehicle_no_details = (TextView)findViewById(R.id.vehicle_no_details);
		echallan_no = (TextView)findViewById(R.id.echallan_no);
		date = (TextView)findViewById(R.id.date);
		time = (TextView)findViewById(R.id.time);
		place_violation = (TextView)findViewById(R.id.place_violation);
		ps_limits = (TextView)findViewById(R.id.ps_limits);
		name_of_violation = (TextView)findViewById(R.id.name_of_violation);
		fine_amnt = (TextView)findViewById(R.id.fine_amnt);
		charges = (TextView)findViewById(R.id.charges);
		total_amnt = (TextView)findViewById(R.id.total_amnt);
		
		layout_DL_Aadhaar = (LinearLayout)findViewById(R.id.layout_DL_Aadhaar);
		layout_DL_Aadhaar.setVisibility(View.VISIBLE);
		unit_name = (TextView)findViewById(R.id.unit_name);
		DL_Aadhaar_text = (TextView)findViewById(R.id.DL_Aadhaar_text);
		DL_Aadhaar_label_txt = (TextView)findViewById(R.id.DL_Aadhaar_label_txt);
		
		ch_vehicle_txt = (TextView)findViewById(R.id.ch_vehicle_txt);
		
		webView_image_spotchallan_xml = (WebView)findViewById(R.id.webView_image_spotchallan_xml);
		
		if (Pendindchallans_withTab.Tabflag.equalsIgnoreCase("RegNo")) {
			DL_Aadhaar_label_txt.setText("Vehicle No");
			layout_DL_Aadhaar.setVisibility(View.GONE);
			owner_layout.setVisibility(View.VISIBLE);
		}else if (Pendindchallans_withTab.Tabflag.equalsIgnoreCase("DLNO")) {
			DL_Aadhaar_label_txt.setText("Driving Licence No");
			layout_DL_Aadhaar.setVisibility(View.VISIBLE);
			owner_layout.setVisibility(View.GONE);
		}else if (Pendindchallans_withTab.Tabflag.equalsIgnoreCase("Aadhar")) {
			DL_Aadhaar_label_txt.setText("Aadhaar No");
			layout_DL_Aadhaar.setVisibility(View.VISIBLE);
			owner_layout.setVisibility(View.GONE);
		}
		
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		 
	    String OwnerName = sharedPreferences.getString("OWNER_NAME", "");
	    String VehicleNo = sharedPreferences.getString("VEHICLE_NO", "");
	    String EticketNo = sharedPreferences.getString("ETICKET_NO", "");
	    String DateOfChallan = sharedPreferences.getString("OFFENCE_DATE", "");
	    String TimeOfChallan = sharedPreferences.getString("OFFENCE_TIME", "");
	    String PointName = sharedPreferences.getString("POINT_NAME", "");
	    String PSLimits = sharedPreferences.getString("PS_NAME", "");
	    String ViolationName = sharedPreferences.getString("VIOLATION", "");
	    String ViolationAmount = sharedPreferences.getString("VIOLATION_AMT", "");
	    String ActualFine = sharedPreferences.getString("ACTUAL_FINE_AMT", "");
	    String UserCharges = sharedPreferences.getString("USER_CHARGES", "");
	    String CmdAmount = sharedPreferences.getString("CMD_FINE_AMT", "");
	    String Lattitude = sharedPreferences.getString("LATTITUDE", "");
	    String Longitude = sharedPreferences.getString("LONGITUDE", "");
	    String regNo = sharedPreferences.getString("REGISTRATION_NO", "");
	    String UnitName = sharedPreferences.getString("UNIT_NAME", "");
	    

	    
	    ImageData = sharedPreferences.getString("IMAGE_URL", "");
	    
	    SimpleDateFormat parse=new SimpleDateFormat("yyyy-mm-dd");
		SimpleDateFormat format=new SimpleDateFormat("dd-mm-yyyy");
		String date_format = null ;
		try {
			 date_format = format.format(parse.parse(DateOfChallan));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		text = "Your Vehicle Number "+VehicleNo +" have been Challaned for "+ViolationName+" Violation On "+date_format;
		//speakOut();

	    owner_name.setText(""+OwnerName);
		vehicle_no_details.setText(""+regNo);





		if (Pendindchallans_withTab.Tabflag.equalsIgnoreCase("RegNo")) {
			DL_Aadhaar_text.setText(""+Pendingchallansbyregno.vehicle_no_details.getText().toString().trim());
		}else if (Pendindchallans_withTab.Tabflag.equalsIgnoreCase("DLNO")) {
			DL_Aadhaar_text.setText(""+PendingChallansbyDlno.vehicle_no_details.getText().toString().trim());

		}else if (Pendindchallans_withTab.Tabflag.equalsIgnoreCase("Aadhar")) {
			DL_Aadhaar_text.setText(""+PendingChallansbyAadhar.vehicle_no_details.getText().toString().trim());
		}



		
		echallan_no.setText(""+EticketNo);
		date.setText(""+date_format);
		time.setText(""+TimeOfChallan);
		place_violation.setText(""+PointName);
		ps_limits.setText(""+PSLimits);
		name_of_violation.setText(""+ViolationName);
		fine_amnt.setText(""+ActualFine);
		charges.setText(""+UserCharges);
		total_amnt.setText(""+(Integer.parseInt(ActualFine)+Integer.parseInt(UserCharges))); 
		unit_name.setText(""+UnitName);
		
		webView_image_spotchallan_xml.setWebViewClient(new MyWebViewClient());
		
		new LoadImage().execute();
		
		netbanking_btn = (Button)findViewById(R.id.netbanking_btn);
		
		netbanking_btn.setVisibility(View.GONE);
		
		netbanking_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent browserIntent = new Intent(Challan_Detail.this, PopupDetails.class);
				startActivity(browserIntent);		
            	overridePendingTransition(R.anim.fade_enter,R.anim.fade_leave);
			}
		});
	}
	
	
	public static boolean exists(String URLName){
	    try {
	      HttpURLConnection.setFollowRedirects(false);
	      HttpURLConnection con = (HttpURLConnection) new URL(URLName).openConnection();
	      con.setRequestMethod("HEAD");
	      
	      return (con.getResponseCode() == HttpURLConnection.HTTP_OK);
	    }
	    catch (Exception e) {
	       e.printStackTrace();
	       return false;
	    }
	  }
	
	@SuppressLint("SetJavaScriptEnabled")
	public class LoadImage extends AsyncTask<Void, Void, String> {

		@SuppressWarnings("deprecation")
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showDialog(PROGRESS_DIALOG);
		}
		
		@Override
		protected String doInBackground(Void... arg0) {
			/*********************Display Image*********************/
			if (ImageData != null ) {
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						if (ImageData.length()==0) {
							webView_image_spotchallan_xml.setVisibility(View.GONE);
						}else {
							webView_image_spotchallan_xml.setVisibility(View.VISIBLE);	
						}
						
						webviewloader = new WebviewLoader();
						webView_image_spotchallan_xml.setBackgroundColor(0x00000000);
						webView_image_spotchallan_xml.setHorizontalScrollBarEnabled(true);
						webView_image_spotchallan_xml.setVerticalScrollBarEnabled(true);
						WebSettings webSettings = webView_image_spotchallan_xml.getSettings();
						webView_image_spotchallan_xml.setInitialScale(50);
						webSettings.setJavaScriptEnabled(true);
						webView_image_spotchallan_xml.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
						
						webviewloader.DisplayImage(""+ImageData, webView_image_spotchallan_xml);
						
						webView_image_spotchallan_xml.setOnTouchListener(new OnTouchListener() {
							
							public final static int FINGER_RELEASED = 0;
					        public final static int FINGER_TOUCHED = 1;
					        public final static int FINGER_DRAGGING = 2;
					        public final static int FINGER_UNDEFINED = 3;

						    private int fingerState = FINGER_RELEASED;
						        
							@SuppressLint("ClickableViewAccessibility")
							@Override
							public boolean onTouch(View v, MotionEvent event) {
								 switch (event.getAction()) {

					                case MotionEvent.ACTION_DOWN:
					                    if (fingerState == FINGER_RELEASED) fingerState = FINGER_TOUCHED;
					                    else fingerState = FINGER_UNDEFINED;
					                    break;

					                case MotionEvent.ACTION_UP:
					                    if(fingerState != FINGER_DRAGGING) {
					                        fingerState = FINGER_RELEASED;

											Intent zoom = new Intent(getApplicationContext(), ImageViewActivity.class);
											zoom.putExtra("image", ImageData);
											startActivity(zoom);

					                    }
					                    else if (fingerState == FINGER_DRAGGING) fingerState = FINGER_RELEASED;
					                    else fingerState = FINGER_UNDEFINED;
					                    break;

					                case MotionEvent.ACTION_MOVE:
					                    if (fingerState == FINGER_TOUCHED || fingerState == FINGER_DRAGGING) fingerState = FINGER_DRAGGING;
					                    else fingerState = FINGER_UNDEFINED;
					                    break;

					                default:
					                    fingerState = FINGER_UNDEFINED;

					            }
								return false;
							}
						});
						
					}
				});
				
			}else {
				webView_image_spotchallan_xml.setVisibility(View.GONE);
			}
			
			/*********************Display Image*********************/
			return null;
		}
		
		@SuppressWarnings("deprecation")
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			removeDialog(PROGRESS_DIALOG);
		}
	}
	
	 class MyWebViewClient extends WebViewClient {
	        @Override
	        public boolean shouldOverrideUrlLoading(WebView view, String url) {
	            return super.shouldOverrideUrlLoading(view, url);
	        }

	        @Override
	        public void onPageStarted(WebView view, String url, Bitmap favicon) {
	            super.onPageStarted(view, url, favicon);
	        }

	        @Override
	        public void onLoadResource(WebView view, String url) {
	            super.onLoadResource(view, url);
	        }

	        @Override
	        public void onPageFinished(WebView view, String url) {
	            if (mbErrorOccured == false && mbReloadPressed) {
	                hideErrorLayout();
	                mbReloadPressed = false;
	            }

	            super.onPageFinished(view, url);
	        }

	        @Override
	        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
	            mbErrorOccured = true;
	            showErrorLayout();
	            super.onReceivedError(view, errorCode, description, failingUrl);
	        }
	    }
	 
	@SuppressWarnings("deprecation")
	@Override
	protected Dialog onCreateDialog(int id) {
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
	
	@SuppressWarnings("unused")
	private WebChromeClient getChromeClient() {
        final ProgressDialog progressDialog = new ProgressDialog(getApplicationContext());
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCancelable(false);

        return new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
            }
        };
    }
	
	  private void showErrorLayout() {
	        mlLayoutRequestError.setVisibility(View.VISIBLE);
	    }

	    private void hideErrorLayout() {
	        mhErrorLayoutHide.sendEmptyMessageDelayed(10000, 200);
	    }

	    @SuppressLint("HandlerLeak")
		private Handler getErrorLayoutHideHandler() {
	        return new Handler() {
	            @Override
	            public void handleMessage(Message msg) {
	                mlLayoutRequestError.setVisibility(View.GONE);
	                super.handleMessage(msg);
	            }
	        };
	    }
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		owner_name.setText("");
		vehicle_no_details.setText("");
		owner_layout.setVisibility(View.GONE);
		echallan_no.setText("");
		unit_name.setText("");
		date.setText("");
		time.setText("");
		DL_Aadhaar_text.setText(""); 
		place_violation.setText("");
		ps_limits.setText("");
		name_of_violation.setText("");
		fine_amnt.setText("");
		charges.setText("");
		total_amnt.setText(""); 
	}


	@Override
	public void onInit(int status) {

		if (status == TextToSpeech.SUCCESS) {

			int result = tts.setLanguage(Locale.US);

			if (result == TextToSpeech.LANG_MISSING_DATA
					|| result == TextToSpeech.LANG_NOT_SUPPORTED) {
				Log.e("TTS", "This Language is not supported");
			} else {
				speakOut();
			}

		} else {
			Log.e("TTS", "Initilization Failed!");
		}

	}
	
	@SuppressWarnings("deprecation")
	private void speakOut() {
		tts.setSpeechRate(0.5f);
		tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
	}
	
	@Override
	public void onDestroy() {
	// Don't forget to shutdown tts!
	if (tts != null) {
	    tts.stop();
	    tts.shutdown();
	}
	super.onDestroy();
	}
}
