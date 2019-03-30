package com.example.sebi.licentatest.data;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.sebi.licentatest.R;

public class DataHolder extends RecyclerView.ViewHolder {
    private TextView date;
    private TextView latitude;
    private TextView longitude;
    private TextView temperature;
    private TextView lpg;
    private TextView co;
    private TextView smoke;
    private TextView co2;
    private TextView temperature2;
    private TextView humidity;
    private TextView dust;
    private TextView pressure;
    private TextView vis;
    private TextView ir;
    private TextView uv;
    public DataHolder(View itemView){
        super(itemView);
        date=itemView.findViewById(R.id.date);
        latitude=itemView.findViewById(R.id.latitude);
        longitude=itemView.findViewById(R.id.longitude);
        temperature=itemView.findViewById(R.id.temperature);
        lpg=itemView.findViewById(R.id.lpg);
        co=itemView.findViewById(R.id.co);
        smoke=itemView.findViewById(R.id.smoke);
        co2=itemView.findViewById(R.id.co2);
        temperature2=itemView.findViewById(R.id.temperature2);
        humidity=itemView.findViewById(R.id.humidity);
        dust=itemView.findViewById(R.id.dust);
        pressure=itemView.findViewById(R.id.pressure);
        vis=itemView.findViewById(R.id.vis);
        ir=itemView.findViewById(R.id.ir);
        uv=itemView.findViewById(R.id.uv);

    }
    public void bindViewHolder(Data data)
    {
        date.setText(data.getDate());
        latitude.setText(data.getLattitude());
        longitude.setText(data.getLongitude());
        temperature.setText(String.valueOf(data.getFrontTemp()));
        lpg.setText(String.valueOf(data.getLpg()));
        co.setText(String.valueOf(data.getCo()));
        smoke.setText(String.valueOf(data.getSmoke()));
        co2.setText(String.valueOf(data.getCo2()));
        temperature2.setText(String.valueOf(data.getBackTemp()));
        humidity.setText(String.valueOf(data.getHumidity()));
        dust.setText(String.valueOf(data.getDust()));
        pressure.setText(String.valueOf(data.getPressure()));
        vis.setText(String.valueOf(data.getVis()));
        ir.setText(String.valueOf(data.getIr()));
        uv.setText(String.valueOf(data.getUv()));
    }
}
