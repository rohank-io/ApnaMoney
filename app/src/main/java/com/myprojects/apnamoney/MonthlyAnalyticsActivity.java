package com.myprojects.apnamoney;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.google.firebase.messaging.FirebaseMessaging;

import org.joda.time.DateTime;
import org.joda.time.Months;
import org.joda.time.MutableDateTime;
import org.joda.time.Weeks;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;

public class MonthlyAnalyticsActivity extends AppCompatActivity {

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

    private Button downloadBtn;
    private Bitmap bitmap;
    private LinearLayout linearId;

    private AnyChartView anyChartView;

    private ImageView monthRatioSpending_image,transport_status,food_status,house_status,entertainment_status,education_status,charity_status,apparel_status,health_status,
            personal_status,other_status;

    private TextView monthRatioSpending,progress_ratio_food,progress_ratio_other,progress_ratio_transport,progress_ratio_house,
            progress_ratio_entertainment,progress_ratio_education,progress_ratio_charity,progress_ratio_apparel,progress_ratio_health,progress_ratio_personal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly_analytics);

        // Notification



        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Monthly Analytics");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                finish();
            }
        });

        mAuth = FirebaseAuth.getInstance();
        onlineUserId = mAuth.getCurrentUser().getUid();
        expenseRef = FirebaseDatabase.getInstance().getReference("expenses").child(onlineUserId);
        personalRef = FirebaseDatabase.getInstance().getReference("personal").child(onlineUserId);

        monthSpentAmount = findViewById(R.id.monthSpentAmount);
        monthRatioSpending = findViewById(R.id.monthRatioSpending);
        monthRatioSpending_image = findViewById(R.id.monthRatioSpending_image);
        linearLayoutAnalysis = findViewById(R.id.linearLayoutAnalysis);

        totalBudgetAmountTextView = findViewById(R.id.totalBudgetAmountTextView);

        anyChartView = findViewById(R.id.anyChartView);

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


//     Download Button
        downloadBtn = findViewById(R.id.downloadBtn);
        linearId = findViewById(R.id.linearId);

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
            },1000
        );


        downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("size",""+linearId.getWidth()+" "+linearId.getWidth());
                bitmap = LoadBitmap(linearId,linearId.getWidth(),linearId.getHeight());
                createPdf();

            }
        });




    }

    private Bitmap LoadBitmap(View v, int width, int height) {
        Bitmap bitmap = Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        v.draw(canvas);
        return bitmap;
    }

    private void createPdf() {

        WindowManager windowManager = (WindowManager)getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        float width = displayMetrics.widthPixels;
        float height = displayMetrics.heightPixels;
        int convertWidth = (int)width,convertHeight = (int)height;

        PdfDocument document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(convertWidth,convertHeight,3).create();
        PdfDocument.Page page = document.startPage(pageInfo);
        Canvas canvas = page.getCanvas();
        Paint paint = new Paint();
        canvas.drawPaint(paint);
        bitmap = Bitmap.createScaledBitmap(bitmap,convertWidth,convertHeight,true);

        canvas.drawBitmap(bitmap,0,0,null);
        document.finishPage(page);

        //Target pdf download
       // String targetPdf = "/Pictures/page.pdf";

        File file;
        file = new File(getFilePath());
        try {
            document.writeTo(new FileOutputStream(file));
        }catch (IOException e){
            e.printStackTrace();
            Toast.makeText(this,"something wrong try again"+e.toString(),Toast.LENGTH_LONG).show();

            //After Close the document

            document.close();
            Toast.makeText(this,"Successfully download",Toast.LENGTH_SHORT).show();

            openPdf();//Used to open PDF file that is downloaded


        }

    }

    private void openPdf() {
        //File file = new File(getFilePath());
        String path = "/storage/Android/com.myprojects.apnamoney/files/documents";
        File file = new File(path);
        if (file.exists()){
            Intent intent = new Intent(Intent.ACTION_VIEW);
            Uri uri = Uri.fromFile(file);
            intent.setDataAndType(uri,"application/pdf");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            try {
                startActivity(intent);
            }catch (ActivityNotFoundException e){
                Toast.makeText(this,"No Application for pdf view",Toast.LENGTH_SHORT).show();

            }
        }
    }

    private String getFilePath(){
        ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
        File getFileDirectory = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        File file2 = new File(getFileDirectory,"page"+".pdf");
        return file2.getPath();
    }

    private void getTotalWeekSpending() {
        MutableDateTime epoch = new MutableDateTime();
        epoch.setDate(0);
        DateTime now = new DateTime();

        Months months = Months.monthsBetween(epoch,now);


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("expenses").child(onlineUserId);
        Query query = reference.orderByChild("month").equalTo(months.getMonths());
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
                    totalBudgetAmountTextView.setText("Total Month's spending: ₹"+totalAmount);
                    monthSpentAmount.setText("Total Spent: ₹"+totalAmount);
                }
                else {
                    anyChartView.setVisibility(View.GONE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MonthlyAnalyticsActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getTotalWeekApparelExpenses() {
        MutableDateTime epoch = new MutableDateTime();
        epoch.setDate(0);
        DateTime now = new DateTime();

        Months months = Months.monthsBetween(epoch,now);

        String itemNmonth = "Apparel"+months.getMonths();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("expenses").child(onlineUserId);
        Query query = reference.orderByChild("itemNmonth").equalTo(itemNmonth);
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


                    personalRef.child("monthApparel").setValue(totalAmount);
                }
                else {
                    relativeLayoutApparel.setVisibility(View.GONE);
                    personalRef.child("monthApparel").setValue(0);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MonthlyAnalyticsActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getTotalWeekCharityExpenses() {
        MutableDateTime epoch = new MutableDateTime();
        epoch.setDate(0);
        DateTime now = new DateTime();

        Months months = Months.monthsBetween(epoch,now);

        String itemNmonth = "Charity"+months.getMonths();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("expenses").child(onlineUserId);
        Query query = reference.orderByChild("itemNmonth").equalTo(itemNmonth);
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


                    personalRef.child("monthCharity").setValue(totalAmount);
                }
                else {
                    relativeLayoutCharity.setVisibility(View.GONE);
                    personalRef.child("monthCharity").setValue(0);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MonthlyAnalyticsActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getTotalWeekEducationExpenses() {
        MutableDateTime epoch = new MutableDateTime();
        epoch.setDate(0);
        DateTime now = new DateTime();

        Months months = Months.monthsBetween(epoch,now);

        String itemNmonth = "Education"+months.getMonths();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("expenses").child(onlineUserId);
        Query query = reference.orderByChild("itemNmonth").equalTo(itemNmonth);
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


                    personalRef.child("monthEducation").setValue(totalAmount);
                }
                else {
                    relativeLayoutEducation.setVisibility(View.GONE);
                    personalRef.child("monthEducation").setValue(0);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MonthlyAnalyticsActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getTotalWeekEntertainmentExpenses() {
        MutableDateTime epoch = new MutableDateTime();
        epoch.setDate(0);
        DateTime now = new DateTime();

        Months months = Months.monthsBetween(epoch,now);

        String itemNmonth = "Entertainment"+months.getMonths();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("expenses").child(onlineUserId);
        Query query = reference.orderByChild("itemNmonth").equalTo(itemNmonth);
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


                    personalRef.child("monthEntertainment").setValue(totalAmount);
                }
                else {
                    relativeLayoutEntertainment.setVisibility(View.GONE);
                    personalRef.child("monthEntertainment").setValue(0);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MonthlyAnalyticsActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void getTotalWeekHealthExpenses() {
        MutableDateTime epoch = new MutableDateTime();
        epoch.setDate(0);
        DateTime now = new DateTime();

        Months months = Months.monthsBetween(epoch,now);

        String itemNmonth = "Health"+months.getMonths();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("expenses").child(onlineUserId);
        Query query = reference.orderByChild("itemNmonth").equalTo(itemNmonth);
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


                    personalRef.child("monthHealth").setValue(totalAmount);
                }
                else {
                    relativeLayoutHealth.setVisibility(View.GONE);
                    personalRef.child("monthHealth").setValue(0);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MonthlyAnalyticsActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getTotalWeekHouseExpenses() {
        MutableDateTime epoch = new MutableDateTime();
        epoch.setDate(0);
        DateTime now = new DateTime();

        Months months = Months.monthsBetween(epoch,now);

        String itemNmonth = "House"+months.getMonths();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("expenses").child(onlineUserId);
        Query query = reference.orderByChild("itemNmonth").equalTo(itemNmonth);
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


                    personalRef.child("monthHouse").setValue(totalAmount);
                }
                else {
                    relativeLayoutHouse.setVisibility(View.GONE);
                    personalRef.child("monthHouse").setValue(0);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MonthlyAnalyticsActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getTotalWeekOtherExpenses() {
        MutableDateTime epoch = new MutableDateTime();
        epoch.setDate(0);
        DateTime now = new DateTime();

        Months months = Months.monthsBetween(epoch,now);

        String itemNmonth = "Other"+months.getMonths();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("expenses").child(onlineUserId);
        Query query = reference.orderByChild("itemNmonth").equalTo(itemNmonth);
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


                    personalRef.child("monthOther").setValue(totalAmount);
                }
                else {
                    relativeLayoutOther.setVisibility(View.GONE);
                    personalRef.child("monthOther").setValue(0);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MonthlyAnalyticsActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getTotalWeekPersonalExpenses() {
        MutableDateTime epoch = new MutableDateTime();
        epoch.setDate(0);
        DateTime now = new DateTime();

        Months months = Months.monthsBetween(epoch,now);

        String itemNmonth = "Personal"+months.getMonths();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("expenses").child(onlineUserId);
        Query query = reference.orderByChild("itemNmonth").equalTo(itemNmonth);
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


                    personalRef.child("monthPersonal").setValue(totalAmount);
                }
                else {
                    relativeLayoutPersonal.setVisibility(View.GONE);
                    personalRef.child("monthPersonal").setValue(0);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MonthlyAnalyticsActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getTotalWeekFoodExpenses() {
        MutableDateTime epoch = new MutableDateTime();
        epoch.setDate(0);
        DateTime now = new DateTime();

        Months months = Months.monthsBetween(epoch,now);

        String itemNmonth = "Food"+months.getMonths();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("expenses").child(onlineUserId);
        Query query = reference.orderByChild("itemNmonth").equalTo(itemNmonth);
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


                    personalRef.child("monthFood").setValue(totalAmount);
                }
                else {
                    relativeLayoutFood.setVisibility(View.GONE);
                    personalRef.child("monthFood").setValue(0);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MonthlyAnalyticsActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getTotalWeekTransportExpenses() {
        MutableDateTime epoch = new MutableDateTime();
        epoch.setDate(0);
        DateTime now = new DateTime();

        Months months = Months.monthsBetween(epoch,now);

        String itemNmonth = "Transport"+months.getMonths();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("expenses").child(onlineUserId);
        Query query = reference.orderByChild("itemNmonth").equalTo(itemNmonth);
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


                    personalRef.child("monthTransport").setValue(totalAmount);
                }
                else {
                    relativeLayoutTransport.setVisibility(View.GONE);
                    personalRef.child("monthTransport").setValue(0);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MonthlyAnalyticsActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void loadGraph() {
        personalRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    int traTotal;
                    if (snapshot.hasChild("monthTransport")) {
                        traTotal = Integer.parseInt(snapshot.child("monthTransport").getValue().toString());
                    } else {
                        traTotal = 0;
                    }

                    int foodTotal;
                    if (snapshot.hasChild("monthFood")) {
                        foodTotal = Integer.parseInt(snapshot.child("monthFood").getValue().toString());
                    } else {
                        foodTotal = 0;
                    }

                    int houseTotal;
                    if (snapshot.hasChild("monthHouse")) {
                        houseTotal = Integer.parseInt(snapshot.child("monthHouse").getValue().toString());
                    } else {
                        houseTotal = 0;
                    }

                    int entTotal;
                    if (snapshot.hasChild("monthEntertainment")) {
                        entTotal = Integer.parseInt(snapshot.child("monthEntertainment").getValue().toString());


                    } else {
                        entTotal = 0;
                    }

                    int eduTotal;
                    if (snapshot.hasChild("monthEducation")) {
                        eduTotal = Integer.parseInt(snapshot.child("monthEducation").getValue().toString());
                    } else {
                        eduTotal = 0;
                    }
                    int chaTotal;
                    if (snapshot.hasChild("monthCharity")) {
                        chaTotal = Integer.parseInt(snapshot.child("monthCharity").getValue().toString());
                    } else {
                        chaTotal = 0;
                    }

                    int appTotal;
                    if (snapshot.hasChild("monthApparel")) {
                        appTotal = Integer.parseInt(snapshot.child("monthApparel").getValue().toString());
                    } else {
                        appTotal = 0;
                    }

                    int perTotal;
                    if (snapshot.hasChild("monthPersonal")) {
                        perTotal = Integer.parseInt(snapshot.child("monthPersonal").getValue().toString());
                    } else {
                        perTotal = 0;
                    }


                    int helTotal;
                    if (snapshot.hasChild("monthHealth")) {
                        helTotal = Integer.parseInt(snapshot.child("monthHealth").getValue().toString());
                    } else {
                        helTotal = 0;
                    }

                    int othTotal;
                    if (snapshot.hasChild("monthOther")) {
                        othTotal = Integer.parseInt(snapshot.child("monthOther").getValue().toString());
                    } else {
                        othTotal = 0;
                    }

                    Pie pie = AnyChart.pie();
                    List<DataEntry> data = new ArrayList<>();
                    data.add(new ValueDataEntry("Transport", traTotal));
                    data.add(new ValueDataEntry("House", houseTotal));
                    data.add(new ValueDataEntry("Food", foodTotal));
                    data.add(new ValueDataEntry("Entertainment", entTotal));
                    data.add(new ValueDataEntry("Education", eduTotal));
                    data.add(new ValueDataEntry("Charity", chaTotal));
                    data.add(new ValueDataEntry("Apparel", appTotal));
                    data.add(new ValueDataEntry("Health", helTotal));
                    data.add(new ValueDataEntry("Other", othTotal));
                    data.add(new ValueDataEntry("Personal", perTotal));


                    pie.data(data);
                    pie.title("Monthly Analytics");
                    pie.labels().position("outside");

                    pie.legend().title().enabled(true);
                    pie.legend().title()
                            .text("Items Spent On")
                            .padding(0d, 0d, 10d, 0d);

                    pie.legend()
                            .position("center.bottom")
                            .itemsLayout(LegendLayout.HORIZONTAL)
                            .align(Align.CENTER);

                    anyChartView.setChart(pie);


                } else {
                    Toast.makeText(MonthlyAnalyticsActivity.this, "Child does not exist", Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private  void setStatusAndImageResource() {
        personalRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    float traTotal;
                    if (snapshot.hasChild("monthTransport")) {
                        traTotal = Integer.parseInt(snapshot.child("monthTransport").getValue().toString());
                    } else {
                        traTotal = 0;

                    }

                    float houseTotal;
                    if (snapshot.hasChild("monthHouse")) {
                        houseTotal = Integer.parseInt(snapshot.child("monthHouse").getValue().toString());
                    } else {
                        houseTotal = 0;

                    }

                    float foodTotal;
                    if (snapshot.hasChild("monthFood")) {
                        foodTotal = Integer.parseInt(snapshot.child("monthFood").getValue().toString());
                    } else {
                        foodTotal = 0;

                    }

                    float entTotal;
                    if (snapshot.hasChild("monthEntertainment")) {
                        entTotal = Integer.parseInt(snapshot.child("monthEntertainment").getValue().toString());
                    } else {
                        entTotal = 0;

                    }

                    float eduTotal;
                    if (snapshot.hasChild("monthEducation")) {
                        eduTotal = Integer.parseInt(snapshot.child("monthEducation").getValue().toString());
                    } else {
                        eduTotal = 0;

                    }

                    float chaTotal;
                    if (snapshot.hasChild("monthCharity")) {
                        chaTotal = Integer.parseInt(snapshot.child("monthCharity").getValue().toString());
                    } else {
                        chaTotal = 0;

                    }

                    float appTotal;
                    if (snapshot.hasChild("monthApparel")) {
                        appTotal = Integer.parseInt(snapshot.child("monthApparel").getValue().toString());
                    } else {
                        appTotal = 0;

                    }

                    float helTotal;
                    if (snapshot.hasChild("monthHealth")) {
                        helTotal = Integer.parseInt(snapshot.child("monthHealth").getValue().toString());
                    } else {
                        helTotal = 0;

                    }

                    float otherTotal;
                    if (snapshot.hasChild("monthOther")) {
                        otherTotal = Integer.parseInt(snapshot.child("monthOther").getValue().toString());
                    } else {
                        otherTotal = 0;

                    }

                    float perTotal;
                    if (snapshot.hasChild("monthPersonal")) {
                        perTotal = Integer.parseInt(snapshot.child("monthPersonal").getValue().toString());
                    } else {
                        perTotal = 0;

                    }
                    float monthTotalSpentAmount;
                    if (snapshot.hasChild("month")) {
                        monthTotalSpentAmount = Integer.parseInt(snapshot.child("month").getValue().toString());
                    } else {
                        monthTotalSpentAmount = 0;

                    }


                    // Geting Ratios

                    float transportRatio;
                    if (snapshot.hasChild("monthTransRatio")) {
                        transportRatio = Integer.parseInt(snapshot.child("monthTransRatio").getValue().toString());
                    } else {
                        transportRatio = 0;

                    }

                    float houseRatio;
                    if (snapshot.hasChild("monthHouseRatio")) {
                        houseRatio = Integer.parseInt(snapshot.child("monthHouseRatio").getValue().toString());
                    } else {
                        houseRatio = 0;

                    }
                    float foodRatio;
                    if (snapshot.hasChild("monthFoodRatio")) {
                        foodRatio = Integer.parseInt(snapshot.child("monthFoodRatio").getValue().toString());
                    } else {
                        foodRatio = 0;

                    }

                    float entRatio;
                    if (snapshot.hasChild("monthEntRatio")) {
                        entRatio = Integer.parseInt(snapshot.child("monthEntRatio").getValue().toString());
                    } else {
                        entRatio = 0;

                    }

                    float eduRatio;
                    if (snapshot.hasChild("monthEduRatio")) {
                        eduRatio = Integer.parseInt(snapshot.child("monthEduRatio").getValue().toString());
                    } else {
                        eduRatio = 0;

                    }

                    float charRatio;
                    if (snapshot.hasChild("monthCharityRatio")) {
                        charRatio = Integer.parseInt(snapshot.child("monthCharityRatio").getValue().toString());
                    } else {
                        charRatio = 0;

                    }

                    float appRatio;
                    if (snapshot.hasChild("monthApparelRatio")) {
                        appRatio = Integer.parseInt(snapshot.child("monthApparelRatio").getValue().toString());
                    } else {
                        appRatio = 0;

                    }

                    float helRatio;
                    if (snapshot.hasChild("monthHealthRatio")) {
                        helRatio = Integer.parseInt(snapshot.child("monthHealthRatio").getValue().toString());
                    } else {
                        helRatio = 0;

                    }

                    float otherRatio;
                    if (snapshot.hasChild("monthOtherRatio")) {
                        otherRatio = Integer.parseInt(snapshot.child("monthOtherRatio").getValue().toString());
                    } else {
                        otherRatio = 0;

                    }

                    float perRatio;
                    if (snapshot.hasChild("monthPersonalRatio")) {
                        perRatio = Integer.parseInt(snapshot.child("monthPersonalRatio").getValue().toString());
                    } else {
                        perRatio = 0;

                    }

                    float monthTotalSpentAmountRatio;
                    if (snapshot.hasChild("budget")) {
                        monthTotalSpentAmountRatio = Integer.parseInt(snapshot.child("budget").getValue().toString());
                    } else {
                        monthTotalSpentAmountRatio = 0;

                    }


                    float monthPercent = (monthTotalSpentAmount / monthTotalSpentAmountRatio) * 100;
                    if (monthPercent < 50) {
                        monthRatioSpending.setText(monthPercent + "%" + " used of " + monthTotalSpentAmountRatio + ". status");
                        monthRatioSpending_image.setImageResource(R.drawable.green);

                    } else if (monthPercent >= 50 && monthPercent < 100) {
                        monthRatioSpending.setText(monthPercent + "%" + " used of " + monthTotalSpentAmountRatio + ". status");
                        monthRatioSpending_image.setImageResource(R.drawable.brown);
                    } else {
                        monthRatioSpending.setText(monthPercent + "%" + " used of " + monthTotalSpentAmountRatio + ". status");
                        monthRatioSpending_image.setImageResource(R.drawable.red);
                    }

                    float transportPercent = (traTotal / transportRatio) * 100;
                    if (transportPercent < 50) {
                        progress_ratio_transport.setText(transportPercent + "%" + " used of " + transportRatio + ". status");
                        transport_status.setImageResource(R.drawable.green);

                    } else if (transportPercent >= 50 && transportPercent < 100) {
                        progress_ratio_transport.setText(transportPercent + "%" + " used of " + transportRatio + ". status");
                        transport_status.setImageResource(R.drawable.brown);
                    } else {
                        progress_ratio_transport.setText(transportPercent + "%" + " used of " + transportRatio + ". status");
                        transport_status.setImageResource(R.drawable.red);
                    }


                    float housePercent = (houseTotal / houseRatio) * 100;
                    if (housePercent < 50) {
                        progress_ratio_house.setText(housePercent + "%" + " used of " + houseRatio + ". status");
                        house_status.setImageResource(R.drawable.green);

                    } else if (housePercent >= 50 && housePercent < 100) {
                        progress_ratio_house.setText(housePercent + "%" + " used of " + houseRatio + ". status");
                        house_status.setImageResource(R.drawable.brown);
                    } else {
                        progress_ratio_house.setText(housePercent + "%" + " used of " + houseRatio + ". status");
                        house_status.setImageResource(R.drawable.red);
                    }

                    float foodPercent = (foodTotal / foodRatio) * 100;
                    if (foodPercent < 50) {
                        progress_ratio_food.setText(foodPercent + "%" + " used of " + foodRatio + ". status");
                        food_status.setImageResource(R.drawable.green);

                    } else if (foodPercent >= 50 && foodPercent < 100) {
                        progress_ratio_food.setText(foodPercent + "%" + " used of " + foodRatio + ". status");
                        food_status.setImageResource(R.drawable.brown);
                    } else {
                        progress_ratio_food.setText(foodPercent + "%" + " used of " + foodRatio + ". status");
                        food_status.setImageResource(R.drawable.red);
                    }

                    float entPercent = (entTotal / entRatio) * 100;
                    if (entPercent < 50) {
                        progress_ratio_entertainment.setText(entPercent + "%" + " used of " + entRatio + ". status");
                        entertainment_status.setImageResource(R.drawable.green);

                    } else if (entPercent >= 50 && entPercent < 100) {
                        progress_ratio_entertainment.setText(entPercent + "%" + " used of " + entRatio + ". status");
                        entertainment_status.setImageResource(R.drawable.brown);
                    } else {
                        progress_ratio_entertainment.setText(entPercent + "%" + " used of " + entRatio + ". status");
                        entertainment_status.setImageResource(R.drawable.red);
                    }

                    float eduPercent = (eduTotal / eduRatio) * 100;
                    if (eduPercent < 50) {
                        progress_ratio_education.setText(eduPercent + "%" + " used of " + eduRatio + ". status");
                        education_status.setImageResource(R.drawable.green);

                    } else if (eduPercent >= 50 && eduPercent < 100) {
                        progress_ratio_education.setText(eduPercent + "%" + " used of " + eduRatio + ". status");
                        education_status.setImageResource(R.drawable.brown);
                    } else {
                        progress_ratio_education.setText(eduPercent + "%" + " used of " + eduRatio + ". status");
                        education_status.setImageResource(R.drawable.red);
                    }

                    float chaPercent = (chaTotal / charRatio) * 100;
                    if (chaPercent < 50) {
                        progress_ratio_charity.setText(chaPercent + "%" + " used of " + charRatio + ". status");
                        charity_status.setImageResource(R.drawable.green);

                    } else if (chaPercent >= 50 && chaPercent < 100) {
                        progress_ratio_charity.setText(chaPercent + "%" + " used of " + charRatio + ". status");
                        charity_status.setImageResource(R.drawable.brown);
                    } else {
                        progress_ratio_charity.setText(chaPercent + "%" + " used of " + charRatio + ". status");
                        charity_status.setImageResource(R.drawable.red);
                    }

                    float apparelPercent = (appTotal / appRatio) * 100;
                    if (apparelPercent < 50) {
                        progress_ratio_apparel.setText(apparelPercent + "%" + " used of " + appRatio + ". status");
                        apparel_status.setImageResource(R.drawable.green);

                    } else if (apparelPercent >= 50 && apparelPercent < 100) {
                        progress_ratio_apparel.setText(apparelPercent + "%" + " used of " + appRatio + ". status");
                        apparel_status.setImageResource(R.drawable.brown);
                    } else {
                        progress_ratio_apparel.setText(apparelPercent + "%" + " used of " + appRatio + ". status");
                        apparel_status.setImageResource(R.drawable.red);
                    }

                    float healthPercent = (helTotal / helRatio) * 100;
                    if (healthPercent < 50) {
                        progress_ratio_health.setText(healthPercent + "%" + " used of " + helRatio + ". status");
                        health_status.setImageResource(R.drawable.green);

                    } else if (healthPercent >= 50 && healthPercent < 100) {
                        progress_ratio_health.setText(healthPercent + "%" + " used of " + helRatio + ". status");
                        health_status.setImageResource(R.drawable.brown);
                    } else {
                        progress_ratio_health.setText(healthPercent + "%" + " used of " + helRatio + ". status");
                        health_status.setImageResource(R.drawable.red);
                    }

                    float otherPercent = (otherTotal / otherRatio) * 100;
                    if (otherPercent < 50) {
                        progress_ratio_other.setText(otherPercent + "%" + " used of " + otherRatio + ". status");
                        other_status.setImageResource(R.drawable.green);

                    } else if (otherPercent >= 50 && otherPercent < 100) {
                        progress_ratio_other.setText(otherPercent + "%" + " used of " + otherRatio + ". status");
                        other_status.setImageResource(R.drawable.brown);
                    } else {
                        progress_ratio_other.setText(otherPercent + "%" + " used of " + otherRatio + ". status");
                        other_status.setImageResource(R.drawable.red);
                    }

                    float personalPercent = (perTotal / perRatio) * 100;
                    if (personalPercent < 50) {
                        progress_ratio_personal.setText(personalPercent + "%" + " used of " + perRatio + ". status");
                        personal_status.setImageResource(R.drawable.green);

                    } else if (personalPercent >= 50 && personalPercent < 100) {
                        progress_ratio_personal.setText(personalPercent + "%" + " used of " + perRatio + ". status");
                        personal_status.setImageResource(R.drawable.brown);
                    } else {
                        progress_ratio_personal.setText(personalPercent + "%" + " used of " + perRatio + ". status");
                        personal_status.setImageResource(R.drawable.red);
                    }


                } else {
                    Toast.makeText(MonthlyAnalyticsActivity.this, "setStatusAndImageResource Errors", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}