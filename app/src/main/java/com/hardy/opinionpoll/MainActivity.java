package com.hardy.opinionpoll;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import worker8.com.github.radiogroupplus.RadioGroupPlus;

public class MainActivity extends AppCompatActivity {

    //Variable Initialization
    RadioGroupPlus radioGroupPlus;
    Button btnSubmit,btnResult;
    TextView textView;
    int num;
    Dialog dialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Assign variable
        radioGroupPlus = findViewById(R.id.radioGroup);
        btnSubmit=findViewById(R.id.btn_submit);
      //  textView=findViewById(R.id.textView);
        btnResult=findViewById(R.id.btn_result);



        dialog=new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.custom_dialog);
        dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.backgrouund));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);

        Button d_retry=dialog.findViewById(R.id.dialog_retry);
        Button d_result=dialog.findViewById(R.id.dialog_result);
        TextView d_view=dialog.findViewById(R.id.dialog_text);
//SharedPreferences need to be optimized
        SharedPreferences sharedpref=getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        String name=sharedpref.getString("username","new");
        if(name.equals("checked"))
        {
          d_view.setText("Response Saved");
          dialog.show();
        }
        else
        {

            SharedPreferences.Editor editor=sharedpref.edit();
            editor.putString("usercheck","checked");
        }



        d_retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(0,0);
                startActivity(getIntent());
                overridePendingTransition(0,0);
                dialog.dismiss();
            }
        });

        d_result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,BarDisplay.class);
                startActivity(intent);
                dialog.dismiss();
            }
        });


        btnResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,BarDisplay.class);
                startActivity(intent);

            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Get selected radio button id
                int id=radioGroupPlus.getCheckedRadioButtonId();

                switch (id){
                        //when id equals to button
                    case R.id.radio_cplus:
                                          getData("Cplus");
                          break;
                    case R.id.radio_java: getData("Java");
                                            break;
                    case R.id.radio_python: getData("Python");
                                            break;
                    case R.id.radio_react: getData("React");
                                            break;
                    default:
                        Toast.makeText(getApplicationContext(),"Enter Your Choice",Toast.LENGTH_LONG).show();
                        return;
                }
            }
        });

    }

//First fetch the current selected items voting in data base
    private void getData(String userdata) {
        DatabaseReference databaseReference;
    databaseReference=FirebaseDatabase.getInstance().getReference().child("data");
    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {

            String str= snapshot.child(userdata).getValue().toString();
            num=Integer.parseInt(str);
            num=num+1;
           // textView.setText(String.valueOf(num));
            String temp=userdata;
            UpdateData(temp,str);

        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
         Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_LONG).show();
            }

        });

    }


 //Increment by one in Database
    private void UpdateData(String userdata,String str) {
        DatabaseReference DReference;
        DReference=FirebaseDatabase.getInstance().getReference().child("data");
        HashMap hashMap=new HashMap();
        str=String.valueOf(Integer.parseInt(str)+1);
   //     Toast.makeText(getApplicationContext(),str,Toast.LENGTH_LONG).show();
        hashMap.put(userdata,num);
        DReference.updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {

                dialog.show();
               // Toast.makeText(getApplicationContext(),"Data Updated",Toast.LENGTH_LONG).show();

                return;
            }
        });

    }


    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
        overridePendingTransition(0,0);
        startActivity(getIntent());
        overridePendingTransition(0,0);
        dialog.dismiss();

    }
}
