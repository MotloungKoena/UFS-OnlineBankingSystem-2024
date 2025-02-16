package motloung.koena.kovsiesbanking.user;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import motloung.koena.kovsiesbanking.R;
import motloung.koena.kovsiesbanking.adapters.CreateConnection;
import motloung.koena.kovsiesbanking.adapters.UserData;

public class DepositMoneyActivity extends AppCompatActivity {

    private EditText depositAmountField,availableBalanceField, accountNumberField;
    private Button depositButton, btnGoBack;
    private CreateConnection createConnection;
    private String userEmail;
    UserData data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposit_money);

        depositAmountField = findViewById(R.id.Amount_deposit);
        availableBalanceField = findViewById(R.id.AvailableBalance_deposit);
        accountNumberField = findViewById(R.id.AccountNumber_deposit);
        depositButton = findViewById(R.id.btn_deposit);
        btnGoBack = findViewById(R.id.btn_go_back_deposit);

        Intent intent = getIntent();
        data = (UserData) intent.getSerializableExtra("data");

        userEmail = data.getNormalizedEmail().toLowerCase();
        createConnection = new CreateConnection(this);
        loadAccountDetails();

        depositButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                depositMoney();
            }
        });

        btnGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(DepositMoneyActivity.this, HomeActivity.class);
                intent1.putExtra("data",data);
                startActivity(intent1);
            }
        });
    }

    private void loadAccountDetails2() {
        String query = "SELECT * FROM BankAccount WHERE BankAccountNumber = '" + data.getAccountNumber() + "'";
        ResultSet resultSet = createConnection.getData(query);
        try {
            if (resultSet != null && resultSet.next()) {
                double balance = resultSet.getDouble("BankAccountBalance");
                availableBalanceField.setText(String.valueOf(balance));
                accountNumberField.setText(data.getAccountNumber());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadAccountDetails() {
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
                    availableBalanceField.setText(String.valueOf(balance));
                    accountNumberField.setText(data.getAccountNumber());
                }
            } catch (SQLException e){
                e.printStackTrace();
            }
        }
        else {
            Log.e("DB Connection", "Connection is null");
        }
    }

    private void depositMoney() {
        String depositAmountString = depositAmountField.getText().toString();

        if (depositAmountString.isEmpty()) {
            Toast.makeText(this, "Please enter an amount to deposit", Toast.LENGTH_SHORT).show();
            return;
        }

        double depositAmount = Double.parseDouble(depositAmountString);
        double availableBalance = Double.parseDouble(availableBalanceField.getText().toString());

        if (depositAmount <= 0) {
            Toast.makeText(this, "Deposit amount must be greater than zero", Toast.LENGTH_SHORT).show();
            return;
        }

        String updateBalanceQuery = "UPDATE BankAccount SET BankAccountBalance = BankAccountBalance + " + depositAmount + " WHERE UserEmail = '" + userEmail + "'";
        boolean isBalanceUpdated = createConnection.insertData(updateBalanceQuery);

        if (isBalanceUpdated) {
            String insertTransactionQuery = "INSERT INTO [Transaction] " +
                    "([OriginAccountId], [TargetAccountId], [TransactionAmount], [TransactionFeeCharged], [TransactionDate], [UserEmail]) " +
                    "VALUES (" +
                    "(SELECT [BankAccountId] FROM [BankAccount] WHERE [UserEmail] = '" + userEmail + "'), " +
                    "(SELECT [BankAccountId] FROM [BankAccount] WHERE [UserEmail] = '" + userEmail + "'), " + // Same account for deposit
                    depositAmount + ", 0, GETDATE(), '" + userEmail + "')";
            createConnection.insertData(insertTransactionQuery);

            String insertNotificationQuery = "INSERT INTO Notification ([NotificationMessage], [NotificationCreated], [IsRead], [Email]) " +
                    "VALUES ('Deposit of " + depositAmount + " was successful', GETDATE(), 0, '" + userEmail + "')";
            createConnection.insertData(insertNotificationQuery);

            loadAccountDetails();
finish();
            Toast.makeText(this, "Deposit successful", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Deposit failed", Toast.LENGTH_SHORT).show();
        }
    }
}
