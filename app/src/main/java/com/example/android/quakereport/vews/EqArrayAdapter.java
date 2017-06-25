package com.example.android.quakereport.vews;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.android.quakereport.R;
import com.example.android.quakereport.structs.EarthQuakeInfo;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Samsung on 6/10/2017.
 */

public class EqArrayAdapter extends ArrayAdapter<EarthQuakeInfo> {
    private DecimalFormat decimalFormat = new DecimalFormat("0.0");
    private SimpleDateFormat dateFormat = new SimpleDateFormat("MMM d, yyyy");
    private SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm");


    public EqArrayAdapter(
            @NonNull Context context) {
        super(context, 0);
    }

    private void setText(View convertView, int id, String text) {
        ((TextView) convertView.findViewById(id)).setText(text);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        EarthQuakeInfo info = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        int comaPos = info.getDescription().indexOf(',');
        setText(convertView, R.id.mag, decimalFormat.format(info.getMagnitude()));
        if (comaPos != -1) {
            setText(convertView, R.id.dist, info.getDescription().substring(0, comaPos));
        }
        setText(convertView, R.id.descr, info.getDescription().substring(comaPos + 1));
        setText(convertView, R.id.date, dateFormat.format(info.getDate()));
        setText(convertView, R.id.time, timeFormat.format(info.getDate()));
        GradientDrawable magnitudeCircle =
                (GradientDrawable) convertView.findViewById(R.id.mag).getBackground();

        magnitudeCircle.setColor(
                ContextCompat.getColor(getContext(), getCircleColor(info.getMagnitude())));
        return convertView;
    }

    private int getCircleColor(double mag) {
        int v = (int) mag;
        switch (v) {
            case 0:
            case 1:
                return R.color.magnitude1;
            case 2:
                return R.color.magnitude2;
            case 3:
                return R.color.magnitude3;
            case 4:
                return R.color.magnitude4;
            case 5:
                return R.color.magnitude5;
            case 6:
                return R.color.magnitude6;
            case 7:
                return R.color.magnitude7;
            case 8:
                return R.color.magnitude8;
            case 9:
                return R.color.magnitude9;
            default:
                return R.color.magnitude10plus;
        }
    }

    public void setData(List<EarthQuakeInfo> data) {
        clear();
        if (data != null) {
            addAll(data);
        }

    }
}
