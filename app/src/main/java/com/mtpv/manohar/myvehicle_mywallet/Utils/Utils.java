package com.mtpv.manohar.myvehicle_mywallet.Utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by MANOHAR on 6/28/2017.
 */

public class Utils {


    public boolean Vehilcevalidation(String Validate)
    {

        boolean check = false;
        try{
            Pattern pattern = Pattern.compile("^[A-Z]{2}[ -][0-9]{1,2}(?: [A-Z])?(?: [A-Z]*)? [0-9]{4}$");
            Matcher matcher = pattern.matcher(Validate);
            if(matcher.matches()){
                check = true;
            }else{
                check = false;
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return check;


    }
}
