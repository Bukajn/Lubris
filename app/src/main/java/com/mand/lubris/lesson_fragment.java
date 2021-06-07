package com.mand.lubris;

import android.graphics.Paint;
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
    private String lekcja, nowaLekcja, nr, status, nauczyciel,nowyNauczyciel, sala, nowaSala;

    public lesson_fragment(String status, String lekcja, String nowaLekcja ,String nr, String nauczyciel,String nowyNauczyciel ,String sala, String nowaSala)
    {
        this.status = status;
        this.lekcja = lekcja;
        this.nowaLekcja = nowaLekcja;
        this.nr = nr;
        this.nauczyciel = nauczyciel;
        this.nowyNauczyciel =nowyNauczyciel;
        this.sala = sala;
        this.nowaSala = nowaSala;
    }
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
       View view = inflater.inflate(R.layout.fragment_lesson,container,false);
       TextView obj_status = (TextView)view.findViewById(R.id.status);
       TextView obj_lekcja = (TextView)view.findViewById(R.id.lekcja);
       TextView obj_nowaLekcja = (TextView)view.findViewById(R.id.nowalekcja);
       TextView obj_nr = (TextView)view.findViewById(R.id.nr);
       TextView obj_nauczyciel = (TextView)view.findViewById(R.id.nauczyciel);
       TextView obj_nowyNauczyciel = (TextView)view.findViewById(R.id.nowynauczyciel);
       TextView obj_sala = (TextView)view.findViewById(R.id.sala);
       TextView obj_nowaSala = (TextView)view.findViewById(R.id.nowasala);


       ustaw(obj_status,status);
       ustaw(obj_lekcja,lekcja);
       ustaw(obj_nowaLekcja,nowaLekcja);
       ustaw(obj_nr,nr);
       ustaw(obj_nauczyciel,nauczyciel);
       ustaw(obj_nowyNauczyciel,nowyNauczyciel);
       ustaw(obj_sala,sala);
       ustaw(obj_nowaSala,nowaSala);



       if (nowaLekcja!=null)
       {
           przekresl(obj_lekcja);
       }
       if(nowyNauczyciel!=null)
       {
           przekresl(obj_nauczyciel);
       }
       if(nowaSala!=null)
       {
           przekresl(obj_sala);
       }

       if(status != null)
       {
           if(status.equals("odwołane") || status.equals("przesunięcie"))
           {
               przekresl(obj_nr);
               przekresl(obj_lekcja);
               przekresl(obj_nauczyciel);
           }
           if (status.equals("PrzesunięcieTu"))
           {
               ustaw(obj_status,"przesunięcie");
           }
       }

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

    private void przekresl(TextView v)
    {
        v.setPaintFlags(v.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
    }
}
