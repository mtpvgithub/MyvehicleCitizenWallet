package com.mtpv.manohar.myvehicle_mywallet;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RelativeLayout;

import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.util.Calendar;

/**
 * Created by MANOHAR on 6/30/2017.
 */

public class SoldOut_Activity extends AppCompatActivity {
    String[] SPINNERLIST = {"2-WHEELER", "3-WHEELER", "4-WHEELER", "LORRY/TRUCK","TRACTOR/ROAD ROLLER"};

    Button btn_DatePicker;
    private int mYear, mMonth, mDay;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soldout);


        btn_DatePicker = (Button)findViewById(R.id.btn_DatePicker);
        btn_DatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                {

                    // Get Current Date
                    final Calendar c = Calendar.getInstance();
                    mYear = c.get(Calendar.YEAR);
                    mMonth = c.get(Calendar.MONTH);
                    mDay = c.get(Calendar.DAY_OF_MONTH);


                    DatePickerDialog datePickerDialog = new DatePickerDialog(SoldOut_Activity.this,
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

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, SPINNERLIST);
        MaterialBetterSpinner materialDesignSpinner = (MaterialBetterSpinner)
                findViewById(R.id.android_material_design_spinner);
        materialDesignSpinner.setAdapter(arrayAdapter);
    }
}
