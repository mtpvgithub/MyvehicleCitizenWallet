package com.mtpv.manohar.myvehicle_mywallet;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseIntArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.mtpv.manohar.myvehicle_mywallet.Services.ServiceHelper;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.fabric.sdk.android.Fabric;

public class Vehicle_Wallet_Login extends AppCompatActivity {

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
    private static final int REQUEST_PERMISSIONS = 20;
    private SparseIntArray mErrorString;
    EditText edit_id,edit_pwd;
    String Username=null;
    String Password=null;
    public static String alert_string = null ;

    boolean doubleBackToExitPressedOnce = false;

    public static String Mobilenum=null;
    public static String username=null;
    public static String usertype=null;

    LinearLayout layout_do_not_have_an_account;
    private FirebaseAnalytics mFirebaseAnalytics;
    AVLoadingIndicatorView progIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(BuildConfig.DEBUG)
       {
            Fabric.with(this, new Crashlytics());

        }

        // Obtain the FirebaseAnalytics instance.
      //  mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_vehiclelogin);


        layout_do_not_have_an_account = (LinearLayout)findViewById(R.id.layout_do_not_have_an_account);
        RelativeLayout topPanel=(RelativeLayout)findViewById(R.id.topPanel);

        topPanel.getBackground().setAlpha(50);

        setContentView(R.layout.activity_vehiclelogin);

        edit_id=(EditText)findViewById(R.id.edit_id);
        edit_pwd=(EditText)findViewById(R.id.edit_pwd);
        progIndicator=(AVLoadingIndicatorView)findViewById(R.id.progIndicator);


        edit_id.setText("Manohar");
        edit_pwd.setText("12345");



        mErrorString = new SparseIntArray();


        if (Build.VERSION.SDK_INT > 22 && !hasPermissions(requiredPermissions)) {

            Vehicle_Wallet_Login.this.requestAppPermissions(new
                            String[]{ Manifest.permission.READ_PHONE_STATE,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_NETWORK_STATE,
                            Manifest.permission.ACCESS_WIFI_STATE,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.INTERNET,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.INSTALL_SHORTCUT}, R.string
                            .runtime_permissions_txt
                    , REQUEST_PERMISSIONS);

        }

        Button button_login=(Button)findViewById(R.id.button_login);

        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


               if(edit_id.getText().toString().trim().equals("") && edit_pwd.getText().toString().trim().equals(""))
               {
                   showtoast("Validation","Please fill the Fields  to continue");
                   edit_id.requestFocus();
               }

               else if (edit_id.getText().toString().trim().equals("")) {
//                    edit_id.setError(Html.fromHtml("<font color='red'>Please Enter Id!</font>"));

                    showtoast("Validation","Please fill the user id to continue");
                    edit_id.requestFocus();
                }
                else  if (edit_pwd.getText().toString().trim().equals("")) {
                  //  edit_id.setError(Html.fromHtml("<font color='red'>Please Enter Id!</font>"));
                    showtoast("Validation","Please fill the Password to continue");

                    edit_id.requestFocus();
                }

                else {
                    if (isOnline()) {

                        Username=edit_id.getText().toString();
                        Password=edit_pwd.getText().toString();

                        new Login().execute();

                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable()
                        {
                            @Override
                            public void run() {
                                if ( new Login().getStatus() == AsyncTask.Status.RUNNING )
                                    new Login().cancel(true);
                            }
                        }, 30000 );

                    } else {
                        showtoast("Validation","Please check your network connection!");
                    }
                }


            }
        });



        TextView textView_do_not_have_an_account=(TextView)findViewById(R.id.textView_do_not_have_an_account);

        textView_do_not_have_an_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i=new Intent(Vehicle_Wallet_Login.this, RegisterActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
              //  finish();
            }
        });

    }

    public void requestAppPermissions(final String[] requestedPermissions,
                                      final int stringId, final int requestCode) {
        mErrorString.put(requestCode, stringId);
        int permissionCheck = PackageManager.PERMISSION_GRANTED;
        boolean shouldShowRequestPermissionRationale = false;
        for (String permission : requestedPermissions) {
            permissionCheck = permissionCheck + ContextCompat.checkSelfPermission(this, permission);
            shouldShowRequestPermissionRationale = shouldShowRequestPermissionRationale || ActivityCompat.shouldShowRequestPermissionRationale(this, permission);
        }
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale) {
                Snackbar.make(findViewById(android.R.id.content), stringId,
                        Snackbar.LENGTH_INDEFINITE).setAction("GRANT",
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ActivityCompat.requestPermissions(Vehicle_Wallet_Login.this, requestedPermissions, requestCode);
                            }
                        }).show();
            } else {
                ActivityCompat.requestPermissions(this, requestedPermissions, requestCode);
            }
        } else {
            onPermissionsGranted(requestCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        int permissionCheck = PackageManager.PERMISSION_GRANTED;
        for (int permission : grantResults) {
            permissionCheck = permissionCheck + permission;
        }
        if ((grantResults.length > 0) && permissionCheck == PackageManager.PERMISSION_GRANTED) {
            onPermissionsGranted(requestCode);
        } else {
            Snackbar.make(findViewById(android.R.id.content), mErrorString.get(requestCode),
                    Snackbar.LENGTH_INDEFINITE).setAction("ENABLE",
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent();
                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            intent.addCategory(Intent.CATEGORY_DEFAULT);
                            intent.setData(Uri.parse("package:" + getPackageName()));
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                            startActivity(intent);
                        }
                    }).show();
        }
    }

    public void onPermissionsGranted(final int requestCode) {
        Toast.makeText(this, "Permissions Received.", Toast.LENGTH_LONG).show();
    }

    public boolean hasPermissions(String... permissions) {
        for (String permission : permissions)
            if (PackageManager.PERMISSION_GRANTED != checkCallingOrSelfPermission(permission))
                return false;
        return true;
    }


    public class Login extends AsyncTask<String, Void, String> {
      //ProgressDialog dialog;

        protected void onPreExecute() {
//            dialog = new ProgressDialog(Vehicle_Wallet_Login.this);
//            dialog.setTitle("Authenticating User");
//            dialog.setMessage("Please wait....");
//            dialog.setCancelable(false);
//            dialog.show();

            startAnim();
        }


        @Override
        protected String doInBackground(String... params) {


            String response = ServiceHelper.login(Username, Password);


            return response;

        }
        @Override
        protected void onPostExecute(String result) {
          //  dialog.dismiss();

            stopAnim();

            if(!result.equalsIgnoreCase("NA")) {


                try {
                    JSONObject jsonObj = new JSONObject(result);
                    // Getting JSON Array node
                    JSONArray jsonArray = jsonObj.getJSONArray("Login Details");
                    // looping through All Witness
                    for (int i = 0, size = jsonArray.length(); i < size; i++) {
                        JSONObject c = jsonArray.getJSONObject(i);
                        //witness_id_code
                        username = c.getString("User Name");
                        usertype = c.getString("User Type");
                        Mobilenum=c.getString("MobileNo");

                    }

                    Intent i = new Intent(Vehicle_Wallet_Login.this, DashboardActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else
            {
                showtoast("Service Validation","Please check your credentials and try again!");
            }
            }
        }

    void showtoast(String title , String Message)
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



//    public void showMessage(String title, String message) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setCancelable(true);
//        builder.setTitle(title);
//        builder.setMessage(message);
//        builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int which) {
//
//
//            }
//        });
//        builder.show();
//    }


    public Boolean isOnline() {
        ConnectivityManager conManager = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nwInfo = conManager.getActiveNetworkInfo();
        return nwInfo != null;
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        alert_string = "loginback" ;

        this.doubleBackToExitPressedOnce = true;
        showtoast("Validation","Please click BACK again to exit");

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    void startAnim() {
// progIndicator.show();
        progIndicator.smoothToShow();
    }
    public void stopAnim() {
//progIndicator.hide();
        progIndicator.smoothToHide();
    }

}

