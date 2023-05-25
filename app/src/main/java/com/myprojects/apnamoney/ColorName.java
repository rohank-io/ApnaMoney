package com.myprojects.apnamoney;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

public class ColorName extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_name);


        Button button = findViewById(R.id.button1);
        CheckBox blue = findViewById(R.id.blue);
        CheckBox red = findViewById(R.id.red);
        CheckBox yellow = findViewById(R.id.yellow);
        TextView result = findViewById(R.id.result);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (red.isChecked() && yellow.isChecked() && blue.isChecked())
                    result.setText("Red and yellow and blue is checked");

                else if (red.isChecked() && yellow.isChecked())
                    result.setText("Red and Yellow is checked");

                else if (red.isChecked() && blue.isChecked())
                    result.setText("Red and Blue is checked");

                else if (yellow.isChecked() && blue.isChecked())
                    result.setText("Yellow and Blue is checked");

                else if (yellow.isChecked())
                    result.setText("Yellow is checked");

                else if (red.isChecked())
                    result.setText("Red is checked");

                else if (blue.isChecked())
                    result.setText("Blue is checked");
                else
                    result.setText("Please select color");


            }
        });

    }
}
