package com.hardy.opinionpoll;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.collection.LLRBNode;

import java.util.ArrayDeque;
import java.util.ArrayList;

public class BarDisplay extends AppCompatActivity {
    String[] langs = new String[]{ "C++", "JAVA", "PYTHON", "REACT"," " };
    BarChart barChart;

    // TextView txtv;
int i1,i2,i3,i4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_display);

        barChart = (BarChart) findViewById(R.id.bar_chart);
        getDatabase();



    }
//Fetch data to show in graph
    private void getDatabase() {

        DatabaseReference databaseReference;
        databaseReference = FirebaseDatabase.getInstance().getReference().child("data");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String a1 = snapshot.child("Cplus").getValue().toString();
                String a2 = snapshot.child("Java").getValue().toString();
                String a3 = snapshot.child("Python").getValue().toString();
                String a4 = snapshot.child("React").getValue().toString();

                i1=Integer.parseInt(a1);
                i2=Integer.parseInt(a2);
                i3=Integer.parseInt(a3);
                i4=Integer.parseInt(a4);
                    creategraph();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();
            }

        });
    }
    
    
    //Create graph
    private void creategraph(){

        ArrayList<BarEntry> info = new ArrayList<>();
        info.add(new BarEntry(1, i1));
        info.add(new BarEntry(2, i2));
        info.add(new BarEntry(3, i3));
        info.add(new BarEntry(4, i4));

    BarDataSet dataSet = new BarDataSet(info, "Result");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.getValueTextColor(Color.BLACK);
        dataSet.setValueTextSize(18f);


    BarData barData = new BarData(dataSet);
        barChart.setFitBars(true);
        barChart.setData(barData);
     //   barChart.getDescription().setText("Polling Result");
        barChart.getDescription().setEnabled(false);
        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(langs));
        xAxis.setCenterAxisLabels(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1);
        xAxis.setGranularityEnabled(true);
        barChart.animateY(1000);
        barChart.invalidate();




        }



}