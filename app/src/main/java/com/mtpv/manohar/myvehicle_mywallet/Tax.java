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
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.mtpv.manohar.myvehicle_mywallet.Services.ServiceHelper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;


public class Tax extends Fragment {

    int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    ImageView img_photo;
    Animation anim;
    public static byte[] byteArray;
    Button btn_DatePicker;
    private int mYear, mMonth, mDay;
    private String userChoosenTask;
    ContentValues values;
    Uri imageUri;

    EditText et_vehicleNo, et_issuingauthority, et_remarks;
    String regno=null,issueauth=null,remarks=null,imgString = null,validupto=null;
    View rootView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_tax, container, false);
        img_photo = (ImageView) rootView.findViewById(R.id.img_photo);
        btn_DatePicker = (Button) rootView.findViewById(R.id.btn_DatePicker);

        et_vehicleNo = (EditText) rootView.findViewById(R.id.et_vehicleNo);
        et_issuingauthority = (EditText) rootView.findViewById(R.id.et_issuingauthority);
        et_remarks = (EditText) rootView.findViewById(R.id.et_remarks);

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
                settingsDialog.cancel();

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

        Button submit = (Button)rootView.findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_vehicleNo.getText().toString().trim().equals("")) {
                    showtoast("Validation", "Please fill the Regno  to continue");
                    et_vehicleNo.requestFocus();
                } else if (et_issuingauthority.getText().toString().trim().equals("")) {
                    showtoast("Validation", "Please fill the Issuing Authority.  to continue");
                    et_issuingauthority.requestFocus();
                }
                else if (btn_DatePicker.getText().toString().equalsIgnoreCase("--Select Validity Date--") || btn_DatePicker.getText().toString().trim().equals("")) {
                    showtoast("Validation", "Please select  validity date to continue");

                }
                else  if(imgString != null && !imgString.isEmpty())

                {
                    if (isOnline()) {

                        try {
                            regno = et_vehicleNo.getText().toString();
                            //insNo = et_insuranceno.getText().toString();
                            issueauth = et_issuingauthority.getText().toString();
                            validupto = btn_DatePicker.getText().toString();
                        }catch (Exception e)
                        {
                            e.printStackTrace();
                        }

                        if (et_remarks.getText().toString().trim().equals("")) {
                            remarks = "NA";
                        } else {
                            remarks = et_remarks.getText().toString();
                        }


                        UploadTaxDetasils uploadTaxDetasils = new UploadTaxDetasils();
                        uploadTaxDetasils.execute();


                    } else {
                        showtoast("Validation", "Please check your network connection!");

                    }

                }  else {
                    showtoast("Validation", "Please take a document picture which you need to upload");

                }
            }
        });

        Button cancel = (Button) rootView.findViewById(R.id.cancel);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent i=new Intent(getActivity(),DashboardActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(i);
                getActivity().finish();

            }
        });


        return rootView;
    }


    public class UploadTaxDetasils extends AsyncTask<String, Void, String> {

        ProgressDialog dialog;

        protected void onPreExecute() {
            dialog = new ProgressDialog(getActivity());
            dialog.setTitle("Uploading Tax Details");
            dialog.setMessage("Please wait....");
            dialog.setCancelable(false);
            dialog.show();
        }


        @Override
        protected String doInBackground(String... params) {

            String response = ServiceHelper.TaxDetailsUpload(regno.toUpperCase(), "NA", issueauth.toUpperCase(), validupto.toUpperCase(), remarks.toUpperCase(), imgString, "NA", Vehicle_Wallet_Login.Mobilenum, Vehicle_Wallet_Login.usertype.toUpperCase());


            return response;

        }

        @Override
        protected void onPostExecute(String result) {
            dialog.dismiss();

            if (!result.equalsIgnoreCase("NA")) {

                try {

                    if(result.equalsIgnoreCase("Your Complaint is failed to Register. "))
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

    void showtoast(String title, String Message) {
        LayoutInflater inflator = getActivity().getLayoutInflater();
        View toastlayout = inflator.inflate(R.layout.my_toast, (ViewGroup)getActivity().findViewById(R.id.toast_root_view));
        TextView toast_header = (TextView) toastlayout.findViewById(R.id.toast_header);
        toast_header.setText(title);
        TextView toast_body = (TextView) toastlayout.findViewById(R.id.toast_body);
        toast_body.setText(Message);
        Toast toast = new Toast(getActivity());
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(toast.LENGTH_LONG);
        toast.setView(toastlayout);
        toast.show();
    }

    public Boolean isOnline() {
        ConnectivityManager conManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nwInfo = conManager.getActiveNetworkInfo();
        return nwInfo != null;
    }
}
