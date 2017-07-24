package com.mtpv.manohar.myvehicle_mywallet;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.widget.TextView;

public class ImageViewActivity extends Activity {

	WebviewLoader webviewloader;
	WebView webView_image;
	
	final int PROGRESS_DIALOG = 1;
    String URL = "";
	public ArrayList<Integer> imageName;
	String image_Data ;
	
	TextView note ;
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_image_view);
		
		webView_image = (WebView)findViewById(R.id.webView_image);
		
		note = (TextView)findViewById(R.id.note);
		note.setTextColor(Color.RED);
		
	    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
	    final String ImageData = sharedPreferences.getString("IMAGE_URL", "");
	    
	    webviewloader = new WebviewLoader();
	    webView_image.setBackgroundColor(0x00000000);
	    webView_image.setHorizontalScrollBarEnabled(true);
	    webView_image.setVerticalScrollBarEnabled(true);
		WebSettings webSettings = webView_image.getSettings();
		webView_image.setInitialScale(50);
		webSettings.setJavaScriptEnabled(false);
		webView_image.getSettings().setLoadWithOverviewMode(true);
		webView_image.getSettings().setUseWideViewPort(true);
		webView_image.getSettings().setBuiltInZoomControls(true);
		webView_image.getSettings().setSupportZoom(true);
		webView_image.getSettings().setDisplayZoomControls(false);
		webView_image.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		webviewloader.DisplayImage(""+ImageData, webView_image);
	}
	
	@Override
	public void onBackPressed() {
		Intent intent = new Intent(ImageViewActivity.this, Challan_Detail.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
		startActivity(intent);
    	overridePendingTransition(R.anim.fade_enter,R.anim.fade_leave);
	}
}