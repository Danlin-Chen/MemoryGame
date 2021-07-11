package iss.workshop.memorygame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button mGamePlayActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Linear Layout Tutorial
        mGamePlayActivity = findViewById(R.id.btnGamePlayActivity);
        Intent mGamePlayActivityIntent = new Intent(this, GameActivity.class);
        mGamePlayActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(mGamePlayActivityIntent);
            }
        });

    }
}