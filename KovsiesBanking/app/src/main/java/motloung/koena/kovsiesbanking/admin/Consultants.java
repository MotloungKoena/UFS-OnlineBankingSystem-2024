package motloung.koena.kovsiesbanking.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import motloung.koena.kovsiesbanking.R;
import motloung.koena.kovsiesbanking.user.RegisterActivity;

public class Consultants extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultant);

        Button buttonChangePassword = findViewById(R.id.buttonChangePassword);
        Button buttonAddAccount = findViewById(R.id.buttonAddAccount);
        Button buttonUpdateAccount = findViewById(R.id.buttonUpdateAccount);
        Button buttonViewUsers = findViewById(R.id.buttonViewUsers);
        Button buttonLoginSessions = findViewById(R.id.buttonLogins);
        TextView title = findViewById(R.id.title);

        Intent intent = getIntent();
        String role = intent.getStringExtra("role");
        title.setText("Hello " + role.toUpperCase());

        buttonLoginSessions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Consultants.this, LoginSessions.class);
                startActivity(intent);
            }
        });

        buttonChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Consultants.this, ChangePassword.class);
                startActivity(intent);
            }
        });

        buttonAddAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Consultants.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        buttonUpdateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Consultants.this, UpdateAccount.class);
                startActivity(intent);
            }
        });

        buttonViewUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Consultants.this, ViewAllUsers.class);
                startActivity(intent);
            }
        });
    }
}
