package com.myprojects.apnamoney;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ChooseAnalyticActivity extends AppCompatActivity {


    private CardView todayCardView , weekCardView, monthCardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_analytic);
        weekCardView = findViewById(R.id.weekCardView);
        todayCardView = findViewById(R.id.todayCardView);
        monthCardView = findViewById(R.id.monthCardView);

        todayCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChooseAnalyticActivity.this,DailyAnalyticsActivity.class);
                startActivity(intent);

            }
        });

        weekCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChooseAnalyticActivity.this,WeeklyAnalyticsActivity.class);
                startActivity(intent);

            }
        });

        monthCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChooseAnalyticActivity.this,MonthlyAnalyticsActivity.class);
                startActivity(intent);

            }
        });
    }
}