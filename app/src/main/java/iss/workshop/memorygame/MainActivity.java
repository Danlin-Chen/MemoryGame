package iss.workshop.memorygame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private EditText mPlayerName;
    private Button mSubmitBtn;
    String player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPlayerName = findViewById(R.id.playerName);
        mSubmitBtn = findViewById(R.id.submitBtn);

        mSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences pref = getSharedPreferences("players_information", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();

                player = mPlayerName.getText().toString();
                editor.putString("player", player);
                editor.commit();

                Intent intent = new Intent(MainActivity.this, EndActivity.class);
                startActivity(intent);
            }
        });
    }
}