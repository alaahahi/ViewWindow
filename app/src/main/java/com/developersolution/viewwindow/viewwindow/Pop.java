package com.developersolution.viewwindow.viewwindow;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
public class Pop extends Activity {

    EditText company,ip;
    Button savepop,backpop;
    Spinner spinner1;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int)(width),(int)(height));
        spinner1 =(Spinner) findViewById(R.id.spinner);
        company = (EditText) findViewById(R.id.company);
        ip=(EditText) findViewById(R.id.ip);
        savepop = (Button) findViewById(R.id.savebtn);
        backpop= (Button) findViewById(R.id.backbtn);
        ArrayAdapter<CharSequence> myarray = ArrayAdapter.createFromResource(this,R.array.spinardata,android.R.layout.simple_list_item_1);
        myarray.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(myarray);


        SharedPreferences sh = getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        String companyprf = sh.getString("company","");
        String ipprf = sh.getString("ip","");

        company.setText(companyprf);
        ip.setText(ipprf);



        savepop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sh = getSharedPreferences("userinfo", Context.MODE_PRIVATE);
                SharedPreferences.Editor useredit =sh.edit();

                useredit.putString("company",company.getText().toString());
                useredit.putString("ip",ip.getText().toString());
                useredit.putString("type",spinner1.getSelectedItem().toString());
                useredit.apply();

               Toast.makeText(getBaseContext(), "Thanks Your Changes is Saved",
                     Toast.LENGTH_LONG).show();


            }
        });

        backpop.setOnClickListener(new View.OnClickListener()  {
            @Override
            public void onClick(View view) {

                startActivities(new Intent[]{new Intent(Pop.this, MainActivity.class)});

          }
        });





    }
}
