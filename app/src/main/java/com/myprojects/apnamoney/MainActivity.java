package com.myprojects.apnamoney;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.joda.time.DateTime;
import org.joda.time.Months;
import org.joda.time.MutableDateTime;
import org.joda.time.Weeks;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;
import java.util.Objects;
import java.util.Random;


public class MainActivity extends AppCompatActivity {
    private ImageView imgbudget, imgSpending, imgAnalytics, imgHistory, btmnav;
    private TextView budgetTv, todayTv, weekTv, monthTv, savingtv, textViewHistory, tv_tip;
    private static final int REQUEST_CALL = 1;

    FloatingActionButton add, spendingIcon, budgetIcon;

    boolean aBoolean = true;

    private Toolbar toolbar;
    private TextView userEmail;

    private FirebaseAuth mAuth;
    private DatabaseReference budgetRef, expensesRef, personalRef;
    private String onlineUserId = "";

    private int totalAmountMonth = 0;
    private int totalAmountBudget = 0;
    private int totalAmountBudgetB = 0;
    private int totalAmountBudgetC = 0;

    private String items[] = {"It isn't what you earn but how spent it that fixes your class...",
            "A penny saved is worth two pennies earned...after taxes", "A budget doesn't limit your freedom... it gives you freedom.",
            "Too many people spend money they haven't earned, to buy things they don't want, to impress people they don't like... ",
            "Compound interest is the eight wonder of the world. He who understands it, earns it, he who doesn't pays it..."};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btmnav = (ImageView) findViewById(R.id.bottom_nav);
        btmnav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheetDialog();
            }
        });


        imgbudget = findViewById(R.id.imgbudget);
        imgSpending = findViewById(R.id.imgSpending);
        imgAnalytics = findViewById(R.id.imgAnalytics);
        imgHistory = findViewById(R.id.imgHistory);
        textViewHistory = findViewById(R.id.textViewHistory);

        budgetTv = findViewById(R.id.budgetTv);
        todayTv = findViewById(R.id.todayTv);
        weekTv = findViewById(R.id.weekTv);
        monthTv = findViewById(R.id.monthTv);
        savingtv = findViewById(R.id.savingTv);

        userEmail = findViewById(R.id.userEmail);
        userEmail.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());

        tv_tip = findViewById(R.id.tv_tip);

        add = findViewById(R.id.add);
        spendingIcon = findViewById(R.id.spendingIcon);
        budgetIcon = findViewById(R.id.budgetIcon);


        mAuth = FirebaseAuth.getInstance();
        onlineUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        budgetRef = FirebaseDatabase.getInstance().getReference("budget").child(onlineUserId);

        expensesRef = FirebaseDatabase.getInstance().getReference("expenses").child(onlineUserId);
        personalRef = FirebaseDatabase.getInstance().getReference("personal").child(onlineUserId);

        Random rndm = new Random();
        int i = rndm.nextInt(items.length);
        tv_tip.setText(items[i]);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (aBoolean) {
                    budgetIcon.show();
                    spendingIcon.show();
                    aBoolean = false;
                } else {
                    budgetIcon.hide();
                    spendingIcon.hide();
                    aBoolean = true;
                }
            }
        });

        budgetIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BudgetActivity.class);
                startActivity(intent);

            }
        });

        spendingIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ChooseSpendingActivity.class);
                startActivity(intent);

            }
        });


        imgbudget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AccountActivity.class);
                startActivity(intent);

            }
        });

        imgHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
                startActivity(intent);
            }
        });

        textViewHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HistoryActivity.class);

                startActivity(intent);
            }
        });

        imgSpending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, smsActivity.class);
                startActivity(intent);

            }
        });

        imgAnalytics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MonthlyAnalyticsActivity.class);
                startActivity(intent);

            }
        });

        budgetRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists() && snapshot.getChildrenCount() > 0) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object total = map.get("amount");
                        int pTotal = Integer.parseInt(String.valueOf(total));
                        totalAmountBudgetB += pTotal;
                    }
                    totalAmountBudgetC = totalAmountBudgetB;
                    personalRef.child("budget").setValue(totalAmountBudgetC);
                } else {
                    personalRef.child("budget").setValue(0);
                    Toast.makeText(MainActivity.this, "Please Set a Budget", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        getBudgetAmount();
        getTodayspentAmount();
        getWeekSpentAmount();
        getMonthSpentAmount();
        getSavings();


    }

    private void showBottomSheetDialog() {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog_layout);

        LinearLayout share = bottomSheetDialog.findViewById(R.id.share);
        LinearLayout contactus = bottomSheetDialog.findViewById(R.id.contactus);
        LinearLayout visitus = bottomSheetDialog.findViewById(R.id.visitus);

        //not implimented yet
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(MainActivity.this, "Share is Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        contactus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                callButton();


            }
        });
        visitus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goLink("https://www.linkedin.com/school/sinhgad-institute-of-management");

            }
        });
        bottomSheetDialog.show();
    }

    private void goLink(String s) {
        Uri uri = Uri.parse(s);
        startActivity(new Intent(Intent.ACTION_VIEW,uri));
    }

    private void callButton() {

        String number = "8007748618";
        if (number.trim().length() > 0) {
            if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);

            } else {
                String dial = "tel:" + number;
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CALL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callButton();
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getMonthSpentAmount() {
        MutableDateTime epoch = new MutableDateTime();
        epoch.setDate(0);//Set to Epoch Time
        DateTime now = new DateTime();
        Months months = Months.monthsBetween(epoch, now);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("expenses").child(onlineUserId);
        Query query = reference.orderByChild("month").equalTo(months.getMonths());

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int totalAmount = 0;
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Map<String, Object> map = (Map<String, Object>) ds.getValue();
                    Object total = map.get("amount");
                    int pTotal = Integer.parseInt(String.valueOf(total));
                    totalAmount += pTotal;
                    monthTv.setText("₹" + totalAmount);
                }
                personalRef.child("month").setValue(totalAmount);
                totalAmountMonth = totalAmount;

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void getTodayspentAmount() {

        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Calendar cal = Calendar.getInstance();
        String date = dateFormat.format(cal.getTime());
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("expenses").child(onlineUserId);
        Query query = reference.orderByChild("date").equalTo(date);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int totalAmount = 0;
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Map<String, Object> map = (Map<String, Object>) ds.getValue();
                    Object total = map.get("amount");
                    int pTotal = Integer.parseInt(String.valueOf(total));
                    totalAmount += pTotal;
                    todayTv.setText("₹" + totalAmount);
                }
                personalRef.child("today").setValue(totalAmount);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();


            }
        });
    }

    private void getBudgetAmount() {

        budgetRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists() && snapshot.getChildrenCount() > 0) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object total = map.get("amount");
                        int pTotal = Integer.parseInt(String.valueOf(total));
                        totalAmountBudget += pTotal;
                        budgetTv.setText("₹" + String.valueOf(totalAmountBudget));
                    }

                } else {
                    totalAmountBudget = 0;
                    budgetTv.setText("₹" + String.valueOf(0));

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getSavings() {
        personalRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    int budget;
                    if (snapshot.hasChild("budget")) {
                        budget = Integer.parseInt(snapshot.child("budget").getValue().toString());
                    } else {
                        budget = 0;

                    }
                    int monthSpending;
                    if (snapshot.hasChild("month")) {
                        monthSpending = Integer.parseInt(Objects.requireNonNull(snapshot.child("month").getValue().toString()));
                    } else {
                        monthSpending = 0;

                    }
                    int savings = budget - monthSpending;
                    savingtv.setText("₹" + savings);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void getWeekSpentAmount() {
        MutableDateTime epoch = new MutableDateTime();
        epoch.setDate(0);//Set to Epoch Time

        DateTime now = new DateTime();
        Weeks weeks = Weeks.weeksBetween(epoch, now);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("expenses").child(onlineUserId);
        Query query = reference.orderByChild("week").equalTo(weeks.getWeeks());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int totalAmount = 0;
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Map<String, Object> map = (Map<String, Object>) ds.getValue();
                    Object total = map.get("amount");
                    int pTotal = Integer.parseInt(String.valueOf(total));
                    totalAmount += pTotal;
                    weekTv.setText("₹" + totalAmount);
                }
                personalRef.child("week").setValue(totalAmount);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menu,menu);
//        return true;
//
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        if (item.getItemId()==R.id.account){
//            Intent intent = new Intent(MainActivity.this,AccountActivity.class);
//            startActivity(intent);
//        }
//        return super.onOptionsItemSelected(item);
//    }
}