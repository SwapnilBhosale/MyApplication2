package com.kng.app.kngapp.Activitis;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.kng.app.kngapp.Config;
import com.kng.app.kngapp.R;
import com.kng.app.kngapp.helper.PrefManager;

import org.json.JSONException;
import org.json.JSONObject;


public class Register_Activity extends AppCompatActivity implements View.OnClickListener {

    private Button register_btn;
    private EditText mobileNo;
    private EditText name;
    private EditText village;
    private EditText address;
    private EditText city;
    private EditText state;
    private EditText postalCode;
    private Context mContext;
    LinearLayout  pop_up_layout;
    RelativeLayout main_layout;

    private static String TAG = Register_Activity.class.getSimpleName();
    private PopupWindow mPopupWindow = null;
    private  SMSEventReceiver smsEventReceiver;

    ProgressDialog pd;

    ImageView cancel_button;
    Button popup_button ;
    public static EditText otp_text_box ;
    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_);
        mContext = getApplicationContext();
        pd = getProgressBar();
        smsEventReceiver  = new SMSEventReceiver();
        initiolizeId();
        setListners();
        populateMobileNo();
    }

    private ProgressDialog getProgressBar(){
        ProgressDialog pd = new ProgressDialog(Register_Activity.this);
        // Set progress dialog style spinner
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        // Set the progress dialog title and message
        pd.setMessage(Config.getContext().getResources().getString(R.string.loading));
        // Set the progress dialog background color
        //pd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFD4D9D0")));
        pd.setIndeterminate(false);
        pd.setCancelable(false);
        // Finally, show the progress dialog
        return pd;
    }


    private void populateMobileNo() {
        Intent intent = getIntent();
        if (intent.hasExtra("MOBILE_NO")) {
            String mobile = intent.getStringExtra("MOBILE_NO");
            mobileNo.setText(mobile);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage(getString(R.string.exit_confirmation));
        builder1.setCancelable(true);
        builder1.setPositiveButton(
                R.string.option_yes,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                        System.exit(0);
                    }
                });
        builder1.setNegativeButton(
                R.string.option_no,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    private void setListners() {

        register_btn.setOnClickListener(this);
    }

    private JSONObject getRegisterJsonBody() throws JSONException {
        return new JSONObject("{\"language_id\" : "+new PrefManager(Config.getContext()).getAppLanguage()+",\"name\" : \"" + name.getText().toString() + "\",\"village\" : \"" + village.getText().toString() + "\",\"address\" : \"" + address.getText().toString() + "\",\"mobile\" : \"" + mobileNo.getText().toString() + "\",\"state\" : \"" + state.getText().toString() + "\",\"city\" : \"" + city.getText().toString() + "\",\"postal_code\" : \"" + postalCode.getText().toString() + "\"}");
    }

    private void initiolizeId() {

        register_btn = (Button) findViewById(R.id.register_btn);
        mobileNo = (EditText) findViewById(R.id.mobile_no);
        name = (EditText) findViewById(R.id.user_name);
        village = (EditText) findViewById(R.id.village);
        address = (EditText) findViewById(R.id.address);
        city = (EditText) findViewById(R.id.city);
        state = (EditText) findViewById(R.id.state);
        postalCode = (EditText) findViewById(R.id.postal_code);
        main_layout = (RelativeLayout) findViewById(R.id.activity_register_);
    }

    public boolean validateData(){
        String mobile = mobileNo.getText().toString();
        String fname = name.getText().toString();
        String lname = village.getText().toString();
        String add = address.getText().toString();
        String clientCity = city.getText().toString();
        String clientState = state.getText().toString();
        String pinCode = postalCode.getText().toString();

        if(isMobileValid(mobile) && isfNameValid(fname) && islNameValid(lname) && isAddressValid(add) && isCityValid(clientCity)
                && isStateValid(clientState) && isPinCodeValid(pinCode))
            return true;
        return false;
    }

    private boolean isPinCodeValid(String pinCode) {
        if(!TextUtils.isEmpty(pinCode) && pinCode.length() == 6)
            return true;
        else
            postalCode.setError(getString(R.string.error_pin_code));
        return false;
    }

    private boolean isStateValid(String clientState) {
        if(!TextUtils.isEmpty(clientState))
            return true;
        else
            state.setError(getString(R.string.error_state));
        return false;
    }

    private boolean isCityValid(String clientCity) {
        if(!TextUtils.isEmpty(clientCity))
            return true;
        else
            city.setError(getString(R.string.error_city));
        return false;
    }

    private boolean isAddressValid(String add) {
        if(!TextUtils.isEmpty(add))
            return true;
        else
            address.setError(getString(R.string.error_address));
        return false;
    }

    private boolean islNameValid(String lname) {
        if(!TextUtils.isEmpty(lname))
            return true;
        else
            village.setError(getString(R.string.error_village));
        return false;
    }

    private boolean isfNameValid(String fname) {
        if(!TextUtils.isEmpty(fname))
            return true;
        else
            name.setError(getString(R.string.error_name));
        return false;
    }
    private boolean isMobileValid(String mobile) {
        //TODO: Replace this with your own logic
        if(mobile.length() == 10)
            return true;
        else
            mobileNo.setError(getString(R.string.error_mobile_no));
        return false;
    }

    @Override
    public void onClick(View view) {
        if(validateData()) {
            try {
                final JSONObject jsonBody = getRegisterJsonBody();
                JsonObjectRequest loginRequest = new JsonObjectRequest(Request.Method.POST, Config.REGISTER_URL, jsonBody, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "onResponse Register_Activity: " + response.toString());
                        pd.dismiss();
                        try {
                            final boolean isSuccess = response.getBoolean("status");
                            if (isSuccess) {
                                //open verifyOTP screen

                                //Config.OTP_SCREEN = "register";
                                Log.d(TAG, "opening OTP from register activiy");
                                PrefManager pref = new PrefManager(getApplicationContext());

                                pref.setIsWaitingForSMS(true);
                                //JSONArray jsonArray = response.getJSON("data");
                                JSONObject data = response.getJSONObject("data");
                                pref.setCustomerId(data.getString("customer_id"));
                                pref.setName(data.getString("name"));
                                pref.setVillage(data.getString("village"));
                                pref.setSessionKey(data.getString("session_key"));
                                pref.setMobile(data.getString("mobile_no"));
                                pref.setAddress(data.getString("address"));
                                pref.setState(data.getString("state"));
                                pref.setCity(data.getString("city"));
                                pref.setPinCode(data.getString("postal_code"));

                                //Log.d(TAG, "Response success. Will be opening enter OTP screen" + jsonArray.toString());


                                Log.d(TAG, "Response success. Will be opening enter OTP screen");
                                AlertDialog.Builder dialogBuilder = new android.support.v7.app.AlertDialog.Builder(Register_Activity.this);

                                // ...Irrelevant code for customizing the buttons and title
                                // dialogBuilder.setTitle("Order History");
                                LayoutInflater inflater = getLayoutInflater();
                                View dialogView = inflater.inflate(R.layout.otp_pop_up, null);
                                dialogBuilder.setView(dialogView);

                                cancel_button = (ImageView) dialogView.findViewById(R.id.cancel_button);
                                popup_button = (Button) dialogView.findViewById(R.id.otp_btn);
                                otp_text_box = (EditText) dialogView.findViewById(R.id.otp_text_box);
                                TextView text = (TextView) dialogView.findViewById(R.id.otp_text_with_mobile);
                                text.setText(getString(R.string.verify_mobile_hint)+" "+mobileNo.getText().toString());



                                alertDialog = dialogBuilder.create();
                                alertDialog.setCanceledOnTouchOutside(false);
                                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                                lp.copyFrom(alertDialog.getWindow().getAttributes());
                                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                                alertDialog.show();

                                try {
                                    popup_button.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            verifyOtp();
                                            //alertDialog.dismiss();
                                        }
                                    });

                                    cancel_button.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {

                                            alertDialog.dismiss();
                                        }
                                    });

                                } catch (Exception e) {
                                    e.getMessage();
                                    Log.e("submit", "onClick: ",e );
                                }
                            } else {
                                //error in registartion
                                //do error handling
                                pd.dismiss();
                                if (response.getJSONObject("error").getInt("errorCode") == 5)
                                    Toast.makeText(Register_Activity.this, R.string.err_mobile_already_exist, Toast.LENGTH_LONG).show();
                                else
                                    Toast.makeText(Register_Activity.this, "User registartion error" + response.getJSONObject("error").getString("errorMessage"), Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            Log.e(TAG, "onResponse: " + response.toString(), e);
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d(TAG, "Error: " + volleyError.getMessage());

                        // hide the progress dialog
                        pd.dismiss();
                        String message = null;
                        if (volleyError instanceof NetworkError || volleyError instanceof ServerError || volleyError instanceof AuthFailureError
                                || volleyError instanceof ParseError || volleyError instanceof NoConnectionError || volleyError instanceof TimeoutError )
                            Toast.makeText(getApplicationContext(),R.string.error_no_internet_conenction, Toast.LENGTH_LONG).show();

                    }
                });
                loginRequest.setRetryPolicy(new DefaultRetryPolicy(Config.WEB_TIMEOUT,Config.WEB_RETRY_COUNT,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                Volley.newRequestQueue(getApplicationContext()).add(loginRequest);
                pd.show();
            } catch (Exception e) {
                Log.e(TAG, "attemptLogin: " + e.getMessage(), e);
            }
        }
    }

    private void verifyOtp() {
        String otp = (otp_text_box.getText().toString()).trim();
        if(TextUtils.isEmpty(otp) || (otp.length() != 6)){
            otp_text_box.setError(getString(R.string.error_empty_otp));
        }else{
            PrefManager pref = new PrefManager(Config.getContext());
            String verify_otp_url_with_param = Config.VERIFY_OTP_URL+"?otp="+otp+"&session_key="+pref.getSessionKey()+"&customer_id="+pref.getCustomerId();
            Log.d(TAG, "verifyOtp() called with: otp = [" + otp + "] url : "+verify_otp_url_with_param);

            JsonObjectRequest verify_otp_req = new JsonObjectRequest(Request.Method.GET,verify_otp_url_with_param,null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d(TAG, "onResponse: verify OTP "+response.toString());
                    pd.dismiss();
                    try{
                        final boolean isSuccess = response.getBoolean("status");
                        if(isSuccess){
                            //put in shared preference here
                            PrefManager pref = new PrefManager(Config.getContext());

                            pref.setIsWaitingForSMS(false);
                            pref.setIsLoggedIn(true);
                            //Stop spinner and open home activitys
                            statMainActivity();
                        }else{
                            int errorCode = response.getJSONObject("error").getInt("errorCode");
                            if(errorCode == 15)
                                otp_text_box.setError(getString(R.string.error_wrong_otp));
                        }
                    }catch (Exception e){
                        Log.e(TAG, "onResponse: "+response.toString(),e);
                    }

                }
            },new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    VolleyLog.d(TAG, "Error: " + volleyError.getMessage());

                    pd.dismiss();

                    if (volleyError instanceof NetworkError || volleyError instanceof ServerError || volleyError instanceof AuthFailureError || volleyError instanceof ParseError || volleyError instanceof NoConnectionError || volleyError instanceof TimeoutError )
                        Toast.makeText(Register_Activity.this,R.string.error_no_internet_conenction, Toast.LENGTH_LONG).show();
                    Toast.makeText(Register_Activity.this,R.string.error_general_error,Toast.LENGTH_SHORT).show();
                }
            });
            verify_otp_req.setRetryPolicy(new DefaultRetryPolicy(Config.WEB_TIMEOUT,Config.WEB_RETRY_COUNT,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            Volley.newRequestQueue(this).add(verify_otp_req);
            pd.show();
        }

    }

    private void statMainActivity(){
        Log.d(TAG, "Started main activity");
        Intent i = new Intent(Config.getContext(), MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK  | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        Config.getContext().startActivity(i);
    }


    public class SMSEventReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "onReceive: ");
            if(intent.hasExtra("otp")){
                String otp = intent.getExtras().getString("otp");
                Log.d(TAG, "onReceive: otp received  : "+otp);
                verifyOTP(otp);

            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(smsEventReceiver,new IntentFilter(Config.SMS_RECIEVED_INTENT));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(smsEventReceiver);
    }

    public void verifyOTP(String otp){
        otp_text_box.setText(otp);

        final PrefManager pref = new PrefManager(Config.getContext());
        String verify_otp_url_with_param = Config.VERIFY_OTP_URL+"?otp="+otp+"&session_key="+pref.getSessionKey()+"&customer_id="+pref.getCustomerId();
        Log.d(TAG, "verifyOtp() called with: otp = [" + otp + "] url : "+verify_otp_url_with_param);

        JsonObjectRequest verify_otp_req = new JsonObjectRequest(Request.Method.GET,verify_otp_url_with_param,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, "onResponse: verify OTP "+response.toString());
                try{
                    final boolean isSuccess = response.getBoolean("status");
                    if(isSuccess){
                        //put in shared preference here
                        if(alertDialog.isShowing())
                            alertDialog.dismiss();
                        pref.setIsLoggedIn(true);
                        pref.setIsWaitingForSMS(false);
                        //Stop spinner and open home activitys
                        statMainActivity();
                    }
                }catch (Exception e){
                    Log.e(TAG, "onResponse: "+response.toString(),e);
                }

            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e(TAG, "Error: " + volleyError);
                if (volleyError instanceof NetworkError || volleyError instanceof ServerError || volleyError instanceof AuthFailureError || volleyError instanceof ParseError || volleyError instanceof NoConnectionError || volleyError instanceof TimeoutError)
                    Toast.makeText(Config.getContext(),R.string.error_no_internet_conenction, Toast.LENGTH_LONG).show();
                Toast.makeText(Config.getContext(),R.string.error_general_error,Toast.LENGTH_SHORT).show();

            }
        });
        Volley.newRequestQueue(Config.getContext()).add(verify_otp_req);
    }

}
