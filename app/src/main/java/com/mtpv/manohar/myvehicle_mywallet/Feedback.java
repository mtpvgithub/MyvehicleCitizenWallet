package com.mtpv.manohar.myvehicle_mywallet;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mtpv.manohar.myvehicle_mywallet.Services.ServiceHelper;

import java.util.Calendar;

/**
 * Created by MANOHAR on 5/23/2017.
 */

public class Feedback extends AppCompatActivity implements  LocationListener{

    Button Btn_Dldetails,Btn_Rcdetails,Btn_Complaints,Btn_Pendingchallans,Btn_Aadhardetails,Btn_overall;
    RatingBar ratingbar;
    private int mYear, mMonth, mDay;

    LocationManager m_locationlistner;
    Location location;
    static double latitude = 0.0;
    static double longitude = 0.0;
    boolean isGPSEnabled = false;
    // flag for network status
    boolean isNetworkEnabled = false;
    boolean canGetLocation = false;
    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters
    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute
    String moduleType=null,remarks=null,date=null;
    EditText et_remarks;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback);

        getLocation();
         ratingbar=(RatingBar)findViewById(R.id.ratingbar);
        et_remarks=(EditText)findViewById(R.id.et_remarks);


        Btn_overall=(Button)findViewById(R.id.Btn_overall);
        Btn_Aadhardetails=(Button)findViewById(R.id.Btn_Aadhardetails);
        Btn_Pendingchallans=(Button)findViewById(R.id.Btn_Pendingchallans);
        Btn_Complaints=(Button)findViewById(R.id.Btn_Complaints);
        Btn_Rcdetails=(Button)findViewById(R.id.Btn_Rcdetails);
        Btn_Dldetails=(Button)findViewById(R.id.Btn_Dldetails);

        Btn_Rcdetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String rating=String.valueOf(ratingbar.getRating());

                if(rating.equals("0"))
                {
                    showtoast("Valdation","Please give us rating for better service");
                }
                else  if (isOnline()) {


                    try {
                        moduleType="Rcdetails";

                        final Calendar c = Calendar.getInstance();
                        mYear = c.get(Calendar.YEAR);
                        mMonth = c.get(Calendar.MONTH);
                        mDay = c.get(Calendar.DAY_OF_MONTH);

                        date = mYear + "-" + mMonth+1 +"-" + mDay;

                        if (et_remarks.getText().toString().trim().equals("")) {
                            remarks = "NA";
                        } else {
                            remarks = et_remarks.getText().toString();
                        }

                        UploaFeedback uploaFeedback=new UploaFeedback();
                        uploaFeedback.execute();


                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }



                }

            }
        });

        Btn_Dldetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String rating=String.valueOf(ratingbar.getRating());
                if(rating.equals("0"))
                {
                    showtoast("Valdation","Please give us rating for better service");
                }
                else  if (isOnline()) {


                    try {
                        moduleType = "Dldetails";

                        final Calendar c = Calendar.getInstance();
                        mYear = c.get(Calendar.YEAR);
                        mMonth = c.get(Calendar.MONTH);
                        mDay = c.get(Calendar.DAY_OF_MONTH);

                        date = mYear + "-" + mMonth + 1 + "-" + mDay;

                        if (et_remarks.getText().toString().trim().equals("")) {
                            remarks = "NA";
                        } else {
                            remarks = et_remarks.getText().toString();
                        }

                        UploaFeedback uploaFeedback = new UploaFeedback();
                        uploaFeedback.execute();


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        Btn_Aadhardetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String rating=String.valueOf(ratingbar.getRating());

                if(rating.equals("0"))
                {
                    showtoast("Valdation","Please give us rating for better service");
                }
                else  if (isOnline()) {


                    try {
                        moduleType = "aadhardetails";

                        final Calendar c = Calendar.getInstance();
                        mYear = c.get(Calendar.YEAR);
                        mMonth = c.get(Calendar.MONTH);
                        mDay = c.get(Calendar.DAY_OF_MONTH);

                        date = mYear + "-" + mMonth + 1 + "-" + mDay;

                        if (et_remarks.getText().toString().trim().equals("")) {
                            remarks = "NA";
                        } else {
                            remarks = et_remarks.getText().toString();
                        }

                        UploaFeedback uploaFeedback = new UploaFeedback();
                        uploaFeedback.execute();


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        Btn_overall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String rating=String.valueOf(ratingbar.getRating());
                if(rating.equals("0"))
                {
                    showtoast("Valdation","Please give us rating for better service");
                }
                else  if (isOnline()) {


                    try {
                        moduleType = "Overalldetails";

                        final Calendar c = Calendar.getInstance();
                        mYear = c.get(Calendar.YEAR);
                        mMonth = c.get(Calendar.MONTH);
                        mDay = c.get(Calendar.DAY_OF_MONTH);

                        date = mYear + "-" + mMonth + 1 + "-" + mDay;

                        if (et_remarks.getText().toString().trim().equals("")) {
                            remarks = "NA";
                        } else {
                            remarks = et_remarks.getText().toString();
                        }

                        UploaFeedback uploaFeedback = new UploaFeedback();
                        uploaFeedback.execute();


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        Btn_Pendingchallans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String rating=String.valueOf(ratingbar.getRating());
                if(rating.equals("0"))
                {
                    showtoast("Valdation","Please give us rating for better service");
                }
                else  if (isOnline()) {


                    try {
                        moduleType = "PendingchallansMenu";

                        final Calendar c = Calendar.getInstance();
                        mYear = c.get(Calendar.YEAR);
                        mMonth = c.get(Calendar.MONTH);
                        mDay = c.get(Calendar.DAY_OF_MONTH);

                        date = mYear + "-" + mMonth + 1 + "-" + mDay;

                        if (et_remarks.getText().toString().trim().equals("")) {
                            remarks = "NA";
                        } else {
                            remarks = et_remarks.getText().toString();
                        }

                        UploaFeedback uploaFeedback = new UploaFeedback();
                        uploaFeedback.execute();


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        Btn_Complaints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String rating=String.valueOf(ratingbar.getRating());
                if(rating.equals("0"))
                {
                    showtoast("Valdation","Please give us rating for better service");
                }
                else  if (isOnline()) {


                    try {
                        moduleType = "ComplaintsMenu";

                        final Calendar c = Calendar.getInstance();
                        mYear = c.get(Calendar.YEAR);
                        mMonth = c.get(Calendar.MONTH);
                        mDay = c.get(Calendar.DAY_OF_MONTH);

                        date = mYear + "-" + mMonth + 1 + "-" + mDay;

                        if (et_remarks.getText().toString().trim().equals("")) {
                            remarks = "NA";
                        } else {
                            remarks = et_remarks.getText().toString();
                        }

                        UploaFeedback uploaFeedback = new UploaFeedback();
                        uploaFeedback.execute();


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }


    private void getLocation() {

        try {
            m_locationlistner = (LocationManager)getSystemService(LOCATION_SERVICE);
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

    void showtoast(String title ,String Message)
    {
        LayoutInflater inflator=this.getLayoutInflater();
        View toastlayout=inflator.inflate(R.layout.my_toast,(ViewGroup)findViewById(R.id.toast_root_view));
        TextView toast_header=(TextView)toastlayout.findViewById(R.id.toast_header);
        toast_header.setText(title);
        TextView toast_body=(TextView)toastlayout.findViewById(R.id.toast_body);
        toast_body.setText(Message);
        Toast toast=new Toast(this);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(toast.LENGTH_LONG);
        toast.setView(toastlayout);
        toast.show();
    }

    public class UploaFeedback extends AsyncTask<String, Void, String> {

        ProgressDialog dialog;

        protected void onPreExecute() {
            dialog = new ProgressDialog(Feedback.this);
            dialog.setTitle("Uploading Feedback Details");
            dialog.setMessage("Please wait....");
            dialog.setCancelable(false);
            dialog.show();
        }


        @Override
        protected String doInBackground(String... params) {

            String response = ServiceHelper.UploadComplaintOrFeedback("F",Vehicle_Wallet_Login.username,Vehicle_Wallet_Login.Mobilenum,
                    moduleType,"NA",remarks,"NA","NA",date,"NA","NA", String.valueOf(latitude),String.valueOf(longitude));


            return response;

        }
        @Override
        protected void onPostExecute(final String result) {
            dialog.dismiss();

            if (!result.equalsIgnoreCase("NA")) {

                try {

                    if(result.contains("Your Feedback is failed to Register."))
                    {
                        showtoast("Validation", result);

                    }
                    else if(result.contains("Thank you for your Feedback"))
                    {
                        showtoast("Validation", result);
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {


                                Intent i=new Intent(getApplicationContext(),DashboardActivity.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                startActivity(i);
                               finish();
                            }
                        }, 1000);


                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


        }

    }

    public Boolean isOnline() {
        ConnectivityManager conManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nwInfo = conManager.getActiveNetworkInfo();
        return nwInfo != null;
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            latitude = (float) location.getLatitude();
            longitude = (float) location.getLongitude();
        } else {
            latitude = 0.0;
            longitude = 0.0;
        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

}
