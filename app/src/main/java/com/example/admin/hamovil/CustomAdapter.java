package com.example.admin.hamovil;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.List;

import java.util.List;


/**
 * Created by jonnathan on 22/11/17.
 */

public class CustomAdapter extends BaseAdapter {

    String [] result;
    Context context;
    TextView txtView1;
    CheckBox check;

    private static LayoutInflater inflater=null;
    public CustomAdapter(Evaluacion mainActivity, List<String> prgmNameList) {
        // TODO Auto-generated constructor stub
        result = new String[prgmNameList.size()];
        prgmNameList.toArray(result);
        context=mainActivity;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return result.length;
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View convertView, ViewGroup parent) {

        final View rowView;

        rowView = inflater.inflate(R.layout.item_in_list, null);

        txtView1 =(TextView) rowView.findViewById(R.id.txtOpcion);
        check = (CheckBox) rowView.findViewById(R.id.checq);
        txtView1.setText(result[i]);

        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Toast.makeText(context, result[i], Toast.LENGTH_LONG).show();
            }
        });

        return rowView;
    }


}
