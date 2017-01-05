package com.example.hp.myapplication.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.hp.myapplication.R;


public class Fragment_Checkout2 extends Fragment {

    static CheckBox c2_checkbox;
    LinearLayout c2_linear_layout;
    Button c2_back, c2_next;
    EditText c2_name,c2_address,c2_mobile_no,c2_city,c2_state,c2_postal_code;
    private String TAG = Fragment_Checkout2.class.getSimpleName();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment__checkout2, container, false); // see it full way
        try {
            if (view != null) {
                LayoutInflater li = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = li.inflate(R.layout.fragment__checkout2, container, false);


                intioliseId(view);

                c2_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton buttonView,
                                                 boolean isChecked) {
                        // TODO Auto-generated method stub
                        if (isChecked) {
                           c2_linear_layout.setVisibility(View.VISIBLE);

                        } else {
                            c2_linear_layout.setVisibility(View.GONE);

                        }
                    }
                });

                setListners();


            }
        } catch (Exception e) {
            e.getMessage();
        }
        return view;
    }

    public void setActionBarTitle(String title) {
        getActivity().setTitle(title);
    }

    @Override
    public void onResume() {
        super.onResume();
        setActionBarTitle(getString(R.string.delivery_address));
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActionBarTitle(getString(R.string.delivery_address));
    }

    private void setListners() {

        CheckBox.OnCheckedChangeListener listener =
                new CompoundButton.OnCheckedChangeListener() {
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                        if (isChecked) {
                            if (c2_linear_layout.getVisibility() == View.GONE)
                                c2_linear_layout.setVisibility(View.VISIBLE);
                        } else {
                            c2_linear_layout.setVisibility(View.GONE);
                        }
                    }
                };


        c2_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getActivity().onBackPressed();
            }
        });

        c2_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Boolean isChecked = c2_checkbox.isChecked();
                Bundle bundle = new Bundle();
                Log.d(TAG, "onClick: "+isChecked);
                bundle.putBoolean("isDeliveryAddSame",!isChecked);
                if(isChecked){
                  if(!validateData())
                      return;
                }
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                Fragment_Checkout3 fc3 = new Fragment_Checkout3();
                fc3.setArguments(bundle);
                ft.add(R.id.checkout_frame, fc3).addToBackStack(TAG);
                ft.hide(Fragment_Checkout2.this);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.commit();
            }
        });
    }

    private void intioliseId(View view) {
        c2_checkbox = (CheckBox) view.findViewById(R.id.c2_checkbox);
        c2_linear_layout = (LinearLayout) view.findViewById(R.id.c2_linear_layout);
        c2_next = (Button) view.findViewById(R.id.c2_next);
        c2_back = (Button) view.findViewById(R.id.c2_back);
        c2_name = (EditText) view.findViewById(R.id.c2_name);
        c2_address = (EditText) view.findViewById(R.id.c2_address);
        c2_city = (EditText) view.findViewById(R.id.c2_city);
        c2_state = (EditText) view.findViewById(R.id.c2_state);
        c2_mobile_no = (EditText) view.findViewById(R.id.c2_mobile_no);
        c2_postal_code = (EditText) view.findViewById(R.id.c2_postal_code);
    }


    public boolean validateData(){
        String mobile = c2_mobile_no.getText().toString();
        String fname = c2_name.getText().toString();
        String add = c2_address.getText().toString();
        String clientCity = c2_state.getText().toString();
        String clientState = c2_state.getText().toString();
        String pinCode = c2_mobile_no.getText().toString();

        if(isMobileValid(mobile) && isfNameValid(fname) && isAddressValid(add) && isCityValid(clientCity)
                && isStateValid(clientState) && isPinCodeValid(pinCode))
            return true;
        return false;
    }

    private boolean isPinCodeValid(String pinCode) {
        if(!TextUtils.isEmpty(pinCode) && pinCode.length() != 6)
            return true;
        else
            c2_postal_code.setError(getString(R.string.error_pin_code));
        return false;
    }

    private boolean isStateValid(String clientState) {
        if(!TextUtils.isEmpty(clientState))
            return true;
        else
            c2_state.setError(getString(R.string.error_state));
        return false;
    }

    private boolean isCityValid(String clientCity) {
        if(!TextUtils.isEmpty(clientCity))
            return true;
        else
            c2_city.setError(getString(R.string.error_city));
        return false;
    }

    private boolean isAddressValid(String add) {
        if(!TextUtils.isEmpty(add))
            return true;
        else
            c2_address.setError(getString(R.string.error_address));
        return false;
    }

    /*private boolean islNameValid(String lname) {
        if(!TextUtils.isEmpty(lname))
            return true;
        else
            village.setError(getString(R.string.error_village));
        return false;
    }*/

    private boolean isfNameValid(String fname) {
        if(!TextUtils.isEmpty(fname))
            return true;
        else
            c2_name.setError(getString(R.string.error_name));
        return false;
    }
    private boolean isMobileValid(String mobile) {
        //TODO: Replace this with your own logic
        if(mobile.length() == 10)
            return true;
        else
            c2_mobile_no.setError(getString(R.string.error_mobile_no));
        return false;
    }
}
