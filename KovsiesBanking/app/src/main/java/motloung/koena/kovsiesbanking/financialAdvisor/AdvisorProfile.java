package motloung.koena.kovsiesbanking.financialAdvisor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

import motloung.koena.kovsiesbanking.R;
import motloung.koena.kovsiesbanking.adapters.UserData;

public class AdvisorProfile extends AppCompatActivity
{
    TextView txtName,txtSurname, txtUserName, txtEmail;
    Button btnGoBack;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advisor_profile);

        txtName = findViewById(R.id.txtFirstNameUpdate);
        txtSurname = findViewById(R.id.txtLastNameUpdate);
        txtUserName = findViewById(R.id.txtUsernameUpdate);
        txtEmail = findViewById(R.id.txtEmailUpdate);
        btnGoBack = findViewById(R.id.btnGoBackAdv);

        Intent intent = getIntent();
        UserData data = (UserData) intent.getSerializableExtra("data");

        txtName.setText(data.getFirstName());
        txtSurname.setText(data.getLastName());
        txtUserName.setText(data.getUserName());
        txtEmail.setText(data.getNormalizedEmail().toLowerCase(Locale.ROOT));
        btnGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
