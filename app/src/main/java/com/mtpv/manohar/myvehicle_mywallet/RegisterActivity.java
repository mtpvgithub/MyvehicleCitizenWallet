package com.mtpv.manohar.myvehicle_mywallet;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.format.Time;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mtpv.manohar.myvehicle_mywallet.Services.ServiceHelper;
import com.mtpv.manohar.myvehicle_mywallet.Utils.Utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;


@SuppressWarnings("deprecation")
@SuppressLint({ "SimpleDateFormat", "DefaultLocale", "InlinedApi" })
public class RegisterActivity extends Activity implements LocationListener {

    public static EditText name, vehicleNo, mobile, email, password, confirm_password ;
    Button submit, cancel ;

    public static String date ;
    LocationManager m_locationlistner;
    Location location;

    static double latitude = 0.0;
    static double longitude = 0.0;
    String provider = "";
    static String IMEI = "";

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

    final int PROGRESS_DIALOG = 1;

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

    public static String otp_status = "";
    public static String otp_msg = "";
    public static String otpValue = "";
    public static String vStatusConfirmationYN = "", ll_validationString = "";
    public static String mobile_No, regnNo, email_Id, pass_word, sim_No, imeie_No, gps_Lattitude, gps_Longitude, key_no ;


    private static final int REQUEST_APP_SETTINGS = 168;

    private static final String[] requiredPermissions = new String[]{
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.INTERNET,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.INSTALL_SHORTCUT
        /* ETC.. */
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_register);

        RelativeLayout registerActivity=(RelativeLayout)findViewById(R.id.container);

        registerActivity.getBackground().setAlpha(50);

        if (Build.VERSION.SDK_INT > 22 && !hasPermissions(requiredPermissions)) {
            Toast.makeText(this, "Please grant all permissions", Toast.LENGTH_LONG).show();
            goToSettings();
        }

        getLocation();
        getDateAndTime();

        name = (EditText)findViewById(R.id.et_name);
        vehicleNo = (EditText)findViewById(R.id.et_vehicleNo);
        mobile = (EditText)findViewById(R.id.et_mobile);
        email = (EditText)findViewById(R.id.et_email);
        password = (EditText)findViewById(R.id.et_password);
        confirm_password = (EditText)findViewById(R.id.et_confirmpassword);

        submit = (Button) findViewById(R.id.submit);
        cancel = (Button) findViewById(R.id.cancel);

        cancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                finish();
            }
        });

        submit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                String Regno=vehicleNo.getText().toString().trim();

                boolean regvalid=new Utils().Vehilcevalidation(Regno);

                if (name.getText().toString().trim().equals("")) {
                  //  name.setError(Html.fromHtml("<font color='red'>Please Enter Name!</font>"));
                    showToast("Validation","Please Enter Your Name");


                    name.requestFocus();
                }else if(!regvalid)
                {
                    showToast("Validation","Please Enter Proper Vehicle Number!");
                    vehicleNo.requestFocus();
                }

                else if (vehicleNo.getText().toString().trim().equals("")) {
                  //  vehicleNo.setError(Html.fromHtml("<font color='red'>Please Enter Vehicle Number!</font>"));
                    showToast("Validation","Please Enter Vehicle Number!");
                    vehicleNo.requestFocus();
                }else if (mobile.getText().toString().trim().equals("")) {
                  //  mobile.setError(Html.fromHtml("<font color='red'>Please Enter Mobile Number!</font>"));
                    showToast("Validation","Please Enter Mobile Number!");
                    mobile.requestFocus();
                }else if (mobile.getText().toString().trim().length()>1 && mobile.getText().toString().trim().length() != 10) {
                   // mobile.setError(Html.fromHtml("<font color='red'>Please Enter 10 digit Mobile Number!</font>"));
                    showToast("Validation","Please Enter 10 digit Mobile Number!");
                    mobile.requestFocus();
                }else if (password.getText().toString().trim().equals("")) {
                  //  password.setError(Html.fromHtml("<font color='red'>Please Enter Password!</font>"));
                    showToast("Validation","Please Enter Password!");
                    password.requestFocus();
                }else if (confirm_password.getText().toString().trim().equals("")) {
                    //confirm_password.setError(Html.fromHtml("<font color='red'>Please Enter Confirm Password!</font>"));
                    showToast("Validation","Please Enter Confirm Password!");
                    confirm_password.requestFocus();
                }else if (!confirm_password.getText().toString().equals(password.getText().toString()) ) {
                   // password.setError(Html.fromHtml("<font color='red'>Password Doesn't Match!</font>"));
                    showToast("Validation","Password Doesn't Match!");
                    password.setText("");
                    confirm_password.setText("");
                    password.requestFocus();
                }else if (email.getText().toString().trim().equals("")) {
                    //email.setError(Html.fromHtml("<font color='red'>Please Enter Email!</font>"));
                    showToast("Validation","Please Enter Email!");
                    email.requestFocus();
                }
                else if (!validateMobileNo(mobile.getText().toString())) {
                  //  mobile.setError(Html.fromHtml("<font color='red'>Please Enter 10 digit Mobile Number!</font>"));
                    showToast("Validation","Please Enter 10 digit Mobile Number!");
                    mobile.requestFocus();
                }else if (!isValidEmaillId(email.getText().toString().trim())) {
            //        email.setError(Html.fromHtml("<font color='red'>Please Enter Valid Email!</font>"));
                    showToast("Validation","Please Enter Valid Email!");
                    email.requestFocus();
                }else {
                    if (isOnline()) {
                       // Log.i("***OTP CONFIRMATION ENTERED", ""	+ vStatusConfirmationYN);
                        otp_status = "verify";
                        new Async_send_otp().execute();
                    } else {
                        showToast("Validation","Please check your network connection!");
                    }
                    //new Async_Register().execute();
                }
            }
        });
    }


    private boolean isValidEmaillId(String email){

        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches();
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
        Log.i("mobile number",mobileNo+" Length "+mobileNo.trim().length()+"1 letter"+mobileNo.trim().substring(0,1));
        Log.i("mobile verify flag",flg+"");

        return flg;
    }
    /***********************Mobile Number Validation Method Ends***************************/

    public Boolean isOnline() {
        ConnectivityManager conManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nwInfo = conManager.getActiveNetworkInfo();
        return nwInfo != null;
    }

    private void getDateAndTime() {
        // TODO Auto-generated method stub
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
                    m_locationlistner.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
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
                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {
                    if (location == null) {
                        m_locationlistner.requestLocationUpdates(LocationManager.GPS_PROVIDER,
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

        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        IMEI = getDeviceID(telephonyManager);
        sim_No = telephonyManager.getLine1Number();

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

    class Async_send_otp extends AsyncTask<Void, Void, String>
    {
        ProgressDialog dialog;
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            //showDialog(PROGRESS_DIALOG);
            dialog = new ProgressDialog(RegisterActivity.this);
            dialog.setTitle("Verifying  Details");
            dialog.setMessage("Please wait....");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(Void... arg0) {
            // TODO Auto-generated method stub
            mobile_No = mobile.getText().toString().trim();
            email_Id = email.getText().toString().trim();
            pass_word = password.getText().toString().trim();
            regnNo = vehicleNo.getText().toString().trim();
            imeie_No = IMEI ;
            gps_Lattitude = ""+latitude ;
            gps_Longitude = ""+longitude ;
            key_no = "";

            ServiceHelper.sendOTP(regnNo, mobile_No, present_date_toSend);
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            //removeDialog(PROGRESS_DIALOG);
            dialog.dismiss();
            otp_msg = "";
            otpValue = "";
            if (ServiceHelper.otp_sent_resp.toLowerCase().equals("na")) {

            } else {
                showToast("Validation","OTP is sent to your mobile number");
                otpValue = "" + ServiceHelper.otp_sent_resp;
                Intent dialogbox = new Intent(getApplicationContext(), OTP.class );

                dialogbox.putExtra("regNO", regnNo);
                dialogbox.putExtra("MobileNo", mobile_No);
                dialogbox.putExtra("otp_date", ""+getDate().toUpperCase());
                dialogbox.putExtra("OTP_value", otpValue);
                dialogbox.putExtra("email", ""+email.getText().toString().trim());
                dialogbox.putExtra("password", ""+password.getText().toString().trim());

                startActivity(dialogbox);
                overridePendingTransition(R.anim.fade_enter,R.anim.fade_leave);
            }
        }

    }

    public static String getDate() {
        Time today = new Time(Time.getCurrentTimezone());
        today.setToNow();
        System.out.println(today.month);
        return today.monthDay + "-" + getMonthName(today.month) + "-"
                + today.year;
    }

    public static String getMonthName(int month) {
        switch (month + 1) {
            case 1:
                return "Jan";

            case 2:
                return "Feb";

            case 3:
                return "Mar";

            case 4:
                return "Apr";

            case 5:
                return "May";

            case 6:
                return "Jun";

            case 7:
                return "Jul";

            case 8:
                return "Aug";

            case 9:
                return "Sep";

            case 10:
                return "Oct";

            case 11:
                return "Nov";

            case 12:
                return "Dec";
        }

        return "";
    }


    class Async_Register extends AsyncTask<Void, Void, String>
    {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
        //    showDialog(PROGRESS_DIALOG);
            dialog = new ProgressDialog(RegisterActivity.this);
            dialog.setTitle("Verifying  Details");
            dialog.setMessage("Please wait....");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(Void... arg0) {
            // TODO Auto-generated method stub
            mobile_No = mobile.getText().toString().trim();
            email_Id = email.getText().toString().trim();
            pass_word = password.getText().toString().trim();
            imeie_No = IMEI ;
            gps_Lattitude = ""+latitude ;
            gps_Longitude = ""+longitude ;
            key_no = "";

            //ServiceHelper.registration(""+name, ""+regnNo, ""+ mobile_No, ""+email_Id, ""+pass_word, ""+RegisterActivity.IMEI, "");
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
           // removeDialog(PROGRESS_DIALOG);
            dialog.dismiss();
        }

    }

//    @Override
//    protected Dialog onCreateDialog(int id) {
//        // TODO Auto-generated method stub
//        switch (id) {
//            case PROGRESS_DIALOG:
//                ProgressDialog pd = ProgressDialog.show(this, "", "",	true);
//                pd.setContentView(R.layout.custom_progress_dialog);
//                pd.setCancelable(false);
//
//                return pd;
//
//            default:
//                break;
//        }
//        return super.onCreateDialog(id);
//    }



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

    @Override
    public void onLocationChanged(Location location) {
        // TODO Auto-generated method stub

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

    private void goToSettings() {
        Intent myAppSettings = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getPackageName()));
        myAppSettings.addCategory(Intent.CATEGORY_DEFAULT);
        myAppSettings.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivityForResult(myAppSettings, REQUEST_APP_SETTINGS);
    }

    public boolean hasPermissions(String... permissions) {
        for (String permission : permissions)
            if (PackageManager.PERMISSION_GRANTED != checkCallingOrSelfPermission(permission))
                return false;
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_APP_SETTINGS) {
            if (hasPermissions(requiredPermissions)) {
                Toast.makeText(this, "All permissions granted!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permissions not granted.", Toast.LENGTH_LONG).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
