package motloung.koena.kovsiesbanking.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import motloung.koena.kovsiesbanking.R;
import motloung.koena.kovsiesbanking.adapters.UserData;


public class CardActivity extends AppCompatActivity
{
    TextView txtNameSurname, txtAccountNumber;
    Button btnBack;
    UserData data;
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_card);

        txtAccountNumber = findViewById(R.id.txtAccountNumber_card);
        txtNameSurname = findViewById(R.id.txtNameSurname_card);
        btnBack= findViewById(R.id.btn_back_card);

        Intent intent = getIntent();
        data = (UserData) intent.getSerializableExtra("data");

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        txtAccountNumber.setText(data.getAccountNumber());
        txtNameSurname.setText(data.getLastName() +" "+ data.getFirstName());

    }
}
