package com.mand.lubris;

import android.os.Bundle;
import android.os.Debug;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class lesson_fragment extends Fragment
{
    private String name;
    public lesson_fragment(String name)
    {
        this.name = name;
    }
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
       View view = inflater.inflate(R.layout.fragment_lesson,container,false);

        TextView name_text = (TextView)view.findViewById(R.id.nameLesson);
        name_text.setText(name);

       return view;
    }


}
