package motloung.koena.kovsiesbanking.financialAdvisor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import motloung.koena.kovsiesbanking.R;
import motloung.koena.kovsiesbanking.adapters.CreateConnection;
import motloung.koena.kovsiesbanking.adapters.UserData;

public class Advice extends AppCompatActivity {

    private EditText advTxtBankAcc;
    private Button advSearchAcc;
    private EditText txtAdvice;
    private Button btnGiveAdvice;
    private Connection connection;
    private CreateConnection connectHelper;
    private String userEmail;
    private String AccountNumber;
    UserData data;
    CardView cardAdvice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advice);
        connectHelper = new CreateConnection(this);
        connection = connectHelper.getConnection();
        AccountNumber = userEmail = "";

        advTxtBankAcc = findViewById(R.id.advTxtBankAcc);
        advSearchAcc = findViewById(R.id.btnSearchAccount);
        cardAdvice = findViewById(R.id.cardAdvice);
        advSearchAcc.setOnClickListener(v -> handleSearchBankAccount());

        Intent me = getIntent();
        AccountNumber = me.getStringExtra("accountnumber");

        data  = (UserData) me.getSerializableExtra("data");

        if(!AccountNumber.equals(""))
        {
            advTxtBankAcc.setText(AccountNumber);
        }
        txtAdvice = findViewById(R.id.txtAdvice);
        btnGiveAdvice = findViewById(R.id.btnSubmitAdvice);
        btnGiveAdvice.setOnClickListener(v -> handleGiveAdvice());
    }

    private void handleSearchBankAccount()
    {
        boolean dataFound = false;
        if(connection != null){
            try {
                ResultSet set = connectHelper.getData("SELECT Email FROM AspNetUsers WHERE AccountNo = '" + advTxtBankAcc.getText().toString() + "';");
                while (set.next()) {
                    dataFound = true;
                    userEmail = set.getString(1);
                    break;
                }
            } catch (SQLException e)
            {
                e.printStackTrace();
                finish();
            }

        }
        if(dataFound) {
            btnGiveAdvice.setVisibility(View.VISIBLE);
            txtAdvice.setVisibility(View.VISIBLE);
            cardAdvice.setVisibility(View.VISIBLE);
        }
    }

    private void handleGiveAdvice()
    {
        if (validateInputs()) {
            boolean success = connectHelper.insertData("INSERT INTO Notification (NotificationMessage, NotificationCreated, IsRead, Email) VALUES ('" + "[ADVICE] " + txtAdvice.getText().toString() + "', GETDATE(), 0 ,'" + userEmail + "');");
            if(success){
                finish();
                Toast.makeText(this, "Advice given successfully", Toast.LENGTH_SHORT).show();
            }
            else{ finish();
                Toast.makeText(this, "Error in giving advice", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean validateInputs() {
        if (advTxtBankAcc.getText().toString().isEmpty()) {
            advTxtBankAcc.setError("Bank account is required");
            return false;
        }
        if (txtAdvice.getText().toString().isEmpty()) {
            txtAdvice.setError("Advice is required");
            return false;
        }
        return true;
    }
}