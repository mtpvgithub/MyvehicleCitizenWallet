package com.mtpv.manohar.myvehicle_mywallet;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ZoomControls;

import com.mtpv.manohar.myvehicle_mywallet.Services.ServiceHelper;

import org.apache.commons.validator.routines.checkdigit.VerhoeffCheckDigit;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by MANOHAR on 5/22/2017.
 */

public class PendingChallansbyAadhar extends Fragment {


    public static String name,ticket_no ,offe_date ,offec_time ,poi_name ,ps_nam ,violation_data ,
            violat_amt ,actual_fine ,user_charges_dat ,cmd_fin_amt,img_url ,latt_data ,longit_data,AllDetails, regNo, unitName ;

    public int rowscount = 0, columnscount = 0, rows = 0, columns = 0;

    final int PROGRESS_DIALOG = 1;
    Button go, netbanking_btn ;
    public static EditText vehicle_no ;
    public static TextView owner_name, vehicle_no_details, total_amount, heading, service_data, vehicleNo_text, vehicle_heading;

    LinearLayout owner_layout ;

    public static String captcha_received;

    public static TableLayout table_layout;
    RelativeLayout vehicle_details_layout, dl_details;

    public static String one, two, three ;

    public static String OWNER_NAME,ETICKET_NO,OFFENCE_DATE,OFFENCE_TIME,PS_NAME,POINT_NAME,VIOLATION,VIOLATION_AMT,
            ACTUAL_FINE_AMT,USER_CHARGES,CMD_FINE_AMT,LATTITUDE, LONGITUDE,IMAGE_URL, VEHICLE_No, UNIT_NAME, REG_NO;
    TableRow tr;

    public static ScrollView scroll;

    SharedPreferences sharedpreference;
    SharedPreferences.Editor editors ;
    private String blockCharacterSet = ".-+()/@=<>?;:,~#^'|$%&*!abcdefghijklmnopqrstuvwxyz";
    private InputFilter filter = new InputFilter() {

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

            if (source != null && blockCharacterSet.contains(("" + source))) {
                return "";
            }
            return null;
        }
    };

    ZoomControls zoom;
    RelativeLayout container ;

    public static int clickCount = 1 ;

    public static String logout = "";

    public static TextView captcha_image;
    public static EditText captcha_text;
    public static ImageView resend_captcha, clear_btn;

    @SuppressWarnings("unused")
    private ScaleGestureDetector mScaleDetector;


    public static String date ;
    LocationManager m_locationlistner;
    Location location;

    static double latitude = 0.0;
    static double longitude = 0.0;
    String provider = "";
    static String IMEI = "", sim_No = "";

    /* DATE & TIME START */
    SimpleDateFormat date_format;
    Calendar calendar;
    int present_date;
    int present_month;
    int present_year;

    int present_hour;
    int present_minutes;
    public static String present_date_toSend = "";
    public static StringBuffer present_time_toSend;

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

    public static String UserName_send = "", Verify_Status = "",usertype=null;

    TextView note, title, title1 ;

    public static String regNoFLG = "0", dlFLG = "0", aadhaarFLG = "0" ;


    View rootView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         rootView = inflater.inflate(R.layout.activity_public__acitivity, container, false);

        TelephonyManager telephonyManager = (TelephonyManager)getActivity().getSystemService(Context.TELEPHONY_SERVICE);
        IMEI = telephonyManager.getDeviceId();
        if (telephonyManager.getSimState() != TelephonyManager.SIM_STATE_ABSENT) {
            sim_No = "" + telephonyManager.getSimSerialNumber();
            Log.i("SIM Number", ""+sim_No);
        } else {
            sim_No = "";
        }

        Log.i("IMEI and  sim_No :::", ""+IMEI+" and "+sim_No);

        String fontPath = "fonts/SerialPublicationDEMO.ttf";

        regNoFLG = "1" ;

        owner_layout = (LinearLayout)rootView.findViewById(R.id.owner_layout);

        scroll = (ScrollView)rootView.findViewById(R.id.scroll);
        scroll.setVisibility(View.GONE);

        heading = (TextView)rootView.findViewById(R.id.text);
        vehicle_no = (EditText)rootView.findViewById(R.id.vehicle_no_text);

        title = (TextView)rootView.findViewById(R.id.title);
//		title1 = (TextView)findViewById(R.id.title2);

        vehicle_heading = (TextView)rootView.findViewById(R.id.vehicle_heading);

        note = (TextView)rootView.findViewById(R.id.note);
        note.setTextColor(Color.RED);


        captcha_image = (TextView)rootView.findViewById(R.id.captcha_image);
        captcha_text = (EditText)rootView.findViewById(R.id.captcha_text);
        resend_captcha = (ImageView)rootView.findViewById(R.id.resend_captcha);

        captcha_image.setText("1234");
        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), fontPath);
        captcha_image.setTypeface(tf);
        captcha_text.requestFocus() ;

        vehicle_no_details = (TextView)rootView.findViewById(R.id.vehicle_no_details);
        owner_name = (TextView)rootView.findViewById(R.id.owner_name);
        total_amount = (TextView)rootView.findViewById(R.id.total_amount);

        service_data = (TextView)rootView.findViewById(R.id.server_data);
        vehicleNo_text = (TextView)rootView.findViewById(R.id.vehicleNo_text);
        table_layout = (TableLayout)rootView.findViewById(R.id.tableLayout1);
        container = (RelativeLayout)rootView.findViewById(R.id.container);

        vehicle_details_layout = (RelativeLayout)rootView.findViewById(R.id.vehicle_details_layout);



        getLocation();


        AsyncTask_Captcha asyncTask_captcha=new AsyncTask_Captcha();
        asyncTask_captcha.execute();



        zoom = (ZoomControls) rootView.findViewById(R.id.zoomControls1);
        zoom.setVisibility(View.GONE);

        table_layout = (TableLayout) rootView.findViewById(R.id.tableLayout);


        regNoFLG = "0";
        dlFLG = "0" ;
        aadhaarFLG = "1" ;

        vehicle_heading.setText("Aadhaar Details");
        heading.setText("Enter Aadhaar Number");
        vehicle_no.setHint("Enter Aadhaar Number");
        vehicleNo_text.setText("Aadhaar No");
        vehicle_no.setFilters(new InputFilter[] {});
        vehicle_no.setInputType(InputType.TYPE_CLASS_NUMBER);

        vehicle_no.setFilters(new InputFilter[] { new InputFilter.LengthFilter(12) });

        vehicle_no.setText("");
        vehicle_no_details.setText("");
        owner_name.setText("");
        owner_layout.setVisibility(View.GONE);
        total_amount.setText("");
        scroll.setVisibility(View.GONE);

        resend_captcha.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //   new Public_Acitivity.AsyncTask_Captcha().execute();

                AsyncTask_Captcha asyncTask_captcha=new AsyncTask_Captcha();
                asyncTask_captcha.execute();



                vehicle_no.setText("");
                //vehicle_no.setHint("Enter Vehicle Number");
                owner_name.setText("");
                total_amount.setText("");
                scroll.setVisibility(View.GONE);
            }
        });


        netbanking_btn = (Button)rootView.findViewById(R.id.netbanking_btn);
        go = (Button)rootView.findViewById(R.id.go_btn);

        go.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                owner_name.setText("");
                total_amount.setText("");
                service_data.setText("");
                vehicle_no_details.setText("");
                table_layout.removeAllViews();
                scroll.setVisibility(View.GONE);
                //vehicleNo_text.setText("");
                VerhoeffCheckDigit ver=new VerhoeffCheckDigit();
                if (!vehicle_no.getText().toString().equals("")) {
                    if (captcha_text.getText().toString().trim().equals("")) {
                        showtoast("Validation","Please Click on Resend Button for Captcha");
                        captcha_text.setError(Html.fromHtml("<font color='red'>Please Enter Captcha Here!</font>"));
                        captcha_text.requestFocus();
                    }else {
                        if (captcha_text.getText().toString().equals(""+captcha_received) && "Y".equals(Verify_Status)) {

                          Async_task_Aadhaar async_task_aadhaar=new Async_task_Aadhaar();
                            async_task_aadhaar.execute();


                        }else {
                            showtoast("Validation","Invalid Captcha !!!");
                            captcha_text.setError(Html.fromHtml("<font color='red'>Invalid Captcha!</font>"));
                            captcha_text.requestFocus();
                        }
                    }
                }else {
                    if (captcha_text.getText().toString().equals("")) {
                        captcha_text.setError(Html.fromHtml("<font color='red'>Please Enter Captcha!</font>"));
                        captcha_text.requestFocus();

                    }
                }
            }
        });
        netbanking_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (vehicle_no_details.getText().toString().equals("")) {
                    vehicle_no.setError(Html.fromHtml("<font color='red'>Please Enter Selected Item Number!</font>"));
                    vehicle_no.requestFocus();
                }else {
                    Intent browserIntent = new Intent(getActivity(), PopupDetails.class);
                    startActivity(browserIntent);
                    getActivity().overridePendingTransition(R.anim.fade_enter,R.anim.fade_leave);
                }
            }
        });


        return rootView;
    }

    void showtoast(String title ,String Message)
    {
        LayoutInflater inflator=getActivity().getLayoutInflater();
        View toastlayout=inflator.inflate(R.layout.my_toast,(ViewGroup)rootView.findViewById(R.id.toast_root_view));
        TextView toast_header=(TextView)toastlayout.findViewById(R.id.toast_header);
        toast_header.setText(title);
        TextView toast_body=(TextView)toastlayout.findViewById(R.id.toast_body);
        toast_body.setText(Message);
        Toast toast=new Toast(getActivity());
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(toast.LENGTH_LONG);
        toast.setView(toastlayout);
        toast.show();
    }


    public class ZoomableRelativeLayout extends TableLayout {
        float mScaleFactor = 1;
        float mPivotX;
        float mPivotY;

        public ZoomableRelativeLayout(Context context) {
            super(context);
        }

        public ZoomableRelativeLayout(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public ZoomableRelativeLayout(Context context, AttributeSet attrs,
                                      int defStyle) {
            super(context, attrs);
        }

        protected void dispatchDraw(Canvas canvas) {
            canvas.save(Canvas.MATRIX_SAVE_FLAG);
            canvas.scale(mScaleFactor, mScaleFactor, mPivotX, mPivotY);
            super.dispatchDraw(canvas);
            canvas.restore();
        }

        public void scale(float scaleFactor, float pivotX, float pivotY) {
            mScaleFactor = scaleFactor;
            mPivotX = pivotX;
            mPivotY = pivotY;
            this.invalidate();
        }

        public void restore() {
            mScaleFactor = 1;
            this.invalidate();
        }

    }

    public void onBackPressed() {
        Vehicle_Wallet_Login.alert_string = "onback";
        Intent logout = new Intent(getActivity().getApplicationContext(), Logout.class);
        startActivity(logout);
        getActivity().overridePendingTransition(R.anim.fade_enter,R.anim.fade_leave);

    }

    public void onLocationChanged(Location location) {
        if (location != null) {
            latitude = (float) location.getLatitude();
            longitude = (float) location.getLongitude();
        } else {
            latitude = 0.0;
            longitude = 0.0;
        }

    }

    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    public void onProviderEnabled(String provider) {

    }

    public void onProviderDisabled(String provider) {

    }



    public class AsyncTask_Captcha extends AsyncTask<Void, Void, String> {

        @SuppressWarnings("deprecation")
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            getActivity().showDialog(PROGRESS_DIALOG);
        }

        @Override
        protected String doInBackground(Void... params) {
            getDateAndTime();
            ServiceHelper.captchaService(""+Vehicle_Wallet_Login.username, ""+IMEI, ""+present_date_toSend,Vehicle_Wallet_Login.Mobilenum);
            return null;
        }

        @SuppressWarnings({ "deprecation" })
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            getActivity().removeDialog(PROGRESS_DIALOG);
            Log.i("captcha_received:::", ""+ServiceHelper.captch_resp );
            if (!ServiceHelper.captch_resp.equals("0")) {
                Verify_Status = "Y";
                captcha_received = ""+ServiceHelper.captch_resp ;
                captcha_image.setText(""+captcha_received);
                captcha_text.setText("");
                vehicle_no.setText("");
            }else {
                showtoast("Validation","Please Click on Resend Button for Valid Captcha");
                Verify_Status = "N";
            }
        }
    }

    private void getLocation() {

        try {
            m_locationlistner = (LocationManager) getActivity().getSystemService(getActivity().LOCATION_SERVICE);
            isGPSEnabled = m_locationlistner.isProviderEnabled(LocationManager.GPS_PROVIDER);

            isNetworkEnabled = m_locationlistner.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                latitude = 0.0;
                longitude = 0.0;
            } else {
                this.canGetLocation = true;
                if (isNetworkEnabled) {
                    m_locationlistner.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, (LocationListener) this);
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
                        m_locationlistner.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, (LocationListener) this);
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


    @SuppressWarnings("deprecation")
    public void addHeaders() {
        /** Create a TableRow dynamically **/
        tr = new TableRow(getActivity());
        tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

        /** Creating a TextView to add to the row **/
        TextView serialNo = new TextView(getActivity());
        serialNo.setText("S.No");
        serialNo.setBackgroundResource(R.drawable.cell_heading);
        serialNo.setGravity(Gravity.CENTER);
        serialNo.setTextColor(Color.WHITE);
        serialNo.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        serialNo.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        serialNo.setPadding(5,11,5,11);
        serialNo.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.text_size_header));
        tr.addView(serialNo);

        /** Creating a TextView to add to the row **/
        TextView date_headng = new TextView(getActivity());
        date_headng.setText("Date");
        date_headng.setBackgroundResource(R.drawable.cell_heading);
        date_headng.setGravity(Gravity.CENTER);
        date_headng.setTextColor(Color.WHITE);
        date_headng.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        date_headng.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        date_headng.setPadding(5,11,5,11);
        date_headng.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.text_size_header));
        tr.addView(date_headng);

        /** Creating another textview **/
        TextView violation_hder = new TextView(getActivity());
        violation_hder.setText("Violation");
        violation_hder.setBackgroundResource(R.drawable.cell_heading);
        violation_hder.setGravity(Gravity.CENTER);
        violation_hder.setTextColor(Color.WHITE);
        violation_hder.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        violation_hder.setPadding(5,11,5,11);
        violation_hder.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.text_size_header));
        violation_hder.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tr.addView(violation_hder);

        /** Creating another textview **/
        TextView amount_hder = new TextView(getActivity());
        amount_hder.setText("Amount");
        amount_hder.setBackgroundResource(R.drawable.cell_heading);
        amount_hder.setGravity(Gravity.CENTER);
        amount_hder.setTextColor(Color.WHITE);
        amount_hder.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        amount_hder.setPadding(5,11,5,11);
        amount_hder.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.text_size_header));
        amount_hder.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tr.addView(amount_hder);

        table_layout.addView(tr, new TableLayout.LayoutParams(
                TableRow.LayoutParams.FILL_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT));

    }

    @SuppressWarnings("deprecation")
    private void getDateAndTime() {
        calendar = Calendar.getInstance();

        present_date = calendar.get(Calendar.DAY_OF_MONTH);
        present_month = calendar.get(Calendar.MONTH);
        present_year = calendar.get(Calendar.YEAR);

        date_format = new SimpleDateFormat("dd-MMM-yyyy");

        present_date_toSend = date_format.format(new Date(present_year - 1900, present_month, present_date));

		/* TIME START */

        present_hour = calendar.get(Calendar.HOUR_OF_DAY);
        present_minutes = calendar.get(Calendar.MINUTE);

        present_time_toSend = new StringBuffer();
        present_time_toSend.delete(0, present_time_toSend.length());

        if (present_hour < 10) {
            present_time_toSend.append("0").append(present_hour);
        } else {
            present_time_toSend.append(present_hour);
        }
        present_time_toSend.append(":");

        if (present_minutes < 10) {
            present_time_toSend.append("0").append(present_minutes);
        } else {
            present_time_toSend.append(present_minutes);
        }

		/* TIME END */
    }


    public class Async_task_Aadhaar extends AsyncTask<Void, Void, String> {
        String getAadhaar = vehicle_no.getText().toString().trim();

        @SuppressWarnings("deprecation")
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            getActivity().showDialog(PROGRESS_DIALOG);
        }

        @Override
        protected String doInBackground(Void... params) {
            ServiceHelper.getViolationsByAadhaarDetails(getAadhaar,Vehicle_Wallet_Login.usertype,captcha_received,Vehicle_Wallet_Login.Mobilenum);
            return null;
        }

        @SuppressWarnings({ "deprecation" })
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            getActivity().removeDialog(PROGRESS_DIALOG);

            if (ServiceHelper.aadhaar_response.equals("0")) {
                showtoast("Validation","No Challans Found");
                vehicle_no.setText("");
                table_layout.removeAllViews();
                scroll.setVisibility(View.GONE);
            }else if (ServiceHelper.aadhaar_response.equals("1")){
                showtoast("Validation","Invalid Captcha");
                captcha_text.setText("");
                table_layout.removeAllViews();
                scroll.setVisibility(View.GONE);
            }else if (ServiceHelper.aadhaar_response.equals("2")) {
                showtoast("Validation","Invalid Vehicle Details");
                vehicle_no.setText("");
                table_layout.removeAllViews();
                scroll.setVisibility(View.GONE);
            }else if (ServiceHelper.aadhaar_response.toString().trim().equals("anyType{}")) {
                showtoast("Validation","Please Try After SomeTime");
                vehicle_no.setText("");
                table_layout.removeAllViews();
                scroll.setVisibility(View.GONE);
            }else if (ServiceHelper.aadhaar_response.equals("3")) {
                Vehicle_Wallet_Login.alert_string = "exceed";
                vehicle_no.setText("");
                table_layout.removeAllViews();
                scroll.setVisibility(View.GONE);

                Intent logout = new Intent(getActivity(), Logout.class);
                startActivity(logout);
                getActivity().overridePendingTransition(R.anim.fade_enter,R.anim.fade_leave);

            }else {
                scroll.setVisibility(View.VISIBLE);

                SimpleDateFormat parse=new SimpleDateFormat("yyyy-mm-dd");
                SimpleDateFormat format=new SimpleDateFormat("dd-mm-yyyy");
                int totalfine=0;
                ArrayList<String> arrIds = new ArrayList<String>();

                String strJson = ServiceHelper.aadhaar_response;
                try {
                    JSONObject jsonRootObject = new JSONObject(strJson);//array method
                    JSONArray jsonArray = jsonRootObject.optJSONArray("challan_data");//array object

                    table_layout.removeAllViews();
                    addHeaders();
                    for(int i=0; i < jsonArray.length(); i++){

                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        name = jsonObject.optString("OWNER_NAME").toString();
                        ticket_no = jsonObject.optString("ETICKET_NO").toString();
                        offe_date = jsonObject.optString("OFFENCE_DATE").toString();
                        offec_time = jsonObject.optString("OFFENCE_TIME").toString();
                        poi_name = jsonObject.optString("POINT_NAME").toString();
                        ps_nam = jsonObject.optString("PS_NAME").toString();
                        violation_data = jsonObject.optString("VIOLATION").toString();
                        violat_amt = jsonObject.optString("VIOLATION_AMT").toString();
                        actual_fine = jsonObject.optString("ACTUAL_FINE_AMT").toString();
                        user_charges_dat = jsonObject.optString("USER_CHARGES").toString();
                        cmd_fin_amt = jsonObject.optString("CMD_FINE_AMT").toString();
                        img_url = jsonObject.optString("IMAGE_URL").toString();
                        latt_data = jsonObject.optString("LATTITUDE").toString();
                        longit_data = jsonObject.optString("LONGITUDE").toString();

                        regNo = jsonObject.optString("REGN_NO").toString();
                        unitName = jsonObject.optString("UNIT_NAME").toString();

                        Log.i("ticket_no :::::", (i+1)+"."+ticket_no);
                        AllDetails += name +"\n"+ticket_no+"\n"+offe_date+"\n"+offec_time+"\n"+poi_name+"\n"+ps_nam+"\n"+
                                violation_data+"\n"+violat_amt+"\n"+actual_fine+"\n"+user_charges_dat+"\n"+cmd_fin_amt+"\n"+img_url
                                +"\n"+latt_data+"\n"+longit_data;

                        rows = jsonArray.length();

                        owner_name.setText(name);
                        vehicle_no_details.setText(vehicle_no.getText().toString().trim());

                        arrIds.add(ticket_no);

                        final TableRow row = new TableRow(getActivity());
                        row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,	TableRow.LayoutParams.WRAP_CONTENT));
                        row.setClickable(true);

                        for (int j = 1; j <= 4; j++) {
                            if (j == 1) {
                                TextView tv = new TextView(getActivity());
                                tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,	TableRow.LayoutParams.WRAP_CONTENT));
                                tv.setBackgroundResource(R.drawable.cell_shape);
                                tv.setPadding(5,12,5,12);
                                tv.setText((i+1)+".");
                                tv.setTextColor(Color.BLACK);
                                tv.setGravity(Gravity.CENTER);
                                tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.text_size));
                                row.addView(tv);
                            }else if (j == 2) {
                                String date="";
                                try {
                                    date=format.format(parse.parse(offe_date));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                TextView tv = new TextView(getActivity());
                                tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,	TableRow.LayoutParams.WRAP_CONTENT));
                                tv.setBackgroundResource(R.drawable.cell_shape);
                                tv.setPadding(5,12,5,12);
                                tv.setText(date);
                                tv.setTextColor(Color.BLACK);
                                tv.setGravity(Gravity.CENTER);
                                tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.text_size));
                                row.addView(tv);
                            }else if(j == 3){
                                final TextView tv_name = new TextView(getActivity());
                                tv_name.setText(name);
                                tv_name.setVisibility(View.GONE);
                                final TextView tv_ticket_no = new TextView(getActivity());
                                tv_ticket_no.setText(ticket_no);
                                tv_ticket_no.setVisibility(View.GONE);
                                final TextView tv_offe_date = new TextView(getActivity());
                                tv_offe_date.setText(offe_date);
                                tv_offe_date.setVisibility(View.GONE);
                                final TextView tv_offec_time = new TextView(getActivity());
                                tv_offec_time.setText(offec_time);
                                tv_offec_time.setVisibility(View.GONE);
                                final TextView tv_ps_nam = new TextView(getActivity());
                                tv_ps_nam.setText(ps_nam);
                                tv_ps_nam.setVisibility(View.GONE);
                                final TextView tv_poi_name = new TextView(getActivity());
                                tv_poi_name.setText(poi_name);
                                tv_poi_name.setVisibility(View.GONE);
                                final TextView tv_violat_amt = new TextView(getActivity());
                                tv_violat_amt.setText(violat_amt);
                                tv_violat_amt.setVisibility(View.GONE);
                                final TextView tv_actual_fine = new TextView(getActivity());
                                tv_actual_fine.setText(actual_fine);
                                tv_actual_fine.setVisibility(View.GONE);
                                final TextView tv_user_charges_dat = new TextView(getActivity());
                                tv_user_charges_dat.setText(user_charges_dat);
                                tv_user_charges_dat.setVisibility(View.GONE);
                                final TextView tv_cmd_fin_amt = new TextView(getActivity());
                                tv_cmd_fin_amt.setText(cmd_fin_amt);
                                tv_cmd_fin_amt.setVisibility(View.GONE);
                                final TextView tv_latt_data = new TextView(getActivity());
                                tv_latt_data.setText(latt_data);
                                tv_latt_data.setVisibility(View.GONE);
                                final TextView tv_longit_data = new TextView(getActivity());
                                tv_longit_data.setText(longit_data);
                                tv_longit_data.setVisibility(View.GONE);
                                final TextView tv_img_url = new TextView(getActivity());
                                tv_img_url.setText(img_url);
                                tv_img_url.setVisibility(View.GONE);

                                final TextView tv_reg_no = new TextView(getActivity());
                                tv_reg_no.setText(regNo);
                                tv_reg_no.setVisibility(View.GONE);

                                final TextView tv_unit_name = new TextView(getActivity());
                                tv_unit_name.setText(unitName);
                                tv_unit_name.setVisibility(View.GONE);

                                final TextView tv = new TextView(getActivity());
                                tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,	TableRow.LayoutParams.WRAP_CONTENT));
                                tv.setBackgroundResource(R.drawable.cell_shape);
                                tv.setPadding(5,12,5,12);
                                tv.setTextColor(Color.BLACK);
                                tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.text_size));

                                if(violation_data.contains("|")){
                                    tv.setText(violation_data.replaceAll("\\|", "\\,"));
                                }else{
                                    tv.setText(violation_data);
                                }
                                tv.setOnClickListener(new View.OnClickListener() {

                                    @Override
                                    public void onClick(View arg0) {
                                        showtoast("Validation",""+tv.getText().toString());
                                        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                                        SharedPreferences.Editor editors = sharedPreferences.edit();

                                        OWNER_NAME = tv_name.getText().toString();
                                        ETICKET_NO = tv_ticket_no.getText().toString();
                                        OFFENCE_DATE = tv_offe_date.getText().toString();
                                        OFFENCE_TIME = tv_offec_time.getText().toString();
                                        PS_NAME = tv_ps_nam.getText().toString();
                                        POINT_NAME = tv_poi_name.getText().toString();
                                        VIOLATION = tv.getText().toString();
                                        VIOLATION_AMT = tv_violat_amt.getText().toString();
                                        ACTUAL_FINE_AMT = tv_actual_fine.getText().toString();
                                        USER_CHARGES = tv_user_charges_dat.getText().toString();
                                        CMD_FINE_AMT = tv_cmd_fin_amt.getText().toString();
                                        LATTITUDE = tv_latt_data.getText().toString();
                                        LONGITUDE = tv_longit_data.getText().toString();
                                        IMAGE_URL = tv_img_url.getText().toString();
                                        VEHICLE_No = vehicle_no.getText().toString();

                                        REG_NO = tv_reg_no.getText().toString().trim();
                                        UNIT_NAME = tv_unit_name.getText().toString().trim();

                                        editors.putString("OWNER_NAME", OWNER_NAME);
                                        editors.putString("ETICKET_NO", ETICKET_NO);
                                        editors.putString("OFFENCE_DATE", OFFENCE_DATE);
                                        editors.putString("OFFENCE_TIME", OFFENCE_TIME);
                                        editors.putString("PS_NAME", PS_NAME);
                                        editors.putString("POINT_NAME", POINT_NAME);
                                        editors.putString("VIOLATION", VIOLATION);
                                        editors.putString("VIOLATION_AMT", VIOLATION_AMT);
                                        editors.putString("ACTUAL_FINE_AMT", ACTUAL_FINE_AMT);
                                        editors.putString("USER_CHARGES", USER_CHARGES);
                                        editors.putString("CMD_FINE_AMT", CMD_FINE_AMT);
                                        editors.putString("LATTITUDE", LATTITUDE);
                                        editors.putString("LONGITUDE", LONGITUDE);
                                        editors.putString("IMAGE_URL", IMAGE_URL);
                                        editors.putString("VEHICLE_NO", VEHICLE_No);
                                        editors.putString("REGISTRATION_NO", REG_NO);
                                        editors.putString("UNIT_NAME", UNIT_NAME);
                                        editors.commit();

                                        Intent challan_detail = new Intent(getActivity(), Challan_Detail.class);
                                        startActivity(challan_detail);
                                        getActivity().overridePendingTransition(R.anim.fade_enter,R.anim.fade_leave);
                                    }
                                });
                                row.addView(tv);
                            }else if(j == 4){
                                final TextView tv_name = new TextView(getActivity());
                                tv_name.setText(name);
                                tv_name.setVisibility(View.GONE);
                                final TextView tv_ticket_no = new TextView(getActivity());
                                tv_ticket_no.setText(ticket_no);
                                tv_ticket_no.setVisibility(View.GONE);
                                final TextView tv_offe_date = new TextView(getActivity());
                                tv_offe_date.setText(offe_date);
                                tv_offe_date.setVisibility(View.GONE);
                                final TextView tv_offec_time = new TextView(getActivity());
                                tv_offec_time.setText(offec_time);
                                tv_offec_time.setVisibility(View.GONE);
                                final TextView tv_ps_nam = new TextView(getActivity());
                                tv_ps_nam.setText(ps_nam);
                                tv_ps_nam.setVisibility(View.GONE);
                                final TextView tv_poi_name = new TextView(getActivity());
                                tv_poi_name.setText(poi_name);
                                tv_poi_name.setVisibility(View.GONE);
                                final TextView tv_violat_data = new TextView(getActivity());
                                tv_violat_data.setText(violation_data);
                                tv_violat_data.setVisibility(View.GONE);
                                final TextView tv_actual_fine = new TextView(getActivity());
                                tv_actual_fine.setText(actual_fine);
                                tv_actual_fine.setVisibility(View.GONE);
                                final TextView tv_user_charges_dat = new TextView(getActivity());
                                tv_user_charges_dat.setText(user_charges_dat);
                                tv_user_charges_dat.setVisibility(View.GONE);
                                final TextView tv_cmd_fin_amt = new TextView(getActivity());
                                tv_cmd_fin_amt.setText(cmd_fin_amt);
                                tv_cmd_fin_amt.setVisibility(View.GONE);
                                final TextView tv_latt_data = new TextView(getActivity());
                                tv_latt_data.setText(latt_data);
                                tv_latt_data.setVisibility(View.GONE);
                                final TextView tv_longit_data = new TextView(getActivity());
                                tv_longit_data.setText(longit_data);
                                tv_longit_data.setVisibility(View.GONE);
                                final TextView tv_img_url = new TextView(getActivity());
                                tv_img_url.setText(img_url);
                                tv_img_url.setVisibility(View.GONE);

                                final TextView tv_reg_no = new TextView(getActivity());
                                tv_reg_no.setText(regNo);
                                tv_reg_no.setVisibility(View.GONE);

                                final TextView tv_unit_name = new TextView(getActivity());
                                tv_unit_name.setText(unitName);
                                tv_unit_name.setVisibility(View.GONE);

                                final TextView tv = new TextView(getActivity());
                                tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,	TableRow.LayoutParams.WRAP_CONTENT));
                                tv.setBackgroundResource(R.drawable.cell_shape);
                                tv.setGravity(Gravity.CENTER);
                                tv.setPadding(5,12,5,12);
                                tv.setTextColor(Color.BLACK);
                                tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.text_size));
                                int totamt=0;
                                if(violat_amt.contains("|")){
                                    String []amt=violat_amt.split("\\|");

                                    for (String k:amt) {
                                        totamt+=Integer.parseInt(k.trim());
                                    }
                                    tv.setText(""+(totamt+Integer.parseInt(tv_user_charges_dat.getText().toString())));
                                }else{
                                    tv.setText(""+(Integer.parseInt(violat_amt)+Integer.parseInt(tv_user_charges_dat.getText().toString())));
                                    totamt=Integer.parseInt(violat_amt);
                                }

                                tv.setOnClickListener(new View.OnClickListener() {

                                    @Override
                                    public void onClick(View arg0) {
                                        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                                        SharedPreferences.Editor editors = sharedPreferences.edit();

                                        OWNER_NAME = tv_name.getText().toString();
                                        ETICKET_NO = tv_ticket_no.getText().toString();
                                        OFFENCE_DATE = tv_offe_date.getText().toString();
                                        OFFENCE_TIME = tv_offec_time.getText().toString();
                                        PS_NAME = tv_ps_nam.getText().toString();
                                        POINT_NAME = tv_poi_name.getText().toString();
                                        VIOLATION = tv_violat_data.getText().toString();
                                        VIOLATION_AMT = tv.getText().toString();
                                        ACTUAL_FINE_AMT = tv_actual_fine.getText().toString();
                                        USER_CHARGES = tv_user_charges_dat.getText().toString();
                                        CMD_FINE_AMT = tv_cmd_fin_amt.getText().toString();
                                        LATTITUDE = tv_latt_data.getText().toString();
                                        LONGITUDE = tv_longit_data.getText().toString();
                                        IMAGE_URL = tv_img_url.getText().toString();
                                        VEHICLE_No = vehicle_no.getText().toString();

                                        REG_NO = tv_reg_no.getText().toString().trim();
                                        UNIT_NAME = tv_unit_name.getText().toString().trim();

                                        editors.putString("OWNER_NAME", OWNER_NAME);
                                        editors.putString("ETICKET_NO", ETICKET_NO);
                                        editors.putString("OFFENCE_DATE", OFFENCE_DATE);
                                        editors.putString("OFFENCE_TIME", OFFENCE_TIME);
                                        editors.putString("PS_NAME", PS_NAME);
                                        editors.putString("POINT_NAME", POINT_NAME);
                                        editors.putString("VIOLATION", VIOLATION);
                                        editors.putString("VIOLATION_AMT", VIOLATION_AMT);
                                        editors.putString("ACTUAL_FINE_AMT", ACTUAL_FINE_AMT);
                                        editors.putString("USER_CHARGES", USER_CHARGES);
                                        editors.putString("CMD_FINE_AMT", CMD_FINE_AMT);
                                        editors.putString("LATTITUDE", LATTITUDE);
                                        editors.putString("LONGITUDE", LONGITUDE);
                                        editors.putString("IMAGE_URL", IMAGE_URL);
                                        editors.putString("VEHICLE_NO", VEHICLE_No);
                                        editors.putString("REGISTRATION_NO", REG_NO);
                                        editors.putString("UNIT_NAME", UNIT_NAME);
                                        editors.commit();

                                        Intent challan_detail = new Intent(getActivity(), Challan_Detail.class);
                                        startActivity(challan_detail);
                                        getActivity().overridePendingTransition(R.anim.fade_enter,R.anim.fade_leave);

                                    }
                                });
                                totalfine+=totamt;
                                row.addView(tv);
                            }
                        }

                        total_amount.setText(""+totalfine+".00");
                        table_layout.addView(row);

                    }
                } catch (JSONException e) {e.printStackTrace();}
            }
          AsyncTask_Captcha asyncTask_captcha=new AsyncTask_Captcha();
            asyncTask_captcha.execute();
        }
    }

}
