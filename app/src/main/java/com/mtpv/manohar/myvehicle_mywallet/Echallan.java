package com.mtpv.manohar.myvehicle_mywallet;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.mtpv.manohar.myvehicle_mywallet.Services.ServiceHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;

public class Echallan extends Fragment implements AdapterView.OnItemClickListener,LocationListener {

    int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    ImageView img_photo;
    Animation anim;
    String imgString = null;
    public static byte[] byteArray;
    Button btn_DatePicker,submit,cancel;
    private int mYear, mMonth, mDay;
    private String userChoosenTask;
    ContentValues values;
    Uri imageUri;
    static double latitude = 0.0;
    static double longitude = 0.0;
    boolean isGPSEnabled = false;
    // flag for network status
    boolean isNetworkEnabled = false;
    boolean canGetLocation = false;
    private static final String LOG_TAG = "SamplePlacesApp";

    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";
    LocationManager m_locationlistner;
    Location location;

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters
    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute
    //------------ make your specific key ------------
    //private static final String API_KEY = "AIzaSyAU9ShujnIg3IDQxtPr7Q1qOvFVdwNmWc4";
    private static final String API_KEY = "AIzaSyDVFlMwuv5ctfKc-75u4rT3Z3vzw_OjTiw";

    String[] SelectCategories={"SELECT CATEGORY","Traffic Violations","Crime Violations","Violations By Police","She Team Complaint"};

    String[] SelectCity={"SELECT CITY","Hyderabad Police Commissionarate","Cyberabad Police Commissionarate","Rachakonda Police Commissionarate","Warangal Police Commissionarate",
            "Siddipet Police Commissionarate","Karimnagar Police Commissionarate","Ramagundam Police Commissionarate","Nizamabad Police Commissionarate"};

    String[] SelectArea={"SELECT AREA","Abids","Yousuguda","Ameerpet","Panjagutta"};

    String regno=null,fdbkORcmplnt=null,moduleType=null,remarks=null,evidenceImg=null,dtNtime=null,city=null,area=null,lat=null,lang=null;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_echallan, container, false);
        img_photo = (ImageView) rootView.findViewById(R.id.img_photo);
        btn_DatePicker = (Button) rootView.findViewById(R.id.btn_DatePicker);

        final EditText et_vehicleNo=(EditText)rootView.findViewById(R.id.et_vehicleNo);
        final TextInputLayout textInputLayout1=(TextInputLayout)rootView.findViewById(R.id.textInputLayout1);
        final EditText et_remarks=(EditText)rootView.findViewById(R.id.et_remarks);
        final TextInputLayout textInputLayout4=(TextInputLayout)rootView.findViewById(R.id.textInputLayout4);

        textInputLayout1.setVisibility(View.INVISIBLE);
        et_vehicleNo.setVisibility(View.INVISIBLE);

        final Spinner spinner_category = (Spinner)rootView.findViewById(R.id.spinner_category);


        ArrayAdapter categoryadapter = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,SelectCategories);
        categoryadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_category.setAdapter(categoryadapter);


        final Spinner spinner_city = (Spinner)rootView.findViewById(R.id.spinner_city);
        // spinner_city.setOnItemSelectedListener(this);

        ArrayAdapter cityadapter = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,SelectCity);
        cityadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_city.setAdapter(cityadapter);


        spinner_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (SelectCategories[position].toString().equalsIgnoreCase("SELECT CATEGORY"))
                {

                    showtoast("Validation","Please Select Any Category to Continue");

                }
                else if(SelectCategories[position].toString().equalsIgnoreCase("Traffic Violations"))
                {
                    et_vehicleNo.setVisibility(View.VISIBLE);
                    textInputLayout1.setVisibility(View.VISIBLE);
                    moduleType=SelectCategories[position].toString();
                }
                else
                {
                    moduleType=SelectCategories[position].toString();
                }

//                else if(SelectCategories[position].toString().equalsIgnoreCase("Crime Violations"))
//                {
//                    et_vehicleNo.setVisibility(View.INVISIBLE);
//                    textInputLayout1.setVisibility(View.INVISIBLE);
//                    moduleType=SelectCategories[position].toString();
//
//                }
//                else if(SelectCategories[position].toString().equalsIgnoreCase("Violations By Police"))
//                {
//                    et_vehicleNo.setVisibility(View.INVISIBLE);
//                    textInputLayout1.setVisibility(View.INVISIBLE);
//                    //textInputLayout1.setHint("Enter Police Cadre");
//                    moduleType=SelectCategories[position].toString();
//
//                }
//                else if(SelectCategories[position].toString().equalsIgnoreCase("She Team Complaint"))
//                {
//                    et_vehicleNo.setVisibility(View.INVISIBLE);
//                    textInputLayout1.setVisibility(View.INVISIBLE);
//                    moduleType=SelectCategories[position].toString();
//
//                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        spinner_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (SelectCity[position].toString().equalsIgnoreCase("SELECT CITY"))
                {

                    showtoast("Validation","Please Select Any Category to Continue");

                }
                else
                {


                    city=SelectCity[position].toString();

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        final AutoCompleteTextView autoCompleteTextView_Area = (AutoCompleteTextView)rootView.findViewById(R.id.autoCompleteTextView_Area);

        autoCompleteTextView_Area.setAdapter(new GooglePlacesAutocompleteAdapter(getActivity(), R.layout.list_item));
        autoCompleteTextView_Area.setOnItemClickListener(this);

        final ImageView img_click = (ImageView) rootView.findViewById(R.id.img_click);
        img_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });


        btn_DatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                {
                    // Get Current Date
                    final Calendar c = Calendar.getInstance();
                    mYear = c.get(Calendar.YEAR);
                    mMonth = c.get(Calendar.MONTH);
                    mDay = c.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                            new DatePickerDialog.OnDateSetListener() {

                                @Override
                                public void onDateSet(DatePicker view, int year,
                                                      int monthOfYear, int dayOfMonth) {
                                    btn_DatePicker.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                }
                            }, mYear, mMonth, mDay);
                    datePickerDialog.show();
                }
            }
        });

        img_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final  Dialog settingsDialog = new Dialog(getActivity());

                LayoutInflater inflater = getActivity().getLayoutInflater();
                View newView = (View) inflater.inflate(R.layout.image_layout, null);
                settingsDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                settingsDialog.setContentView(newView);
                settingsDialog.setCancelable(false);

                Button Btn_close=(Button) newView.findViewById(R.id.Btn_close);

                Btn_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        settingsDialog.cancel();
                    }
                });

                ImageView iv = (ImageView) newView.findViewById(R.id.dialogimage);
                Bitmap bm = ((BitmapDrawable) img_photo.getDrawable()).getBitmap();
                iv.setImageBitmap(bm);
                settingsDialog.show();
            }
        });

        cancel=(Button)rootView.findViewById(R.id.cancel);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getActivity(),DashboardActivity.class);
                getActivity().finish();
            }
        });

        submit=(Button)rootView.findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //  location=autoCompleteTextView_Area.getText().toString();

                if(moduleType.toString().trim().equals("")||moduleType.toString().trim().equals(null))
                {
                    showtoast("Validation","Please Select Category to continue");
                    spinner_category.requestFocus();
                }
                else if(city.toString().trim().equals("")||city.toString().trim().equals(null))
                {
                    showtoast("Validation","Please Select City to continue");
                    spinner_city.requestFocus();
                }
                else if(autoCompleteTextView_Area.getText().toString().trim().equals("")||autoCompleteTextView_Area.getText().toString().trim().equals(null))
                {
                    showtoast("Validation","Please Select Area to continue");
                    autoCompleteTextView_Area.requestFocus();
                }

                else if(moduleType.equalsIgnoreCase("Traffic Violations")&& et_vehicleNo.getText().toString().trim().equals(""))
                {
                    showtoast("Validation","Please Enter Vehicle Number  to continue");
                    et_vehicleNo.requestFocus();
                }

                else if(btn_DatePicker.getText().toString().equalsIgnoreCase("--Select Insurance Validity Date--")|| btn_DatePicker.getText().toString().trim().equals(""))
                {
                    showtoast("Validation","Please select insurance validity date to continue");

                }
                else  if(imgString != null && !imgString.isEmpty()) {


                    if (isOnline()) {
                        try {

                            if (et_vehicleNo.getText().toString().trim().equals("")) {
                                regno = "NA";
                            }
                            else {
                                regno = et_vehicleNo.getText().toString();

                            }

                            dtNtime = btn_DatePicker.getText().toString();
                            area=autoCompleteTextView_Area.getText().toString();
                            fdbkORcmplnt="C";


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (et_remarks.getText().toString().trim().equals("")) {
                            remarks = "NA";
                        } else {
                            remarks = et_remarks.getText().toString();
                        }

                        UploaGeneralCompliantdetails uploaGeneralCompliantdetails=new UploaGeneralCompliantdetails();
                        uploaGeneralCompliantdetails.execute();



                    }else
                    {
                        showtoast("Validation","Please Check Your Internet Connection");
                    }

                }else
                {
                    showtoast("Validation","Please take a  picture which you need to upload");
                }
            }
        });



        return rootView;
    }

    private void selectImage() {

        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(getActivity());

                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    if (result)
                        cameraIntent();

                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    if (result)
                        galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if (userChoosenTask.equals("Choose from Library"))
                        galleryIntent();
                } else {
                    //code for deny
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                //  onCaptureImageResult(data);

                try {
                    Bitmap thumbnail;
                    thumbnail = MediaStore.Images.Media.getBitmap(
                            getActivity().getContentResolver(), imageUri);
                    img_photo.setImageBitmap(thumbnail);
                    String imageurl = getRealPathFromURI(imageUri);


                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    thumbnail.compress(Bitmap.CompressFormat.JPEG, 10,
                            bytes);
                    byteArray = bytes.toByteArray();

                    try {
                        if (byteArray != null) {
                            imgString = Base64.encodeToString(byteArray,
                                    Base64.NO_WRAP);
                        } else {
                            imgString = null;
                        }
                        Log.i("imgString ::", "" + imgString);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
        }
    }



    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());


                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.JPEG, 10,
                        bytes);
                byteArray = bytes.toByteArray();

                try {
                    if (byteArray != null) {
                        imgString = Base64.encodeToString(byteArray,
                                Base64.NO_WRAP);
                    } else {
                        imgString = null;
                    }
                    Log.i("imgString ::", "" + imgString);
                } catch (Exception e) {
                    e.printStackTrace();
                }



            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        img_photo.setImageBitmap(bm);
    }

    private void cameraIntent() {
        values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
        imageUri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
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
                    m_locationlistner.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
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
                        m_locationlistner.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
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





    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        String str = (String) adapterView.getItemAtPosition(position);
        Toast.makeText(getActivity(), str, Toast.LENGTH_SHORT).show();
    }

    public static ArrayList<String> autocomplete(String input) {
        ArrayList<String> resultList = null;

        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
            sb.append("?key=" + API_KEY);
            sb.append("&components=country:in");
            sb.append("&input=" + URLEncoder.encode(input, "utf8"));

            URL url = new URL(sb.toString());

            System.out.println("URL: "+url);
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error processing Places API URL", e);
            return resultList;
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error connecting to Places API", e);
            return resultList;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        try {

            // Create a JSON object hierarchy from the results
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

            // Extract the Place descriptions from the results
            resultList = new ArrayList<String>(predsJsonArray.length());
            for (int i = 0; i < predsJsonArray.length(); i++) {
                System.out.println(predsJsonArray.getJSONObject(i).getString("description"));
                System.out.println("============================================================");
                resultList.add(predsJsonArray.getJSONObject(i).getString("description"));
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Cannot process JSON results", e);
        }

        return resultList;
    }

    class GooglePlacesAutocompleteAdapter extends ArrayAdapter<String> implements Filterable {
        private ArrayList<String> resultList;

        public GooglePlacesAutocompleteAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        @Override
        public int getCount() {
            return resultList.size();
        }

        @Override
        public String getItem(int index) {
            return resultList.get(index);
        }

        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();
                    if (constraint != null) {
                        // Retrieve the autocomplete results.
                        resultList = autocomplete(constraint.toString());

                        // Assign the data to the FilterResults
                        filterResults.values = resultList;
                        filterResults.count = resultList.size();
                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    if (results != null && results.count > 0) {
                        notifyDataSetChanged();
                    } else {
                        notifyDataSetInvalidated();
                    }
                }
            };
            return filter;
        }
    }

    void showtoast(String title ,String Message)
    {
        LayoutInflater inflator=getActivity().getLayoutInflater();
        View toastlayout=inflator.inflate(R.layout.my_toast,(ViewGroup)getActivity().findViewById(R.id.toast_root_view));
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

    public class UploaGeneralCompliantdetails extends AsyncTask<String, Void, String> {

        ProgressDialog dialog;

        protected void onPreExecute() {
            dialog = new ProgressDialog(getActivity());
            dialog.setTitle("Uploading Complaint Details");
            dialog.setMessage("Please wait....");
            dialog.setCancelable(false);
            dialog.show();
        }


        @Override
        protected String doInBackground(String... params) {

            String response = ServiceHelper.UploadComplaintOrFeedback(fdbkORcmplnt,Vehicle_Wallet_Login.username,Vehicle_Wallet_Login.Mobilenum,
                    moduleType,"NA",remarks,regno,imgString,dtNtime,city,area, String.valueOf(latitude),String.valueOf(longitude));


            return response;

        }
        @Override
        protected void onPostExecute(final String result) {
            dialog.dismiss();

            if (!result.equalsIgnoreCase("NA")) {

                try {

                    if(result.contains("Your Complaint is failed to Register. "))
                    {
                        showtoast("Validation", result);

                    }
                    else if(result.contains("Your Complaint is Registered Successfully and your Complaint NO"))
                    {
                        showtoast("Validation", result);
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                Intent i=new Intent(getActivity(),DashboardActivity.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                startActivity(i);
                                getActivity().finish();
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
        ConnectivityManager conManager = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
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