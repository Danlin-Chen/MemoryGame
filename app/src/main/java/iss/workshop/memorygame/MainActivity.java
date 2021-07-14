package iss.workshop.memorygame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

        MediaPlayer mediaplayer = MediaPlayer.create(MainActivity.this, R.raw.startbtn_sound);

        // Additional validation which only allows letters, digits, _ and - is set at the activity_main.xml
        mSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isEmpty(mPlayerName)) {

                    SharedPreferences pref = getSharedPreferences("players_information", MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();

                    player = mPlayerName.getText().toString();
                    editor.putString("player", player);
                    editor.commit();

                    mediaplayer.start();
                    startImageActivity();
                }else{
                    Toast.makeText(getApplicationContext(), "Please enter your name", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void startImageActivity(){
        Intent intent = new Intent(this, ImageActivity.class);
        startActivity(intent);
    }

    private boolean isEmpty(EditText editText) {
        if (editText.getText().toString().trim().length() > 0){
            return false;
        }
        else{
            return true;
        }
    }
}