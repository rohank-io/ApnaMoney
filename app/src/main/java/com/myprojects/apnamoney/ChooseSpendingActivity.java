package com.myprojects.apnamoney;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class ChooseSpendingActivity extends AppCompatActivity {

    private ImageView imgToday,imgWeek,imgMonth;

    private CardView todayCardView , weekCardView, monthCardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_spending);

        todayCardView = findViewById(R.id.todayCardView);
        weekCardView = findViewById(R.id.weekCardView);
        monthCardView = findViewById(R.id.monthCardView);


        todayCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChooseSpendingActivity.this,TodaySpendingActivity.class);
                startActivity(intent);

            }
        });

        weekCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChooseSpendingActivity.this,WeekSpendingActivity.class);
                intent.putExtra("type","week");
                startActivity(intent);

            }
        });

        monthCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChooseSpendingActivity.this,WeekSpendingActivity.class);
                intent.putExtra("type","month");
                startActivity(intent);

            }
        });
    }
}