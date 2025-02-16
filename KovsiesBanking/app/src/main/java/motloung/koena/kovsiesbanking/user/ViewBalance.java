package motloung.koena.kovsiesbanking.user;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import motloung.koena.kovsiesbanking.R;
import motloung.koena.kovsiesbanking.adapters.CreateConnection;
import motloung.koena.kovsiesbanking.adapters.UserData;
import motloung.koena.kovsiesbanking.helpers.Helpers;

public class ViewBalance extends AppCompatActivity
{
    private TextView txtAccountNumber, txtBalance;
    private Button btnGoBack;
    public CreateConnection createConnection;
    private String userEmail;
    UserData data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_balance);
        createConnection = new CreateConnection(this);

        txtAccountNumber = findViewById(R.id.tv_account_number);
        txtBalance = findViewById(R.id.tv_balance);
        btnGoBack = findViewById(R.id.btn_go_back);

        Intent intent = getIntent();
        data = (UserData) intent.getSerializableExtra("data");

        txtAccountNumber.setText("Account number : " + data.getAccountNumber());
        userEmail = data.getNormalizedEmail().toLowerCase();

        loadAccountDetails();

        btnGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Close current activity
            }
        });
    }

    private void loadAccountDetails2() {
        try {
            String query = "SELECT * FROM BankAccount WHERE BankAccountNumber = '" + data.getAccountNumber() + "'";
            ResultSet resultSet = createConnection.getData(query);
            while (resultSet != null && resultSet.next())
            {
                double balance = resultSet.getDouble("BankAccountBalance");
                txtBalance.setText("Available balance: " + balance);
                break;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error loading account details", Toast.LENGTH_SHORT).show();
        }
    }
    private void loadAccountDetails()
    {
        Connection connection = createConnection.getConnection();
        if (connection != null)
        {
            try {
                String query = "SELECT * FROM BankAccount WHERE BankAccountNumber = '" + data.getAccountNumber() + "'";
                PreparedStatement statement = connection.prepareStatement(query);
                ResultSet resultSet = statement.executeQuery();

                while (resultSet != null && resultSet.next())
                {
                    double balance = resultSet.getDouble("BankAccountBalance");
                    txtBalance.setText("Available balance: " + balance);
                    break;
                }
            } catch (SQLException e){
                e.printStackTrace();
            }
        }
        else {
            Log.e("DB Connection", "Connection is null");
        }
    }
}
