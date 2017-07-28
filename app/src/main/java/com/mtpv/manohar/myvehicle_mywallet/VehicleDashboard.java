package com.mtpv.manohar.myvehicle_mywallet;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;



public class VehicleDashboard extends AppCompatActivity {

    Button Btn_listmyvehicles,Btn_myvehpurchases,Btn_soldout,Btn_addresschange,Btn_Novehicle,Btn_pendingchallans,Btn_reg_vehicles,Btn_reports_complaints;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicledashboard);

        Btn_listmyvehicles=(Button)findViewById(R.id.Btn_listmyvehicles);

        Btn_listmyvehicles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i=new Intent(VehicleDashboard.this,ListofVehicles_Activity.class);

                startActivity(i);

            }
        });

        Btn_myvehpurchases=(Button)findViewById(R.id.Btn_myvehpurchases);

        Btn_myvehpurchases.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(VehicleDashboard.this,MyVehicle_Purchases.class);
                startActivity(i);
            }
        });

        Btn_Novehicle=(Button)findViewById(R.id.Btn_Novehicle);

        Btn_Novehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(VehicleDashboard.this,NoVehicle_Activity.class);

                startActivity(i);
            }
        });

        Btn_soldout=(Button)findViewById(R.id.Btn_soldout);

        Btn_soldout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(VehicleDashboard.this,SoldOut_Activity.class);
               startActivity(i);
            }
        });

        Btn_addresschange=(Button)findViewById(R.id.Btn_addresschange);

        Btn_addresschange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(VehicleDashboard.this,ChangeofAddress_Activity.class);
                startActivity(i);
            }
        });

        Btn_pendingchallans=(Button)findViewById(R.id.Btn_pendingchallans);

        Btn_pendingchallans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(VehicleDashboard.this,Pendindchallans_withTab.class);
                startActivity(i);
            }
        });

        Btn_reports_complaints=(Button)findViewById(R.id.Btn_reports_complaints);

        Btn_reports_complaints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i=new Intent(VehicleDashboard.this, ReportComplaint.class);
                startActivity(i);
            }
        });


    }
}
