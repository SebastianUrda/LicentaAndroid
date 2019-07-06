package com.example.sebi.licentatest.entities;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.sebi.licentatest.R;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

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
        ((DiscreteSeekBar)myRow.findViewById(R.id.discrete1)).setNumericTransformer(new DiscreteSeekBar.NumericTransformer() {
            @Override
            public int transform(int value) {
                getItem(position).setResponse(value);
                return value;
            }
        });
//        ((SeekBar)myRow.findViewById(R.id.seek_bar)).setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            int response=0;
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                    response=progress;
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//                getItem(position).setResponse(response);
//            }
//        });
        return myRow;
    }

    public static List<Question> getObjects() {
        return objects;
    }
}
