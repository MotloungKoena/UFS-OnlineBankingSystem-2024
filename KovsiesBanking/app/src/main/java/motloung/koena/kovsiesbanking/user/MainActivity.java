package motloung.koena.kovsiesbanking.user;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import motloung.koena.kovsiesbanking.R;
import motloung.koena.kovsiesbanking.adapters.UserData;

public class MainActivity extends AppCompatActivity {
TextView txtWelcomeUser;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtWelcomeUser = findViewById(R.id.welcomeuserr);
        Intent intent = getIntent();
        UserData data = (UserData) intent.getSerializableExtra("data");

        if (data != null) {
            txtWelcomeUser.setText("Hello" + data.getUserEmail());
        } else {
            txtWelcomeUser.setText("No user data available");
        }
    }
}