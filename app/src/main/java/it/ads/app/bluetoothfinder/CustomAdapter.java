package it.ads.app.bluetoothfinder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter<DeviceData> {


    public CustomAdapter(@NonNull Context context, ArrayList<DeviceData> deviceInfo) {
        super(context, 0, deviceInfo);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View currentItemView = convertView;

        if (currentItemView == null) {
            currentItemView = LayoutInflater.from(getContext()).inflate(R.layout.custom_listview, parent, false);
        }

        DeviceData currentNumberPosition = getItem(position);

        TextView textView1 = currentItemView.findViewById(R.id.txtDeviceName);
        textView1.setText("Name: "+currentNumberPosition.getDeviceName());

        TextView textView2 = currentItemView.findViewById(R.id.txtDeviceAddress);
        textView2.setText("MAC: "+currentNumberPosition.getDeviceID());

        TextView textView3 = currentItemView.findViewById(R.id.txtDeviceType);
        textView3.setText("Type: "+currentNumberPosition.getDeviceType());

        return currentItemView;
    }
}
