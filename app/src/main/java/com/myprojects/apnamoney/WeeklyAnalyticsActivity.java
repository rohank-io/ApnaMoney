package com.myprojects.apnamoney;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.anychart.enums.Align;
import com.anychart.enums.LegendLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.joda.time.DateTime;
import org.joda.time.MutableDateTime;
import org.joda.time.Weeks;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;

public class WeeklyAnalyticsActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private FirebaseAuth mAuth;
    private String onlineUserId = "";
    private DatabaseReference expenseRef,personalRef;

    private TextView monthSpentAmount, totalBudgetAmountTextView ,analyticsTransportAmount,analyticsFoodAmount,analyticsHouseAmount,analyticsEntertainmentAmount,
            analyticsEducationAmount,analyticsCharityAmount,analyticsApparelAmount,analyticsHealthAmount,analyticsPersonalAmount,
            analyticsOtherAmount;

    private RelativeLayout linearLayoutAnalysis ,relativeLayoutPersonal,relativeLayoutTransport,relativeLayoutFood,relativeLayoutHouse,relativeLayoutEntertainment,
            relativeLayoutEducation,relativeLayoutCharity,
            relativeLayoutApparel,relativeLayoutHealth,relativeLayoutOther;

    private AnyChartView anyChartView;

    private ImageView monthRatioSpending_image,transport_status,food_status,house_status,entertainment_status,education_status,charity_status,apparel_status,health_status,
            personal_status,other_status;

    private TextView monthRatioSpending,progress_ratio_food,progress_ratio_other,progress_ratio_transport,progress_ratio_house,
            progress_ratio_entertainment,progress_ratio_education,progress_ratio_charity,progress_ratio_apparel,progress_ratio_health,progress_ratio_personal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weekly_analytics);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Weekly Analytics");

        mAuth = FirebaseAuth.getInstance();
        onlineUserId = mAuth.getCurrentUser().getUid();
        expenseRef = FirebaseDatabase.getInstance().getReference("expenses").child(onlineUserId);
        personalRef = FirebaseDatabase.getInstance().getReference("personal").child(onlineUserId);

        monthSpentAmount = findViewById(R.id.monthSpentAmount);
        monthRatioSpending = findViewById(R.id.monthRatioSpending);
        monthRatioSpending_image = findViewById(R.id.monthRatioSpending_image);
        linearLayoutAnalysis = findViewById(R.id.linearLayoutAnalysis);

        totalBudgetAmountTextView = findViewById(R.id.totalBudgetAmountTextView);



        //ImageViews status_image_
        transport_status = findViewById(R.id.transport_status);
        food_status = findViewById(R.id.food_status);
        house_status = findViewById(R.id.house_status);
        entertainment_status = findViewById(R.id.entertainment_status);
        education_status = findViewById(R.id.education_status);
        charity_status = findViewById(R.id.charity_status);
        apparel_status = findViewById(R.id.apparel_status);
        health_status = findViewById(R.id.health_status);
        personal_status = findViewById(R.id.personal_status);
        other_status = findViewById(R.id.other_status);


        //TextViews
        progress_ratio_apparel = findViewById(R.id.progress_ratio_apparel);
        progress_ratio_charity = findViewById(R.id.progress_ratio_charity);
        progress_ratio_education = findViewById(R.id.progress_ratio_education);
        progress_ratio_entertainment = findViewById(R.id.progress_ratio_entertainment);
        progress_ratio_food = findViewById(R.id.progress_ratio_food);
        progress_ratio_health = findViewById(R.id.progress_ratio_health);
        progress_ratio_house = findViewById(R.id.progress_ratio_house);
        progress_ratio_other = findViewById(R.id.progress_ratio_other);
        progress_ratio_personal = findViewById(R.id.progress_ratio_personal);
        progress_ratio_transport = findViewById(R.id.progress_ratio_transport);


//     Linear Layouts

        relativeLayoutPersonal = findViewById(R.id.relativeLayoutPersonal);
        relativeLayoutFood = findViewById(R.id.relativeLayoutFood);
        relativeLayoutHouse = findViewById(R.id.relativeLayoutHouse);
        relativeLayoutEntertainment = findViewById(R.id.relativeLayoutEntertainment);
        relativeLayoutEducation = findViewById(R.id.relativeLayoutEducation);
        relativeLayoutCharity= findViewById(R.id.relativeLayoutCharity);
        relativeLayoutApparel = findViewById(R.id.relativeLayoutApparel);
        relativeLayoutHealth = findViewById(R.id.relativeLayoutHealth);
        relativeLayoutTransport = findViewById(R.id.relativeLayoutTransport);
        relativeLayoutOther = findViewById(R.id.relativeLayoutOther);


//      General Analytic
        analyticsApparelAmount = findViewById(R.id.analyticsApparelAmount);
        analyticsCharityAmount = findViewById(R.id.analyticsCharityAmount);
        analyticsEducationAmount = findViewById(R.id.analyticsEducationAmount);
        analyticsEntertainmentAmount = findViewById(R.id.analyticsEntertainmentAmount);
        analyticsFoodAmount = findViewById(R.id.analyticsFoodAmount);
        analyticsHealthAmount = findViewById(R.id.analyticsHealthAmount);
        analyticsHouseAmount = findViewById(R.id.analyticsHouseAmount);
        analyticsOtherAmount = findViewById(R.id.analyticsOtherAmount);
        analyticsPersonalAmount = findViewById(R.id.analyticsPersonalAmount);
        analyticsTransportAmount = findViewById(R.id.analyticsTransportAmount);

        // AnychartView
        anyChartView = findViewById(R.id.anyChartView);

        getTotalWeekTransportExpenses();

        getTotalWeekFoodExpenses();

        getTotalWeekPersonalExpenses();

        getTotalWeekOtherExpenses();

        getTotalWeekHouseExpenses();
        getTotalWeekHealthExpenses();
        getTotalWeekEntertainmentExpenses();
        getTotalWeekEducationExpenses();
        getTotalWeekCharityExpenses();
        getTotalWeekApparelExpenses();
        getTotalWeekSpending();

        new java.util.Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                loadGraph();
                setStatusAndImageResource();
            }
        },2000
        );

    }

    private void getTotalWeekSpending() {
        MutableDateTime epoch = new MutableDateTime();
        epoch.setDate(0);
        DateTime now = new DateTime();

        Weeks weeks = Weeks.weeksBetween(epoch,now);


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("expenses").child(onlineUserId);
        Query query = reference.orderByChild("week").equalTo(weeks.getWeeks());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount()>0){
                    int totalAmount = 0;

                    for (DataSnapshot ds: dataSnapshot.getChildren()){
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object total = map.get("amount");
                        int pTotal = Integer.parseInt(String.valueOf(total));
                        totalAmount   += pTotal;

                    }
                    totalBudgetAmountTextView.setText("Total weeks spending: ₹"+totalAmount);
                    monthSpentAmount.setText("Total Spent: ₹"+totalAmount);
                }
                else {
                    anyChartView.setVisibility(View.GONE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(WeeklyAnalyticsActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void getTotalWeekApparelExpenses() {
        MutableDateTime epoch = new MutableDateTime();
        epoch.setDate(0);
        DateTime now = new DateTime();

        Weeks weeks = Weeks.weeksBetween(epoch,now);

        String itemNweek = "Apparel"+weeks.getWeeks();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("expenses").child(onlineUserId);
        Query query = reference.orderByChild("itemNweek").equalTo(itemNweek);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    int totalAmount = 0;

                    for (DataSnapshot ds: snapshot.getChildren()){
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object total = map.get("amount");
                        int pTotal = Integer.parseInt(String.valueOf(total));
                        totalAmount += pTotal;
                        analyticsApparelAmount.setText("Spent: "+totalAmount);


                    }


                    personalRef.child("weekApparel").setValue(totalAmount);
                }
                else {
                    relativeLayoutApparel.setVisibility(View.GONE);
                    personalRef.child("weekApparel").setValue(0);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(WeeklyAnalyticsActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getTotalWeekCharityExpenses() {

        MutableDateTime epoch = new MutableDateTime();
        epoch.setDate(0);
        DateTime now = new DateTime();

        Weeks weeks = Weeks.weeksBetween(epoch,now);

        String itemNweek = "Charity"+weeks.getWeeks();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("expenses").child(onlineUserId);
        Query query = reference.orderByChild("itemNweek").equalTo(itemNweek);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    int totalAmount = 0;

                    for (DataSnapshot ds: snapshot.getChildren()){
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object total = map.get("amount");
                        int pTotal = Integer.parseInt(String.valueOf(total));
                        totalAmount += pTotal;
                        analyticsCharityAmount.setText("Spent: "+totalAmount);


                    }


                    personalRef.child("weekCharity").setValue(totalAmount);
                }
                else {
                    relativeLayoutCharity.setVisibility(View.GONE);
                    personalRef.child("weekCharity").setValue(0);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(WeeklyAnalyticsActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getTotalWeekEducationExpenses() {

        MutableDateTime epoch = new MutableDateTime();
        epoch.setDate(0);
        DateTime now = new DateTime();

        Weeks weeks = Weeks.weeksBetween(epoch,now);

        String itemNweek = "Education"+weeks.getWeeks();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("expenses").child(onlineUserId);
        Query query = reference.orderByChild("itemNweek").equalTo(itemNweek);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    int totalAmount = 0;

                    for (DataSnapshot ds: snapshot.getChildren()){
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object total = map.get("amount");
                        int pTotal = Integer.parseInt(String.valueOf(total));
                        totalAmount += pTotal;
                        analyticsEducationAmount.setText("Spent: "+totalAmount);


                    }


                    personalRef.child("weekEducation").setValue(totalAmount);
                }
                else {
                    relativeLayoutEducation.setVisibility(View.GONE);
                    personalRef.child("weekEducation").setValue(0);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(WeeklyAnalyticsActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getTotalWeekEntertainmentExpenses() {
        MutableDateTime epoch = new MutableDateTime();
        epoch.setDate(0);
        DateTime now = new DateTime();

        Weeks weeks = Weeks.weeksBetween(epoch,now);

        String itemNweek = "Entertainment"+weeks.getWeeks();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("expenses").child(onlineUserId);
        Query query = reference.orderByChild("itemNweek").equalTo(itemNweek);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    int totalAmount = 0;

                    for (DataSnapshot ds: snapshot.getChildren()){
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object total = map.get("amount");
                        int pTotal = Integer.parseInt(String.valueOf(total));
                        totalAmount += pTotal;
                        analyticsEntertainmentAmount.setText("Spent: "+totalAmount);


                    }


                    personalRef.child("weekEntertainment").setValue(totalAmount);
                }
                else {
                    relativeLayoutEntertainment.setVisibility(View.GONE);
                    personalRef.child("weekEntertainment").setValue(0);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(WeeklyAnalyticsActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getTotalWeekHealthExpenses() {
        MutableDateTime epoch = new MutableDateTime();
        epoch.setDate(0);
        DateTime now = new DateTime();

        Weeks weeks = Weeks.weeksBetween(epoch,now);

        String itemNweek = "Health"+weeks.getWeeks();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("expenses").child(onlineUserId);
        Query query = reference.orderByChild("itemNweek").equalTo(itemNweek);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    int totalAmount = 0;

                    for (DataSnapshot ds: snapshot.getChildren()){
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object total = map.get("amount");
                        int pTotal = Integer.parseInt(String.valueOf(total));
                        totalAmount += pTotal;
                        analyticsHealthAmount.setText("Spent: "+totalAmount);


                    }


                    personalRef.child("weekHealth").setValue(totalAmount);
                }
                else {
                    relativeLayoutHealth.setVisibility(View.GONE);
                    personalRef.child("weekHealth").setValue(0);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(WeeklyAnalyticsActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getTotalWeekHouseExpenses() {
        MutableDateTime epoch = new MutableDateTime();
        epoch.setDate(0);
        DateTime now = new DateTime();

        Weeks weeks = Weeks.weeksBetween(epoch,now);

        String itemNweek = "House"+weeks.getWeeks();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("expenses").child(onlineUserId);
        Query query = reference.orderByChild("itemNweek").equalTo(itemNweek);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    int totalAmount = 0;

                    for (DataSnapshot ds: snapshot.getChildren()){
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object total = map.get("amount");
                        int pTotal = Integer.parseInt(String.valueOf(total));
                        totalAmount += pTotal;
                        analyticsHouseAmount.setText("Spent: "+totalAmount);


                    }


                    personalRef.child("weekHouse").setValue(totalAmount);
                }
                else {
                    relativeLayoutHouse.setVisibility(View.GONE);
                    personalRef.child("weekHouse").setValue(0);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(WeeklyAnalyticsActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getTotalWeekOtherExpenses() {
        MutableDateTime epoch = new MutableDateTime();
        epoch.setDate(0);
        DateTime now = new DateTime();

        Weeks weeks = Weeks.weeksBetween(epoch,now);

        String itemNweek = "Other"+weeks.getWeeks();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("expenses").child(onlineUserId);
        Query query = reference.orderByChild("itemNweek").equalTo(itemNweek);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    int totalAmount = 0;

                    for (DataSnapshot ds: snapshot.getChildren()){
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object total = map.get("amount");
                        int pTotal = Integer.parseInt(String.valueOf(total));
                        totalAmount += pTotal;
                        analyticsOtherAmount.setText("Spent: "+totalAmount);


                    }


                    personalRef.child("weekOther").setValue(totalAmount);
                }
                else {
                    relativeLayoutOther.setVisibility(View.GONE);
                    personalRef.child("weekFood").setValue(0);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(WeeklyAnalyticsActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getTotalWeekPersonalExpenses() {
        MutableDateTime epoch = new MutableDateTime();
        epoch.setDate(0);
        DateTime now = new DateTime();

        Weeks weeks = Weeks.weeksBetween(epoch,now);

        String itemNweek = "Food"+weeks.getWeeks();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("expenses").child(onlineUserId);
        Query query = reference.orderByChild("itemNweek").equalTo(itemNweek);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    int totalAmount = 0;

                    for (DataSnapshot ds: snapshot.getChildren()){
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object total = map.get("amount");
                        int pTotal = Integer.parseInt(String.valueOf(total));
                        totalAmount += pTotal;
                        analyticsPersonalAmount.setText("Spent: "+totalAmount);


                    }


                    personalRef.child("weekPersonal").setValue(totalAmount);
                }
                else {
                    relativeLayoutPersonal.setVisibility(View.GONE);
                    personalRef.child("weekPersonal").setValue(0);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(WeeklyAnalyticsActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getTotalWeekFoodExpenses() {
        MutableDateTime epoch = new MutableDateTime();
        epoch.setDate(0);
        DateTime now = new DateTime();

        Weeks weeks = Weeks.weeksBetween(epoch,now);

        String itemNweek = "Food"+weeks.getWeeks();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("expenses").child(onlineUserId);
        Query query = reference.orderByChild("itemNweek").equalTo(itemNweek);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    int totalAmount = 0;

                    for (DataSnapshot ds: snapshot.getChildren()){
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object total = map.get("amount");
                        int pTotal = Integer.parseInt(String.valueOf(total));
                        totalAmount += pTotal;
                        analyticsFoodAmount.setText("Spent: "+totalAmount);


                    }


                    personalRef.child("weekFood").setValue(totalAmount);
                }
                else {
                    relativeLayoutFood.setVisibility(View.GONE);
                    personalRef.child("weekFood").setValue(0);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(WeeklyAnalyticsActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getTotalWeekTransportExpenses() {
        MutableDateTime epoch = new MutableDateTime();
        epoch.setDate(0);
        DateTime now = new DateTime();

        Weeks weeks = Weeks.weeksBetween(epoch,now);

        String itemNweek = "Transport"+weeks.getWeeks();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("expenses").child(onlineUserId);
        Query query = reference.orderByChild("itemNweek").equalTo(itemNweek);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    int totalAmount = 0;

                    for (DataSnapshot ds: snapshot.getChildren()){
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object total = map.get("amount");
                        int pTotal = Integer.parseInt(String.valueOf(total));
                        totalAmount += pTotal;
                        analyticsTransportAmount.setText("Spent: "+totalAmount);


                    }


                    personalRef.child("weekTransport").setValue(totalAmount);
                }
                else {
                    relativeLayoutTransport.setVisibility(View.GONE);
                    personalRef.child("weekTransport").setValue(0);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(WeeklyAnalyticsActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void loadGraph(){
        personalRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    int traTotal;
                    if (snapshot.hasChild("weekTransport")){
                        traTotal = Integer.parseInt(snapshot.child("weekTransport").getValue().toString());
                    }else {
                        traTotal = 0;
                    }

                    int foodTotal;
                    if (snapshot.hasChild("weekFood")){
                        foodTotal = Integer.parseInt(snapshot.child("weekFood").getValue().toString());
                    }else {
                        foodTotal = 0;
                    }

                    int houseTotal;
                    if (snapshot.hasChild("weekHouse")){
                        houseTotal = Integer.parseInt(snapshot.child("weekHouse").getValue().toString());
                    }else {
                        houseTotal = 0;
                    }

                    int entTotal;
                    if (snapshot.hasChild("weekEntertainment")){
                        entTotal = Integer.parseInt(snapshot.child("weekEntertainment").getValue().toString());
                    }else {
                        entTotal = 0;
                    }

                    int eduTotal;
                    if (snapshot.hasChild("weekEducation")){
                        eduTotal = Integer.parseInt(snapshot.child("weekEducation").getValue().toString());
                    }else {
                        eduTotal = 0;
                    }

                    int chaTotal;
                    if (snapshot.hasChild("weekCharity")){
                        chaTotal = Integer.parseInt(snapshot.child("weekCharity").getValue().toString());
                    }else {
                        chaTotal = 0;
                    }

                    int appTotal;
                    if (snapshot.hasChild("weekApparel")){
                        appTotal = Integer.parseInt(snapshot.child("weekApparel").getValue().toString());
                    }else {
                        appTotal = 0;
                    }

                    int perTotal;
                    if (snapshot.hasChild("weekPersonal")){
                        perTotal = Integer.parseInt(snapshot.child("weekPersonal").getValue().toString());
                    }else {
                        perTotal = 0;
                    }


                    int helTotal;
                    if (snapshot.hasChild("weekHealth")){
                        helTotal = Integer.parseInt(snapshot.child("weekHealth").getValue().toString());
                    }else {
                        helTotal = 0;
                    }

                    int othTotal;
                    if (snapshot.hasChild("weekOther")){
                        othTotal = Integer.parseInt(snapshot.child("weekOther").getValue().toString());
                    }else {
                        othTotal = 0;
                    }

                    Pie pie = AnyChart.pie();
                    List<DataEntry> data = new ArrayList<>();
                    data.add(new ValueDataEntry("Transport",traTotal));
                    data.add(new ValueDataEntry("House",houseTotal));
                    data.add(new ValueDataEntry("Food",foodTotal));
                    data.add(new ValueDataEntry("Entertainment",entTotal));
                    data.add(new ValueDataEntry("Education",eduTotal));
                    data.add(new ValueDataEntry("Charity",chaTotal));
                    data.add(new ValueDataEntry("Apparel",appTotal));
                    data.add(new ValueDataEntry("Health",helTotal));
                    data.add(new ValueDataEntry("Other",othTotal));
                    data.add(new ValueDataEntry("Personal",perTotal));


                    pie.data(data);
                    pie.title("Week Analytics");
                    pie.labels().position("outside");

                    pie.legend().title().enabled(true);
                    pie.legend().title()
                            .text("Items Spent On")
                            .padding(0d,0d,10d,0d);

                    pie.legend()
                            .position("center.bottom")
                            .itemsLayout(LegendLayout.HORIZONTAL)
                            .align(Align.CENTER);

                    anyChartView.setChart(pie);


                }else {
                    Toast.makeText(WeeklyAnalyticsActivity.this,"Child does not exist",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(WeeklyAnalyticsActivity.this,"Child does not exist",Toast.LENGTH_SHORT).show();

            }
        });
    }

    private  void setStatusAndImageResource(){
        personalRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    float traTotal;
                    if (snapshot.hasChild("weekTransport")){
                        traTotal = Integer.parseInt(snapshot.child("weekTransport").getValue().toString());
                    }else {
                        traTotal = 0;

                    }

                    float houseTotal;
                    if (snapshot.hasChild("weekHouse")){
                        houseTotal = Integer.parseInt(snapshot.child("weekHouse").getValue().toString());
                    }else {
                        houseTotal = 0;

                    }

                    float foodTotal;
                    if (snapshot.hasChild("weekFood")){
                        foodTotal = Integer.parseInt(snapshot.child("weekFood").getValue().toString());
                    }else {
                        foodTotal = 0;

                    }

                    float entTotal;
                    if (snapshot.hasChild("weekEntertainment")){
                        entTotal = Integer.parseInt(snapshot.child("weekEntertainment").getValue().toString());
                    }else {
                        entTotal = 0;

                    }

                    float eduTotal;
                    if (snapshot.hasChild("weekEducation")){
                        eduTotal = Integer.parseInt(snapshot.child("weekEducation").getValue().toString());
                    }else {
                        eduTotal = 0;

                    }

                    float chaTotal;
                    if (snapshot.hasChild("weekCharity")){
                        chaTotal = Integer.parseInt(snapshot.child("weekCharity").getValue().toString());
                    }else {
                        chaTotal = 0;

                    }

                    float appTotal;
                    if (snapshot.hasChild("weekApparel")){
                        appTotal = Integer.parseInt(snapshot.child("weekApparel").getValue().toString());
                    }else {
                        appTotal = 0;

                    }

                    float helTotal;
                    if (snapshot.hasChild("weekHealth")){
                        helTotal = Integer.parseInt(snapshot.child("weekHealth").getValue().toString());
                    }else {
                        helTotal = 0;

                    }

                    float otherTotal;
                    if (snapshot.hasChild("weekOther")){
                        otherTotal = Integer.parseInt(snapshot.child("weekOther").getValue().toString());
                    }else {
                        otherTotal = 0;

                    }

                    float perTotal;
                    if (snapshot.hasChild("weekPersonal")){
                        perTotal = Integer.parseInt(snapshot.child("weekPersonal").getValue().toString());
                    }else {
                        perTotal = 0;

                    }
                    float monthTotalSpentAmount;
                    if (snapshot.hasChild("week")){
                        monthTotalSpentAmount = Integer.parseInt(snapshot.child("week").getValue().toString());
                    }else {
                        monthTotalSpentAmount = 0;

                    }


                    // Geting Ratios

                    float transportRatio;
                    if (snapshot.hasChild("weekTransRatio")){
                        transportRatio = Integer.parseInt(snapshot.child("weekTransRatio").getValue().toString());
                    }else {
                        transportRatio = 0;

                    }

                    float houseRatio;
                    if (snapshot.hasChild("weekHouseRatio")){
                        houseRatio = Integer.parseInt(snapshot.child("weekHouseRatio").getValue().toString());
                    }else {
                        houseRatio = 0;

                    }
                    float foodRatio;
                    if (snapshot.hasChild("weekFoodRatio")){
                        foodRatio = Integer.parseInt(snapshot.child("weekFoodRatio").getValue().toString());
                    }else {
                        foodRatio = 0;

                    }

                    float entRatio;
                    if (snapshot.hasChild("weekEntRatio")){
                        entRatio = Integer.parseInt(snapshot.child("weekEntRatio").getValue().toString());
                    }else {
                        entRatio = 0;

                    }

                    float eduRatio;
                    if (snapshot.hasChild("monthEduRatio")){
                        eduRatio = Integer.parseInt(snapshot.child("monthEduRatio").getValue().toString());
                    }else {
                        eduRatio = 0;

                    }

                    float charRatio;
                    if (snapshot.hasChild("monthCharityRatio")){
                        charRatio = Integer.parseInt(snapshot.child("monthCharityRatio").getValue().toString());
                    }else {
                        charRatio = 0;

                    }

                    float appRatio;
                    if (snapshot.hasChild("monthApparelRatio")){
                        appRatio = Integer.parseInt(snapshot.child("monthApparelRatio").getValue().toString());
                    }else {
                        appRatio = 0;

                    }

                    float helRatio;
                    if (snapshot.hasChild("monthHealthRatio")){
                        helRatio = Integer.parseInt(snapshot.child("monthHealthRatio").getValue().toString());
                    }else {
                        helRatio = 0;

                    }

                    float otherRatio;
                    if (snapshot.hasChild("monthOtherRatio")){
                        otherRatio = Integer.parseInt(snapshot.child("monthOtherRatio").getValue().toString());
                    }else {
                        otherRatio = 0;

                    }

                    float perRatio;
                    if (snapshot.hasChild("monthPersonalRatio")){
                        perRatio = Integer.parseInt(snapshot.child("monthPersonalRatio").getValue().toString());
                    }else {
                        perRatio = 0;

                    }

                    float monthTotalSpentAmountRatio;
                    if (snapshot.hasChild("budget")){
                        monthTotalSpentAmountRatio = Integer.parseInt(snapshot.child("budget").getValue().toString());
                    }else {
                        monthTotalSpentAmountRatio = 0;

                    }


                    float monthPercent = (monthTotalSpentAmount/monthTotalSpentAmountRatio)*100;
                    if (monthPercent<50){
                        monthRatioSpending.setText(monthPercent+ "%"+" used of "+monthTotalSpentAmountRatio+". status");
                        monthRatioSpending_image.setImageResource(R.drawable.green);

                    }else if (monthPercent>=50 && monthPercent<100){
                        monthRatioSpending.setText(monthPercent+ "%"+" used of "+monthTotalSpentAmountRatio+". status");
                        monthRatioSpending_image.setImageResource(R.drawable.brown);
                    }else {
                        monthRatioSpending.setText(monthPercent+ "%"+" used of "+monthTotalSpentAmountRatio+". status");
                        monthRatioSpending_image.setImageResource(R.drawable.red);
                    }

                    float transportPercent = (traTotal/transportRatio)*100;
                    if (transportPercent<50){
                        progress_ratio_transport.setText(transportPercent+ "%"+" used of "+transportRatio+". status");
                        transport_status.setImageResource(R.drawable.green);

                    }else if (transportPercent>=50 && transportPercent<100){
                        progress_ratio_transport.setText(transportPercent+ "%"+" used of "+transportRatio+". status");
                        transport_status.setImageResource(R.drawable.brown);
                    }else {
                        progress_ratio_transport.setText(transportPercent+ "%"+" used of "+transportRatio+". status");
                        transport_status.setImageResource(R.drawable.red);
                    }


                    float housePercent = (houseTotal/houseRatio)*100;
                    if (housePercent<50){
                        progress_ratio_house.setText(housePercent+ "%"+" used of "+houseRatio+". status");
                        house_status.setImageResource(R.drawable.green);

                    }else if (housePercent>=50 && housePercent<100){
                        progress_ratio_house.setText(housePercent+ "%"+" used of "+houseRatio+". status");
                        house_status.setImageResource(R.drawable.brown);
                    }else {
                        progress_ratio_house.setText(housePercent+ "%"+" used of "+houseRatio+". status");
                        house_status.setImageResource(R.drawable.red);
                    }

                    float foodPercent = (foodTotal/foodRatio)*100;
                    if (foodPercent<50){
                        progress_ratio_food.setText(foodPercent+ "%"+" used of "+foodRatio+". status");
                        food_status.setImageResource(R.drawable.green);

                    }else if (foodPercent>=50 && foodPercent<100){
                        progress_ratio_food.setText(foodPercent+ "%"+" used of "+foodRatio+". status");
                        food_status.setImageResource(R.drawable.brown);
                    }else {
                        progress_ratio_food.setText(foodPercent+ "%"+" used of "+foodRatio+". status");
                        food_status.setImageResource(R.drawable.red);
                    }

                    float entPercent = (entTotal/entRatio)*100;
                    if (entPercent<50){
                        progress_ratio_entertainment.setText(entPercent+ "%"+" used of "+entRatio+". status");
                        entertainment_status.setImageResource(R.drawable.green);

                    }else if (entPercent>=50 && entPercent<100){
                        progress_ratio_entertainment.setText(entPercent+ "%"+" used of "+entRatio+". status");
                        entertainment_status.setImageResource(R.drawable.brown);
                    }else {
                        progress_ratio_entertainment.setText(entPercent+ "%"+" used of "+entRatio+". status");
                        entertainment_status.setImageResource(R.drawable.red);
                    }

                    float eduPercent = (eduTotal/eduRatio)*100;
                    if (eduPercent<50){
                        progress_ratio_education.setText(eduPercent+ "%"+" used of "+eduRatio+". status");
                        education_status.setImageResource(R.drawable.green);

                    }else if (eduPercent>=50 && eduPercent<100){
                        progress_ratio_education.setText(eduPercent+ "%"+" used of "+eduRatio+". status");
                        education_status.setImageResource(R.drawable.brown);
                    }else {
                        progress_ratio_education.setText(eduPercent+ "%"+" used of "+eduRatio+". status");
                        education_status.setImageResource(R.drawable.red);
                    }

                    float chaPercent = (chaTotal/charRatio)*100;
                    if (chaPercent<50){
                        progress_ratio_charity.setText(chaPercent+ "%"+" used of "+charRatio+". status");
                        charity_status.setImageResource(R.drawable.green);

                    }else if (chaPercent>=50 && chaPercent<100){
                        progress_ratio_charity.setText(chaPercent+ "%"+" used of "+charRatio+". status");
                        charity_status.setImageResource(R.drawable.brown);
                    }else {
                        progress_ratio_charity.setText(chaPercent+ "%"+" used of "+charRatio+". status");
                        charity_status.setImageResource(R.drawable.red);
                    }

                    float apparelPercent = (appTotal/appRatio)*100;
                    if (apparelPercent<50){
                        progress_ratio_apparel.setText(apparelPercent+ "%"+" used of "+appRatio+". status");
                        apparel_status.setImageResource(R.drawable.green);

                    }else if (apparelPercent>=50 && apparelPercent<100){
                        progress_ratio_apparel.setText(apparelPercent+ "%"+" used of "+appRatio+". status");
                        apparel_status.setImageResource(R.drawable.brown);
                    }else {
                        progress_ratio_apparel.setText(apparelPercent+ "%"+" used of "+appRatio+". status");
                        apparel_status.setImageResource(R.drawable.red);
                    }

                    float healthPercent = (helTotal/helRatio)*100;
                    if (healthPercent<50){
                        progress_ratio_health.setText(healthPercent+ "%"+" used of "+helRatio+". status");
                        health_status.setImageResource(R.drawable.green);

                    }else if (healthPercent>=50 && healthPercent<100){
                        progress_ratio_health.setText(healthPercent+ "%"+" used of "+helRatio+". status");
                        health_status.setImageResource(R.drawable.brown);
                    }else {
                        progress_ratio_health.setText(healthPercent+ "%"+" used of "+helRatio+". status");
                        health_status.setImageResource(R.drawable.red);
                    }

                    float otherPercent = (otherTotal/otherRatio)*100;
                    if (otherPercent<50){
                        progress_ratio_other.setText(otherPercent+ "%"+" used of "+otherRatio+". status");
                        other_status.setImageResource(R.drawable.green);

                    }else if (otherPercent>=50 && otherPercent<100){
                        progress_ratio_other.setText(otherPercent+ "%"+" used of "+otherRatio+". status");
                        other_status.setImageResource(R.drawable.brown);
                    }else {
                        progress_ratio_other.setText(otherPercent+ "%"+" used of "+otherRatio+". status");
                        other_status.setImageResource(R.drawable.red);
                    }

                    float personalPercent = (perTotal/perRatio)*100;
                    if (personalPercent<50){
                        progress_ratio_personal.setText(personalPercent+ "%"+" used of "+perRatio+". status");
                        personal_status.setImageResource(R.drawable.green);

                    }else if (personalPercent>=50 && personalPercent<100){
                        progress_ratio_personal.setText(personalPercent+ "%"+" used of "+perRatio+". status");
                        personal_status.setImageResource(R.drawable.brown);
                    }else {
                        progress_ratio_personal.setText(personalPercent+ "%"+" used of "+perRatio+". status");
                        personal_status.setImageResource(R.drawable.red);
                    }





                }else {
                     Toast.makeText(WeeklyAnalyticsActivity.this,"setStatusAndImageResource Errors",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}