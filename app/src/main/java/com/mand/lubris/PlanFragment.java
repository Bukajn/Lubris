package com.mand.lubris;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

public class PlanFragment extends Fragment {
    private MainActivity mainActivity;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mainActivity = new MainActivity();
        View view = inflater.inflate(R.layout.fragment_plan, container, false);

        new Thread()
        {
            @Override
            public void run()
            {
                mainActivity.pobierzPlanLekcjiNaAktualnyTydzien();


                JSONObject plan=null;
                try {
                    plan= new JSONObject(mainActivity.read("timeTable","Default"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    System.out.println(plan.getJSONObject("Timetable").getJSONArray("2021-05-24"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }.start();


        return view;
    }
}
