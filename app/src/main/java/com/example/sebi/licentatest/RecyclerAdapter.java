package com.example.sebi.licentatest;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter <DataHolder> {

    private List<Data> objects;
    private Context context;
    public RecyclerAdapter(List<Data> objects, Context context) {
        this.objects = objects;
        this.context = context;
    }

    @NonNull
    @Override
    public DataHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater=LayoutInflater.from(context);
        return new DataHolder(layoutInflater.inflate(R.layout.data_layout,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull DataHolder dataHolder, int i) {
            dataHolder.bindViewHolder(objects.get(i));
    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

    public List<Data> getObjects() {
        return objects;
    }

    public void setObjects(List<Data> objects) {
        this.objects = objects;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
