package com.mtpv.manohar.myvehicle_mywallet.Services;

import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class ServiceHelper {

    DatabaseHelper dbh;

    private static final String URL = "http://125.16.1.70:8080/MyVehicleWallet/services/MyVehicleWalletImpl?wsdl";

    private static String GET_CAPTCHA = "sendCAPTCHA";

    private static String SEND_OTP = "sendOTP";

    private static String Verify_OTP = "sendOTP";

    private static String Authenticate_User = "loginUser";


    private static String GET_VIOLATIONS_BY_AADHAR = "getViolationsByAadhaar";

    private static String GET_VIOLATIONS_BY_DL = "getViolationsByDL";

    private static String GET_VIOLATIONS_BY_Regn = "getViolationsByRegn";

    private static String USER_REGISTRATION = "userRegistration";

    private static String GET_VEHICLE_DETAILS = "getVehicleDetails";

    private static String GET_DL_DETAILS = "getDLDetails";

    private static String GET_AADHAR_DETAILS = "getaadhaarDetails";

    private static String GET_Mobile_DETAILS = "getMobileDetails";

    private static String Upload_Insurance_Details = "upLoadInsurance";

    private static String Upload_permit_Details = "upLoadPermit";

    private static String Upload_Tax_Details = "upLoadTax";

    private static String Upload_Fitness_Details = "upLoadFitness";

    private static String Upload_CompliantOr_Feedback = "upLoadFeedbacknCmplnt";




    private static String NameSPACE = "http://service.mother.com";

    public static String Opdata_Chalana = null, dl_Response = null, otp_sent_resp = null, otp_verify_resp = null,
            aadhaar_response = null, forgot_paswrd_resp = null, captch_resp = null;

    //private static final String SOAP_ACTION = NameSPACE + METHOD_NAME;

    private static final String SOAP_ACTION_USERREG = "http://service.mother.com/userRegistration";


    public static void GetViolationsbyDL(String licenCENo, String usertype, String captcha, String Mobilenum) {
        try {
            SoapObject request = new SoapObject(NameSPACE, GET_VIOLATIONS_BY_DL);
            request.addProperty("regnNo", licenCENo);
            request.addProperty("userType", usertype);
            request.addProperty("capTcha", captcha);
            request.addProperty("mobileNo", Mobilenum);


            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
            androidHttpTransport.call(NameSPACE + GET_VIOLATIONS_BY_DL, envelope);
            Object result = envelope.getResponse();

            if (result.toString() != null && !result.toString().equals("") || !result.toString().equals("anyType{}")
                    || !result.toString().equals("NA") || !result.toString().equals("0")) {
                dl_Response = result.toString();
            }

            Log.i("dl_Response :::", "" + dl_Response);
        } catch (Exception E) {
            E.printStackTrace();
            dl_Response = "0";
        }
    }

    public static void captchaService(String User_name, String imei_no, String current_date, String Mobilenum) {

        try {
            SoapObject request = new SoapObject(NameSPACE, GET_CAPTCHA);
            request.addProperty("userName", User_name);
            request.addProperty("iMEI", imei_no);
            request.addProperty("date", current_date);
            request.addProperty("mobileNo", Mobilenum);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            //need to add url
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
            androidHttpTransport.call(NameSPACE + GET_CAPTCHA, envelope);
            Object result = envelope.getResponse();


            if (result.toString() != null && !result.toString().equals("") || !result.toString().equals("anyType{}")
                    || !result.toString().equals("NA") || !result.toString().equals("0")) {
                captch_resp = result.toString();
            }


        } catch (Exception E) {
            E.printStackTrace();
            captch_resp = "0";
        }
    }

    public static void getViolationsByAadhaarDetails(String aadhar_no, String usertype, String captcha, String Mobilenum) {
        try {
            SoapObject request = new SoapObject(NameSPACE, GET_VIOLATIONS_BY_AADHAR);
            request.addProperty("regnNo", aadhar_no);
            request.addProperty("userType", usertype);
            request.addProperty("capTcha", captcha);
            request.addProperty("mobileNo", Mobilenum);


            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
            androidHttpTransport.call(NameSPACE + GET_VIOLATIONS_BY_AADHAR, envelope);
            Object result = envelope.getResponse();

            if (result.toString() != null && !result.toString().equals("") || !result.toString().equals("anyType{}")
                    || !result.toString().equals("NA") || !result.toString().equals("0")) {
                aadhaar_response = result.toString();
            }

            Log.i("aadhaar_response :::", "" + aadhaar_response);
        } catch (Exception E) {
            E.printStackTrace();
            aadhaar_response = "0";
        }
    }

    public static String vehicleDetails(String regnNo, String Usertype) {
        try {
            SoapObject request = new SoapObject(NameSPACE, GET_VEHICLE_DETAILS);
            request.addProperty("regnNo", regnNo);
            request.addProperty("userType", Usertype);

            Log.i("request--->", "" + request);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            //need to add url
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
            //Log.i("URL ::::", ""+Public_Acitivity.wifi_URL);
            Log.i("Request ::::", "" + request);
            androidHttpTransport.call(NameSPACE + GET_VEHICLE_DETAILS, envelope);
            Object result = envelope.getResponse();

            if (result.toString() != null && !result.toString().equals("") || !result.toString().equals("anyType{}")
                    || !result.toString().equals("NA") || !result.toString().equals("0")) {
                Opdata_Chalana = result.toString();
            }
            Log.i("Opdata_Chalana ::::", "" + Opdata_Chalana);

        } catch (Exception E) {
            E.printStackTrace();
            Opdata_Chalana = "0";
        }

        return Opdata_Chalana;
    }


    public static String InsuanceDetailsUpload(String regnNo, String insuNo, String insCmpnyName,
                                               String validUptoDt, String remarks, String insDoc1,
                                               String insDoc2, String mobileNo, String userType) {
        try {
            SoapObject request = new SoapObject(NameSPACE, Upload_Insurance_Details);
            request.addProperty("regnNo", regnNo);
            request.addProperty("insuNo", insuNo);
            request.addProperty("insCmpnyName", insCmpnyName);
            request.addProperty("validUptoDt", validUptoDt);
            request.addProperty("remarks", remarks);
            request.addProperty("insDoc1", insDoc1);
            request.addProperty("insDoc2", insDoc2);
            request.addProperty("mobileNo", mobileNo);
            request.addProperty("userType", userType);


            Log.i("request--->", "" + request);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            //need to add url
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
            Log.i("Request ::::", "" + request);
            androidHttpTransport.call(NameSPACE + Upload_Insurance_Details, envelope);
            Object result = envelope.getResponse();

            if (result.toString() != null && !result.toString().equals("") || !result.toString().equals("anyType{}")
                    || !result.toString().equals("NA") || !result.toString().equals("0")) {
                Opdata_Chalana = result.toString();
            }
            Log.i("Opdata_Chalana ::::", "" + Opdata_Chalana);

        } catch (Exception E) {
            E.printStackTrace();
            Opdata_Chalana = "0";
        }

        return Opdata_Chalana;
    }

    public static String TaxDetailsUpload(String regnNo, String insuNo, String insCmpnyName,
                                          String validUptoDt, String remarks, String insDoc1,
                                          String insDoc2, String mobileNo, String userType) {
        try {
            SoapObject request = new SoapObject(NameSPACE, Upload_Tax_Details);
            request.addProperty("regnNo", regnNo);
            request.addProperty("taxNo", insuNo);
            request.addProperty("issuAuth", insCmpnyName);
            request.addProperty("validUptoDt", validUptoDt);
            request.addProperty("remarks", remarks);
            request.addProperty("permitDoc1", insDoc1);
            request.addProperty("permitDoc2", insDoc2);
            request.addProperty("mobileNo", mobileNo);
            request.addProperty("userType", userType);


            Log.i("request--->", "" + request);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            //need to add url
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
            Log.i("Request ::::", "" + request);
            androidHttpTransport.call(NameSPACE + Upload_Tax_Details, envelope);
            Object result = envelope.getResponse();

            if (result.toString() != null && !result.toString().equals("") || !result.toString().equals("anyType{}")
                    || !result.toString().equals("NA") || !result.toString().equals("0")) {
                Opdata_Chalana = result.toString();
            }
            Log.i("Opdata_Chalana ::::", "" + Opdata_Chalana);

        } catch (Exception E) {
            E.printStackTrace();
            Opdata_Chalana = "0";
        }

        return Opdata_Chalana;
    }

    public static String FitnessDetailsUpload(String regnNo, String insuNo, String insCmpnyName,
                                              String validUptoDt, String remarks, String insDoc1,
                                              String insDoc2, String mobileNo, String userType) {
        try {
            SoapObject request = new SoapObject(NameSPACE, Upload_Fitness_Details);
            request.addProperty("regnNo", regnNo);
            request.addProperty("fitnesNo", insuNo);
            request.addProperty("issuAuth", insCmpnyName);
            request.addProperty("validUptoDt", validUptoDt);
            request.addProperty("remarks", remarks);
            request.addProperty("permitDoc1", insDoc1);
            request.addProperty("permitDoc2", insDoc2);
            request.addProperty("mobileNo", mobileNo);
            request.addProperty("userType", userType);


            Log.i("request--->", "" + request);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            //need to add url
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
            Log.i("Request ::::", "" + request);
            androidHttpTransport.call(NameSPACE + Upload_Fitness_Details, envelope);
            Object result = envelope.getResponse();

            if (result.toString() != null && !result.toString().equals("") || !result.toString().equals("anyType{}")
                    || !result.toString().equals("NA") || !result.toString().equals("0")) {
                Opdata_Chalana = result.toString();
            }
            Log.i("Opdata_Chalana ::::", "" + Opdata_Chalana);

        } catch (Exception E) {
            E.printStackTrace();
            Opdata_Chalana = "0";
        }

        return Opdata_Chalana;
    }

    public static String PermitDetailsUpload(String regnNo, String insuNo, String insCmpnyName,
                                             String validUptoDt, String remarks, String insDoc1,
                                             String insDoc2, String mobileNo, String userType) {
        try {
            SoapObject request = new SoapObject(NameSPACE, Upload_permit_Details);
            request.addProperty("regnNo", regnNo);
            request.addProperty("permitNo", insuNo);
            request.addProperty("issuAuth", insCmpnyName);
            request.addProperty("validUptoDt", validUptoDt);
            request.addProperty("remarks", remarks);
            request.addProperty("permitDoc1", insDoc1);
            request.addProperty("permitDoc2", insDoc2);
            request.addProperty("mobileNo", mobileNo);
            request.addProperty("userType", userType);


            Log.i("request--->", "" + request);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            //need to add url
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
            Log.i("Request ::::", "" + request);
            androidHttpTransport.call(NameSPACE + Upload_permit_Details, envelope);
            Object result = envelope.getResponse();

            if (result.toString() != null && !result.toString().equals("") || !result.toString().equals("anyType{}")
                    || !result.toString().equals("NA") || !result.toString().equals("0")) {
                Opdata_Chalana = result.toString();
            }
            Log.i("Opdata_Chalana ::::", "" + Opdata_Chalana);

        } catch (Exception E) {
            E.printStackTrace();
            Opdata_Chalana = "0";
        }

        return Opdata_Chalana;
    }

    public static String registration(String userName, String regn_no, String mobileNo,
                                      String emailId, String password, String imeie, String key, String date, String otp, String verify_status) {
        try {
            SoapObject request = new SoapObject(NameSPACE, USER_REGISTRATION);
            request.addProperty("userName", userName);
            request.addProperty("regn_no", regn_no);
            request.addProperty("mobileNo", mobileNo);
            request.addProperty("emailId", emailId);
            request.addProperty("password", password);
            request.addProperty("imeie", imeie);
            request.addProperty("key", key);
            request.addProperty("date", date);
            request.addProperty("otp", otp);
            request.addProperty("verify_status", verify_status);

            Log.i("request--->", "" + request);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            //need to add url
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
            Log.i("Request ::::", "" + request);
            androidHttpTransport.call(NameSPACE + USER_REGISTRATION, envelope);
            Object result = envelope.getResponse();

            if (result.toString() != null && !result.toString().equals("") || !result.toString().equals("anyType{}")
                    || !result.toString().equals("NA") || !result.toString().equals("0")) {
                Opdata_Chalana = result.toString();
            }
            Log.i("Opdata_Chalana ::::", "" + Opdata_Chalana);

        } catch (Exception E) {
            E.printStackTrace();
            Opdata_Chalana = "0";
        }

        return Opdata_Chalana;
    }

    public static String DLDetails(String regnNo, String Usertype) {
        try {
            SoapObject request = new SoapObject(NameSPACE, GET_DL_DETAILS);
            request.addProperty("regnNo", regnNo);
            request.addProperty("userType", Usertype);

            Log.i("request--->", "" + request);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            //need to add url
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
            Log.i("Request ::::", "" + request);
            androidHttpTransport.call(NameSPACE + GET_DL_DETAILS, envelope);
            Object result = envelope.getResponse();

            if (result.toString() != null && !result.toString().equals("") || !result.toString().equals("anyType{}")
                    || !result.toString().equals("NA") || !result.toString().equals("0")) {
                Opdata_Chalana = result.toString();
            }
            Log.i("Opdata_Chalana ::::", "" + Opdata_Chalana);

        } catch (Exception E) {
            E.printStackTrace();
            Opdata_Chalana = "0";
        }

        return Opdata_Chalana;
    }


    public static String AAdharDetails(String aadhar, String Usertype) {
        try {
            SoapObject request = new SoapObject(NameSPACE, GET_AADHAR_DETAILS);
            request.addProperty("aadhaarNo", aadhar);
            request.addProperty("eid", "");
            request.addProperty("userType", Usertype);

            Log.i("request--->", "" + request);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            //need to add url
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
            Log.i("Request ::::", "" + request);
            androidHttpTransport.call(NameSPACE + GET_AADHAR_DETAILS, envelope);
            Object result = envelope.getResponse();

            if (result.toString() != null && !result.toString().equals("") || !result.toString().equals("anyType{}")
                    || !result.toString().equals("NA") || !result.toString().equals("0")) {
                Opdata_Chalana = result.toString();
            }
            Log.i("Opdata_Chalana ::::", "" + Opdata_Chalana);

        } catch (Exception E) {
            E.printStackTrace();
            Opdata_Chalana = "0";
        }

        return Opdata_Chalana;
    }


    public static String MobileDetails(String Mobile, String Usertype) {
        try {
            SoapObject request = new SoapObject(NameSPACE, GET_Mobile_DETAILS);
            request.addProperty("mobileNo", Mobile);
            request.addProperty("userType", Usertype);

            Log.i("request--->", "" + request);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            //need to add url
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
            Log.i("Request ::::", "" + request);
            androidHttpTransport.call(NameSPACE + GET_Mobile_DETAILS, envelope);
            Object result = envelope.getResponse();

            if (result.toString() != null && !result.toString().equals("") || !result.toString().equals("anyType{}")
                    || !result.toString().equals("NA") || !result.toString().equals("0")) {
                Opdata_Chalana = result.toString();
            }
            Log.i("Opdata_Chalana ::::", "" + Opdata_Chalana);

        } catch (Exception E) {
            E.printStackTrace();
            Opdata_Chalana = "0";
        }

        return Opdata_Chalana;
    }

    public static void sendOTP(String regnNo, String mobile_No, String current_date) {

        try {
            SoapObject request = new SoapObject(NameSPACE, SEND_OTP);
            request.addProperty("regn_no", regnNo);
            request.addProperty("mobileNo", mobile_No);
            request.addProperty("date", current_date);

            Log.e("request****", "" + request);//33

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            // need to add url
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
            androidHttpTransport.call(NameSPACE + SEND_OTP, envelope);
            Object result = envelope.getResponse();

            Log.i("result****", "" + result);

            if (result.toString() != null && !result.toString().equals("") || !result.toString().equals("anyType{}")
                    || !result.toString().equals("NA") || !result.toString().equals("0")) {
                otp_sent_resp = result.toString();
            }


        } catch (Exception E) {
            E.printStackTrace();
            otp_sent_resp = "0";


        }
    }


    public static String login(String Username, String Password) {

        try {
            SoapObject request = new SoapObject(NameSPACE, Authenticate_User);
            request.addProperty("userName", Username);
            request.addProperty("password", Password);

            Log.e("request****", "" + request);//33

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            // need to add url
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
            androidHttpTransport.call(NameSPACE + Authenticate_User, envelope);
            Object result = envelope.getResponse();

            Log.i("result****", "" + result);

            if (result.toString() != null && !result.toString().equals("") || !result.toString().equals("anyType{}")
                    || !result.toString().equals("NA") || !result.toString().equals("0")) {
                Opdata_Chalana = result.toString();
            }
            Log.i("Opdata_Chalana ::::", "" + Opdata_Chalana);

        } catch (Exception E) {
            E.printStackTrace();
            Opdata_Chalana = "0";
        }

        return Opdata_Chalana;
    }

    public static void GetViolationByRegnNo(String regnNo, String usertype, String captcha, String Mobilenum) {
        try {
            SoapObject request = new SoapObject(NameSPACE, GET_VIOLATIONS_BY_Regn);
            request.addProperty("regnNo", regnNo);
            request.addProperty("userType", usertype);
            request.addProperty("capTcha", captcha);
            request.addProperty("mobileNo", Mobilenum);

            Log.i("request--->", "" + request);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
            Log.i("Request ::::", "" + request);
            androidHttpTransport.call(NameSPACE + GET_VIOLATIONS_BY_Regn, envelope);
            Object result = envelope.getResponse();

            if (result.toString() != null && !result.toString().equals("") || !result.toString().equals("anyType{}")
                    || !result.toString().equals("NA") || !result.toString().equals("0")) {
                Opdata_Chalana = result.toString();
            }
            Log.i("Opdata_Chalana ::::", "" + Opdata_Chalana);

        } catch (Exception E) {
            E.printStackTrace();
            Opdata_Chalana = "0";
        }
    }


    public static String UploadComplaintOrFeedback(String fdbkORcmplnt, String userName, String mobileNo, String moduleType, String rating,
                                                   String remarks, String regnNo, String evidenceImg, String dtNtime, String city,
                                                   String location, String lat, String lang) {
        try {
            SoapObject request = new SoapObject(NameSPACE, Upload_CompliantOr_Feedback);
            request.addProperty("fdbkORcmplnt ", fdbkORcmplnt);
            request.addProperty("userName", userName);
            request.addProperty("mobileNo", mobileNo);
            request.addProperty("moduleType", moduleType);
            request.addProperty("rating", rating);
            request.addProperty("remarks", remarks);
            request.addProperty("regnNo", regnNo);
            request.addProperty("evidenceImg", evidenceImg);
            request.addProperty("dtNtime", dtNtime);
            request.addProperty("city", city);
            request.addProperty("location", location);
            request.addProperty("lat", lat);
            request.addProperty("lang", lang);


            Log.i("request--->", "" + request);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
            Log.i("Request ::::", "" + request);
            androidHttpTransport.call(NameSPACE + Upload_CompliantOr_Feedback, envelope);
            Object result = envelope.getResponse();

            if (result.toString() != null && !result.toString().equals("") || !result.toString().equals("anyType{}")
                    || !result.toString().equals("NA") || !result.toString().equals("0")) {
                Opdata_Chalana = result.toString();
            }
            Log.i("Opdata_Chalana ::::", "" + Opdata_Chalana);

        } catch (Exception E) {
            E.printStackTrace();
            Opdata_Chalana = "0";
        }
        return  Opdata_Chalana;
    }


}
