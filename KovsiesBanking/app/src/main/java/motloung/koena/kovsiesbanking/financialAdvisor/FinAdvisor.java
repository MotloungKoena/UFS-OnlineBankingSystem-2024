package motloung.koena.kovsiesbanking.financialAdvisor;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import motloung.koena.kovsiesbanking.R;
import motloung.koena.kovsiesbanking.admin.ViewAllUsers;

public class FinAdvisor extends AppCompatActivity {

    private Button btnViewAllClients;
    private Button btnAdvice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finadvisor);

        btnViewAllClients = findViewById(R.id.btnViewAllClients);

        btnViewAllClients.setOnClickListener(v -> handleViewClients());
        btnAdvice.setOnClickListener(v -> handleAdvice());
    }

    private void handleViewClients()
    {
        Intent viewall = new Intent(FinAdvisor.this, ViewAllUsers.class);
        viewall.putExtra("role", "advisor");
        startActivity(viewall);
    }

    private void handleAdvice() {
        Intent ga = new Intent(FinAdvisor.this, Advice.class);
        startActivity(ga);
    }
}