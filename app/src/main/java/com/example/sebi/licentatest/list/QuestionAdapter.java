package com.example.sebi.licentatest.list;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.sebi.licentatest.R;

import java.util.List;

public class QuestionAdapter extends ArrayAdapter<Question> {
    public static List<Question> objects;
    public QuestionAdapter(Context context, List<Question> objects)
    {
        super(context, 0, objects);
        this.objects=objects;
    }
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater.from(getContext()));
        final View myRow = (convertView == null) ? inflater.inflate(R.layout.question_adapter, parent, false) : convertView;
        ((TextView)myRow.findViewById(R.id.question)).setText(getItem(position).getText());
        ((SeekBar)myRow.findViewById(R.id.seek_bar)).setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int response=0;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    response=progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                getItem(position).setResponse(response);
            }
        });
        return myRow;
    }

    public static List<Question> getObjects() {
        return objects;
    }
}
