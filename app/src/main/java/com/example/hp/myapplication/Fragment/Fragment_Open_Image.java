package com.example.hp.myapplication.Fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.example.hp.myapplication.Config;
import com.example.hp.myapplication.Fruits;
import com.example.hp.myapplication.R;


public class Fragment_Open_Image extends Fragment {

    private ImageView opened_image;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment__open__image, container, false); // see it full way
        try {
            if (view != null) {
                LayoutInflater li = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = li.inflate(R.layout.fragment__open__image, container, false);


                intioliseId(view);
                setListners();

                Bitmap bmp = getArguments().getParcelable("Image");
               // if(!bmp.isRecycled())
                    opened_image.setImageBitmap(bmp );

            }
        } catch (Exception e) {
            e.getMessage();
        }
        return view;
    }

    private void setListners() {

    }

    private void intioliseId(View view) {
        opened_image = (ImageView) view.findViewById(R.id.image12);

    }
}
