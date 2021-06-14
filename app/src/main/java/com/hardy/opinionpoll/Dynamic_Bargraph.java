package com.hardy.opinionpoll;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Dynamic_Bargraph extends AppCompatActivity {

    ArrayList<String> oPtions = new ArrayList<String>();
    ArrayList<Integer> vAlues = new ArrayList<Integer>();

    BarChart barChart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic__bargraph);
        barChart = (BarChart) findViewById(R.id.dynamic_barChart);
        getDatabase();



    }
    //Fetch data to show in graph
    private void getDatabase() {

        DatabaseReference databaseReference;
        databaseReference = FirebaseDatabase.getInstance().getReference().child("questions");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds:snapshot.child("ques1").child("options").getChildren())
                {
                   oPtions.add(ds.getKey().toString());
                   vAlues.add(Integer.parseInt(String.valueOf(ds.getValue())));

                }
             //   tv.setText("");
           //     for (int i = 0; i < vAlues.size(); i++){
             //   tv.append(vAlues.get(i)+"\n");
             //   }
            //    Toast.makeText(Dynamic_Bargraph.this,oPtions.get(0),Toast.LENGTH_LONG).show();
                creategraph(vAlues,oPtions);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();
            }

        });
    }


    //Create graph
    private void creategraph(ArrayList<Integer>vAlues,ArrayList<String>oPtions) {

        ArrayList<BarEntry> info = new ArrayList<>();

        for (int i = 0; i < vAlues.size(); i++){
            info.add(new BarEntry(i+1, vAlues.get(i)));
        }

        BarDataSet dataSet = new BarDataSet(info, "Result");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.getValueTextColor(Color.BLACK);
        dataSet.setValueTextSize(18f);


        BarData barData = new BarData(dataSet);
        barChart.setFitBars(true);
        barChart.setData(barData);
        barChart.getDescription().setEnabled(false);
        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(oPtions));
        xAxis.setCenterAxisLabels(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1);
        xAxis.setGranularityEnabled(true);
        barChart.animateY(1000);
        barChart.invalidate();


    }

    }



