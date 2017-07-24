package com.mtpv.manohar.myvehicle_mywallet;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

public class Pendindchallans_withTab extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;

    public  static  String Tabflag=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        setContentView(R.layout.activity_pendindchallans_with_tab);


        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);



        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position) {
                case 0:
                    Pendingchallansbyregno pendingchallansbyregno=new Pendingchallansbyregno();
                    Tabflag="RegNo";
                    return pendingchallansbyregno;
                case 1:
                   PendingChallansbyDlno pendingChallansbyDlno=new PendingChallansbyDlno();
                    Tabflag="DLNO";

                    return pendingChallansbyDlno;
                case 2:
                  PendingChallansbyAadhar pendingChallansbyAadhar=new PendingChallansbyAadhar();
                    Tabflag="Aadhar";

                    return pendingChallansbyAadhar;

            }

            return null;
        }
        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Vehicle Registration No";
                case 1:
                    return "Driving License No";
                case 2:
                    return "Aadhar No";
            }
            return null;
        }
    }



}
