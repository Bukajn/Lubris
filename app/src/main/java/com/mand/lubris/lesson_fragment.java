package com.mand.lubris;

import android.os.Bundle;
import android.os.Debug;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class lesson_fragment extends Fragment
{
    private String lekcja, nr, status, nauczyciel, sala;

    public lesson_fragment(String status, String lekcja, String nr, String nauczyciel, String sala)
    {
        this.status = status;
        this.lekcja = lekcja;
        this.nr = nr;
        this.nauczyciel = nauczyciel;
        this.sala = sala;
    }
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
       View view = inflater.inflate(R.layout.fragment_lesson,container,false);
       TextView obj_status = (TextView)view.findViewById(R.id.status);
       TextView obj_lekcja = (TextView)view.findViewById(R.id.lekcja);
       TextView obj_nr = (TextView)view.findViewById(R.id.nr);
       TextView obj_nauczyciel = (TextView)view.findViewById(R.id.nauczyciel);
       TextView obj_sala = (TextView)view.findViewById(R.id.sala);


       ustaw(obj_status,status);
       ustaw(obj_lekcja,lekcja);
       ustaw(obj_nr,nr);
       ustaw(obj_nauczyciel,nauczyciel);
       ustaw(obj_sala,sala);
       return view;
    }

    //ustawia normalnie,lecz gdy jest null to wtedy ustawia widoczność na gone
    private void ustaw(TextView v, String text)
    {
        if (text!=null)
        {
            v.setText(text);
        }else {
            v.setVisibility(View.GONE);
        }
    }
}
