package motloung.koena.kovsiesbanking.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import motloung.koena.kovsiesbanking.R;
import motloung.koena.kovsiesbanking.adapters.UserData;

public class SuccessActivity extends AppCompatActivity {

    Button btnSuccessGoHome;
    UserData data;
    TextView txtBeneficiary, txtAmount, txtDescription, txtDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);

        txtBeneficiary = findViewById(R.id.txt_beneficiary);
        txtAmount = findViewById(R.id.txt_payment_amount);
        txtDescription = findViewById(R.id.txt_reference);
        txtDate = findViewById(R.id.txt_date_time);

        Intent intent = getIntent();
        data = (UserData) intent.getSerializableExtra("data");

        retrieveTransactionDetails();

        btnSuccessGoHome = findViewById(R.id.btn_return_homee);
        btnSuccessGoHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SuccessActivity.this, HomeActivity.class);
                intent.putExtra("data",data);
                startActivity(intent);
                finish();
            }
        });
    }

    private void retrieveTransactionDetails() {
        data = (UserData) getIntent().getSerializableExtra("data");

        String receiverAccount = getIntent().getStringExtra("receiverAccount");
        String reference = getIntent().getStringExtra("reference");
        double amount = getIntent().getDoubleExtra("amount", 0.0);
        DateTimeFormatter formatter = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            formatter = DateTimeFormatter.ofPattern("MMM dd, HH:mm");
        }
        String formattedDateTime = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            formattedDateTime = LocalDateTime.now().format(formatter);
        }

        txtDate.setText("Date: " + formattedDateTime);

        txtBeneficiary.setText("Receiver Account: " + receiverAccount);
        txtDescription.setText("Reference: " + reference);
        txtAmount.setText("Amount Sent: " + String.format(Locale.getDefault(), "%.2f", amount));
        txtDate.setText("Date: " + formattedDateTime);
    }
}