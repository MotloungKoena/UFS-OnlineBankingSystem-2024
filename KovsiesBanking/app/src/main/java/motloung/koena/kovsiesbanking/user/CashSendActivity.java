package motloung.koena.kovsiesbanking.user;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

import motloung.koena.kovsiesbanking.R;
import motloung.koena.kovsiesbanking.adapters.CreateConnection;
import motloung.koena.kovsiesbanking.adapters.UserData;

public class CashSendActivity extends AppCompatActivity
{
    private EditText sendAmountField, availableBalanceField, senderAccountField;
    private Button sendButton, btnGoBack;
    private TextView generatedCodeView, generatedPasswordView;
    private CreateConnection createConnection;
    private String userEmail;
    UserData data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_cash);

        senderAccountField = findViewById(R.id.edtSenderAccount_send);
        sendAmountField = findViewById(R.id.Amount_send);
        availableBalanceField = findViewById(R.id.AvailableBalance_send);
        sendButton = findViewById(R.id.btn_send_cash);
        btnGoBack = findViewById(R.id.btn_go_back_send);
        generatedCodeView = findViewById(R.id.tv_generated_code);
        generatedPasswordView = findViewById(R.id.tv_generated_password);

        Intent intent = getIntent();
        data = (UserData) intent.getSerializableExtra("data");

        userEmail = data.getNormalizedEmail().toLowerCase();
        createConnection = new CreateConnection(this);
        loadAccountDetails();

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendCash();
            }
        });

        btnGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(CashSendActivity.this, HomeActivity.class);
                intent1.putExtra("data", data);
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
                senderAccountField.setText(data.getAccountNumber());
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
                    senderAccountField.setText(data.getAccountNumber());
                }
            } catch (SQLException e){
                e.printStackTrace();
            }
        }
        else {
            Log.e("DB Connection", "Connection is null");
        }
    }
    private void sendCash() {
        String sendAmountString = sendAmountField.getText().toString();

        if (sendAmountString.isEmpty()) {
            Toast.makeText(this, "Please enter an amount to send", Toast.LENGTH_SHORT).show();
            return;
        }

        double sendAmount = Double.parseDouble(sendAmountString);
        double availableBalance = Double.parseDouble(availableBalanceField.getText().toString());

        if (sendAmount <= 0) {
            Toast.makeText(this, "Amount must be greater than zero", Toast.LENGTH_SHORT).show();
            return;
        }
        if (sendAmount > availableBalance) {
            Toast.makeText(this, "Insufficient balance", Toast.LENGTH_SHORT).show();
            return;
        }

        new AlertDialog.Builder(this)
                .setTitle("Confirm Cash Send")
                .setMessage("Are you sure you want to send " + sendAmount + " cash to be withdrawn at DH?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    String cashCode = generateRandomCode(10);
                    String cashPassword = generateRandomCode(4);

                    String updateBalanceQuery = "UPDATE BankAccount SET BankAccountBalance = BankAccountBalance - " + sendAmount +
                            " WHERE UserEmail = '" + userEmail + "'";
                    boolean isBalanceUpdated = createConnection.insertData(updateBalanceQuery);

                    if (isBalanceUpdated) {
                        String insertTransactionQuery = "INSERT INTO [Transaction] " +
                                "([OriginAccountId], [TargetAccountId], [TransactionAmount], [TransactionFeeCharged], [TransactionDate], [UserEmail]) " +
                                "VALUES ((SELECT [BankAccountId] FROM [BankAccount] WHERE [UserEmail] = '" + userEmail + "'), NULL, " +
                                sendAmount + ", 0, GETDATE(), '" + userEmail + "')";
                        createConnection.insertData(insertTransactionQuery);

                        String notificationMessage = "Cash Send successful. \nCode: " + cashCode + " Password: " + cashPassword;
                        String insertNotificationQuery = "INSERT INTO Notification ([NotificationMessage], [NotificationCreated], [IsRead], [Email]) " +
                                "VALUES ('" + notificationMessage + "', GETDATE(), 0, '" + userEmail + "')";
                        createConnection.insertData(insertNotificationQuery);

                        loadAccountDetails();

                        generatedCodeView.setText("Code: " + cashCode);
                        generatedPasswordView.setText("Password: " + cashPassword);
                        generatedCodeView.setVisibility(View.VISIBLE);
                        generatedPasswordView.setVisibility(View.VISIBLE);

                        sendButton.setVisibility(View.GONE);

                        Toast.makeText(this, "Cash Send successful!", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(this, "Cash Send failed", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private String generateRandomCode(int length) {
        Random random = new Random();
        StringBuilder code = new StringBuilder();

        for (int i = 0; i < length; i++) {
            code.append(random.nextInt(10)); // Append a random digit from 0 to 9
        }
        return code.toString();
    }
}








































/*package motloung.koena.kovsiesbanking.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

import motloung.koena.kovsiesbanking.R;
import motloung.koena.kovsiesbanking.adapters.CreateConnection;
import motloung.koena.kovsiesbanking.adapters.UserData;

public class CashSendActivity extends AppCompatActivity {

    private EditText sendAmountField, availableBalanceField;
    private Button sendButton, btnGoBack;
    private CreateConnection createConnection;
    private String userEmail;
    UserData data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_send);

        // Initialize UI elements
        sendAmountField = findViewById(R.id.Amount_send);
        availableBalanceField = findViewById(R.id.AvailableBalance_send);
        sendButton = findViewById(R.id.btn_send);
        btnGoBack = findViewById(R.id.btn_go_back_send);

        Intent intent = getIntent();
        data = (UserData) intent.getSerializableExtra("data");

        userEmail = data.getNormalizedEmail().toLowerCase(); // Convert to lowercase for uniformity
        createConnection = new CreateConnection(this);
        loadAccountDetails();

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendCash();
            }
        });

        btnGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(CashSendActivity.this, HomeActivity.class);
                intent1.putExtra("data", data);
                startActivity(intent1);
            }
        });
    }

    // Load the current account balance
    private void loadAccountDetails() {
        String query = "SELECT * FROM BankAccount WHERE BankAccountNumber = '" + data.getAccountNumber() + "'";
        ResultSet resultSet = createConnection.getData(query);
        try {
            if (resultSet != null && resultSet.next()) {
                double balance = resultSet.getDouble("BankAccountBalance");
                availableBalanceField.setText(String.valueOf(balance));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Handle the cash send process
    private void sendCash() {
        String sendAmountString = sendAmountField.getText().toString();

        // Validate input
        if (sendAmountString.isEmpty()) {
            Toast.makeText(this, "Please enter an amount to send", Toast.LENGTH_SHORT).show();
            return;
        }

        double sendAmount = Double.parseDouble(sendAmountString);
        double availableBalance = Double.parseDouble(availableBalanceField.getText().toString());

        // Ensure the send amount is valid and user has sufficient balance
        if (sendAmount <= 0) {
            Toast.makeText(this, "Amount must be greater than zero", Toast.LENGTH_SHORT).show();
            return;
        }
        if (sendAmount > availableBalance) {
            Toast.makeText(this, "Insufficient balance", Toast.LENGTH_SHORT).show();
            return;
        }

        // Generate a random 10-digit code and a 4-digit password
        String cashCode = generateRandomCode(10);
        String cashPassword = generateRandomCode(4);

        // Update the account balance
        String updateBalanceQuery = "UPDATE BankAccount SET BankAccountBalance = BankAccountBalance - " + sendAmount + " WHERE UserEmail = '" + userEmail + "'";
        boolean isBalanceUpdated = createConnection.insertData(updateBalanceQuery);

        if (isBalanceUpdated) {
            // Insert a transaction record
            String insertTransactionQuery = "INSERT INTO [Transaction] " +
                    "([OriginAccountId], [TargetAccountId], [TransactionAmount], [TransactionFeeCharged], [TransactionDate], [UserEmail], [TransactionType]) " +
                    "VALUES (" +
                    "(SELECT [BankAccountId] FROM [BankAccount] WHERE [UserEmail] = '" + userEmail + "'), NULL, " + // NULL for TargetAccountId in cash send
                    sendAmount + ", 0, GETDATE(), '" + userEmail + "', 'CashSend')";
            createConnection.insertData(insertTransactionQuery);

            // Insert notification with cash code and password
            String notificationMessage = "Cash Send successful. Code: " + cashCode + " Password: " + cashPassword;
            String insertNotificationQuery = "INSERT INTO Notification ([NotificationMessage], [NotificationCreated], [IsRead], [Email]) " +
                    "VALUES ('" + notificationMessage + "', GETDATE(), 0, '" + userEmail + "')";
            createConnection.insertData(insertNotificationQuery);

            // Reload the account details to show the updated balance
            loadAccountDetails();
            Toast.makeText(this, "Cash Send successful! Code: " + cashCode + " Password: " + cashPassword, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Cash Send failed", Toast.LENGTH_SHORT).show();
        }
    }

    // Generate a random numeric code of the specified length
    private String generateRandomCode(int length) {
        Random random = new Random();
        StringBuilder code = new StringBuilder();

        for (int i = 0; i < length; i++) {
            code.append(random.nextInt(10)); // Append a random digit from 0 to 9
        }
        return code.toString();
    }
}*/
