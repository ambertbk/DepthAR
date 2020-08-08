package com.example.AmbersAR;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.net.UnknownHostException;

public class TextActivity extends AppCompatActivity {
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);

        editText = findViewById(R.id.editText);

        setTypingAnimation();
    }

    public void onClick(View v) throws UnknownHostException {
        if (editText.getText().length() > 0) {
            String message = "0000" + editText.getText().toString();
            Client textClient = new Client();
            textClient.execute(message);

            informUser("text sent!");

            editText.setText("");
        } else {
            informUser("Please add text!");
        }
    }

    private void informUser(String text){
        Toast message = Toast.makeText(TextActivity.this, text, Toast.LENGTH_SHORT);
        message.setGravity(Gravity.CENTER, 0, 0);
        message.show();

    }

    private void setTypingAnimation(){
        TypingAnimation typingAnimation = findViewById(R.id.typingAnimation);
        typingAnimation.setText("");
        typingAnimation.setCharacterDelayed(200);
        typingAnimation.animateText("Typing....");
    }
}