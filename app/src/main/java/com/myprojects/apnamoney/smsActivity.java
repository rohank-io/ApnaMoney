package com.myprojects.apnamoney;




import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

public class smsActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_READ_SMS = 1;
    private ArrayList<String> transactionMessagesList;
    private ArrayAdapter<String> transactionMessagesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);

        ListView listView = findViewById(R.id.list_view);

        transactionMessagesList = new ArrayList<>();
        transactionMessagesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, transactionMessagesList);
        listView.setAdapter(transactionMessagesAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Handle item click
            }
        });

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_SMS},
                    PERMISSION_REQUEST_READ_SMS);
        } else {
            readSmsMessages();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_READ_SMS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    readSmsMessages();
                }
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void readSmsMessages() {
        Uri uri = Uri.parse("content://sms");
        String[] projection = new String[]{"_id", "address", "body", "date", "type"};
        String selection = "body LIKE '%debit%' OR body LIKE '%credit%'";
        String[] selectionArgs = null;
        String sortOrder = "date DESC";

        ContentResolver contentResolver = getContentResolver();

        try (Cursor cursor = contentResolver.query(uri, projection, selection, selectionArgs, sortOrder)) {
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    String address = cursor.getString(cursor.getColumnIndex("address"));
                    String body = cursor.getString(cursor.getColumnIndex("body"));
                    transactionMessagesList.add(address + ": " + body + "\n___________________________________________" );
                }
            }
            transactionMessagesAdapter.notifyDataSetChanged();
        }
    }
}