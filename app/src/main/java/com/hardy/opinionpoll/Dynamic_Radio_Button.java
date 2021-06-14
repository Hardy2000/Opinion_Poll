package com.hardy.opinionpoll;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class Dynamic_Radio_Button extends AppCompatActivity {

    TextView textView;
    RadioGroup radioGroup;
    Button btn_radio_dynamic;
    Dialog dialog;
   // int num;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic__radio__button);

          textView=findViewById(R.id.textView);
          radioGroup=findViewById(R.id.radio_Group_dynamic);
          radioGroup.setOrientation(LinearLayout.VERTICAL);
          btn_radio_dynamic=findViewById(R.id.submit_dynamic_radio);
        dialog=new Dialog(Dynamic_Radio_Button.this);
        dialog.setContentView(R.layout.custom_dialog);
        dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.backgrouund));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);

        Button d_retry=dialog.findViewById(R.id.dialog_retry);
        Button d_result=dialog.findViewById(R.id.dialog_result);
        TextView d_view=dialog.findViewById(R.id.dialog_text);

    d_view.setText("Response Saved");


//
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
                Intent intent=new Intent(Dynamic_Radio_Button.this,Dynamic_Bargraph.class);
                startActivity(intent);
                dialog.dismiss();
            }
        });


        DatabaseReference databaseReference;
        databaseReference= FirebaseDatabase.getInstance().getReference();
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String str=snapshot.child("questions").child("ques1").child("qtext").getValue().toString();
                textView.setText(str);
                for (DataSnapshot ds:snapshot.child("questions").child("ques1").child("options").getChildren())
                {
                    String key=ds.getKey();
               //     textView.append("\n"+key);
                    RadioButton radio_btn = new RadioButton(Dynamic_Radio_Button.this);
                    radio_btn.setId(View.generateViewId());
                    radio_btn.setTextSize(20);
                    radio_btn.setText(key);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.WRAP_CONTENT, 1f);
                    radio_btn.setLayoutParams(params);
                    radioGroup.addView(radio_btn);
                }

                btn_radio_dynamic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int selectedId =radioGroup.getCheckedRadioButtonId();

                        // find the radio button by returned id
                        RadioButton radioButton = (RadioButton) findViewById(selectedId);
                        String passvalue=snapshot.child("questions").child("ques1").child("options").child(radioButton.getText().toString()).getValue().toString();

                       UpdateData(radioButton.getText().toString(),passvalue);
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_LONG).show();
            }
        });

    }


   //Increment by one in Database
    private void UpdateData(String name,String value) {
        DatabaseReference DReference;
        DReference=FirebaseDatabase.getInstance().getReference().child("questions").child("ques1").child("options");
        HashMap hashMap=new HashMap();
         hashMap.put(name,Integer.parseInt(value)+1);
        DReference.updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {

                dialog.show();
                // Toast.makeText(getApplicationContext(),"Data Updated",Toast.LENGTH_LONG).show();

                return;
            }
        });

    }


    public void openBargrapg(View view) {

        Intent intent=new Intent(Dynamic_Radio_Button.this,Dynamic_Bargraph.class);
        startActivity(intent);
        dialog.dismiss();
        finish();
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