package com.mtpv.manohar.myvehicle_mywallet;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class DashboardActivity extends AppCompatActivity {


    public int currentimageindex=0;
    ImageView slidingimage;
 //   String usertype=null,Username=null;
    private int[] IMAGE_IDS = {
            R.drawable.fir, R.drawable.sec, R.drawable.fir};
    TextView Tv_user;
    Button Btn_complaints_service,Btn_componpoliceorps,Btn_feedback_componechallan,Btn_reports_complaints,Btn_reg_vehicles,Btn_fake_numbers,Btn_firm_info,
            Btn_violations_payments,Btn_vehicle_wallet,Btn_mydrivers_info,Btn_myvehicle_info;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);


        Tv_user=(TextView)findViewById(R.id.Tv_user);

        Tv_user.setText("Welcome"+" "+Vehicle_Wallet_Login.username);


        Btn_myvehicle_info=(Button)findViewById(R.id.Btn_myvehicle_info);
        Btn_myvehicle_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent i=new Intent(DashboardActivity.this,VehicleDashboard.class);

                startActivity(i);


            }
        });

        Btn_vehicle_wallet=(Button)findViewById(R.id.Btn_vehicle_wallet);

        Btn_vehicle_wallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(DashboardActivity.this,ComplaintsSystemMenu.class);
                startActivity(i);

            }
        });

        Btn_feedback_componechallan=(Button)findViewById(R.id.Btn_feedback_componechallan);

        Btn_feedback_componechallan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i=new Intent(DashboardActivity.this,Complaintsinechallans.class);
                startActivity(i);
            }
        });

        Btn_feedback_componechallan=(Button)findViewById(R.id.Btn_feedback_componechallan);

        Btn_feedback_componechallan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i=new Intent(DashboardActivity.this,Complaintsinechallans.class);
                startActivity(i);

            }
        });

        Btn_componpoliceorps=(Button)findViewById(R.id.Btn_componpoliceorps);

        Btn_componpoliceorps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(DashboardActivity.this,ComplaintOnPolice_PS.class);
                startActivity(i);
            }
        });

        Btn_violations_payments=(Button)findViewById(R.id.Btn_violations_payments);

        Btn_violations_payments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i=new Intent(DashboardActivity.this,Pendindchallans_withTab.class);
                startActivity(i);
            }
        });

        final Handler mHandler = new Handler();

        // Create runnable for posting
        final Runnable mUpdateResults = new Runnable() {
            public void run() {

                try
                {
                    AnimateandSlideShow();
                }catch(Exception e)
                {
                    e.printStackTrace();
                }

            }
        };

        try
        {
            int delay = 100; // delay for 1 sec.

            int period = 2500; // repeat every 2.5 sec.

            Timer timer = new Timer();

            timer.scheduleAtFixedRate(new TimerTask() {

                public void run() {

                    mHandler.post(mUpdateResults);

                }

            }, delay, period);
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    private void AnimateandSlideShow() {


        try
        {
            slidingimage = (ImageView)findViewById(R.id.ImageView3_Left);
            slidingimage.setImageResource(IMAGE_IDS[currentimageindex%IMAGE_IDS.length]);

            currentimageindex++;

            Animation rotateimage = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);


            slidingimage.startAnimation(rotateimage);
        }catch(Exception e)
        {
            e.printStackTrace();
        }


    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Really Exit?")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {

                        Intent i=new Intent(DashboardActivity.this,Vehicle_Wallet_Login.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        startActivity(i);
                        finish();
                    }
                }).create().show();
    }

}
