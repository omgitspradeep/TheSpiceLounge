package com.sujan.info.thespicelounge.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sujan.info.thespicelounge.R;

import org.jsoup.Jsoup;

/**
 * A simple {@link Fragment} subclass.
 */
public class AboutUs extends Fragment {

    TextView textView;
    public AboutUs() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_about_us, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        textView=(TextView)view.findViewById(R.id.tv_aboutUs);
        textView.setText(Html.fromHtml(aboutUsMessage));

    }

    public String aboutUsMessage="<h1> \tThe Spice Lounge</h1>\n\n\n\n\n\n"+
            "<p>&nbsp;&nbsp;The Spice Lounge is located at<b> Minbhawan, Baneshwor</b>, on the banks of the beautiful Bagmati River. It was established with love in the year of 2016 as the doorway to discover more than our cuisines! We offer more than just best food in the town,it is an experience…a wholesome, satisfying and elevating experience!<i><b> Live Music, National and international celebrities, wide choices of cuisines, delicate services and joyful ambience</i></b> makes it a wise choice for foodie of any age. </p>\n\n"+

            "<p>&nbsp;&nbsp;Our expert chefs constantly challenge themselves in creating better, tastier and more appetizing food. Its extensive gardens and location on the banks of the river make it an interesting place to observe birds and other wildlife. </p>\n\n"+

            "<p>Give us a visit and let us prove our claim...</p>\n"+

            "<p><b>Minbhawan, Baneswor</p>"+"<p>Kathmandu, Nepal \n +977-9863999999\n\n</p><b>";


}
