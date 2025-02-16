package motloung.koena.kovsiesbanking.user;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;

import motloung.koena.kovsiesbanking.R;
import motloung.koena.kovsiesbanking.adapters.CreateConnection;
import motloung.koena.kovsiesbanking.adapters.UserData;

public class SendMoneyActivity extends AppCompatActivity
{

    private EditText recipientAccountField;
    private EditText senderAccountField;
    private EditText amountField;
    private EditText availableBalanceField;
    private Button sendMoneyButton;
    private Button btnGoBack;
    public CreateConnection createConnection;
    private String userEmail;
    UserData data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);

        recipientAccountField = findViewById(R.id.et_receiver_account);
        senderAccountField = findViewById(R.id.et_sender_account);
        amountField = findViewById(R.id.et_amount);
        sendMoneyButton = findViewById(R.id.btn_transfer);
        availableBalanceField = findViewById(R.id.et_available_balance);
        btnGoBack = findViewById(R.id.btn_go_back);

        Intent intent = getIntent();
        data = (UserData) intent.getSerializableExtra("data");

        userEmail = data.getNormalizedEmail();
        createConnection = new CreateConnection(this);
        loadSenderAccount();

        recipientAccountField.addTextChangedListener(new TextWatcher() {
                                                    @Override
                                                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                                                    @Override
                                                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                                                        if (s.length() > 10) {
                                                            recipientAccountField.setBackgroundColor(Color.RED);
                                                        } else {
                                                            recipientAccountField.setBackgroundResource(android.R.drawable.editbox_background);
                                                        }
                                                    }



                                                    @Override
                                                    public void afterTextChanged(Editable s) {}
        });

        sendMoneyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transferMoney();
            }
        });

        btnGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void loadSenderAccount2() {
        String query = "SELECT * FROM BankAccount WHERE BankAccountNumber = '" + data.getAccountNumber() + "'";
        ResultSet resultSet = createConnection.getData(query);
        try {
            if (resultSet != null && resultSet.next()) {
                String accountNumber = resultSet.getString("BankAccountNumber");
                double balance = resultSet.getDouble("BankAccountBalance");
                senderAccountField.setText(accountNumber);
                availableBalanceField.setText(String.valueOf(balance));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadSenderAccount()
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
                    String accountNumber = resultSet.getString("BankAccountNumber");
                    double balance = resultSet.getDouble("BankAccountBalance");
                    senderAccountField.setText(accountNumber);
                    availableBalanceField.setText(String.valueOf(balance));
                }
            } catch (SQLException e){
                e.printStackTrace();
            }
        }
        else {
            Log.e("DB Connection", "Connection is null");
        }
    }

    private void transferMoney() {
        String receiverAccount = recipientAccountField.getText().toString();
        String amountString = amountField.getText().toString();

        if (receiverAccount.isEmpty() || amountString.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }
        if (receiverAccount.length() != 10) {
            Toast.makeText(this, "Invalid account number", Toast.LENGTH_SHORT).show();
            return;
        }

        double amount = Double.parseDouble(amountString);
        double availableBalance = Double.parseDouble(availableBalanceField.getText().toString());

        if (amount <= 0) {
            Toast.makeText(this, "Amount must be greater than zero", Toast.LENGTH_SHORT).show();
            return;
        }
        if (amount > availableBalance) {
            Toast.makeText(this, "Insufficient funds", Toast.LENGTH_SHORT).show();
            return;
        }

        new AlertDialog.Builder(this)
                .setTitle("Confirm Transaction")
                .setMessage("Are you sure you want to transfer " + amount + " to account " + receiverAccount + "?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    String updateSenderBalanceQuery = "UPDATE [2019476593].[dbo].[BankAccount] SET [BankAccountBalance] = [BankAccountBalance] - " + amount +
                            " WHERE [UserEmail] = '" + userEmail.toLowerCase(Locale.ROOT) + "'";
                    boolean isSenderUpdated = createConnection.insertData(updateSenderBalanceQuery);

                    String updateReceiverBalanceQuery = "UPDATE [2019476593].[dbo].[BankAccount] SET [BankAccountBalance] = [BankAccountBalance] + " + amount +
                            " WHERE [BankAccountNumber] = '" + receiverAccount + "'";
                    boolean isReceiverUpdated = createConnection.insertData(updateReceiverBalanceQuery);

                    if (isSenderUpdated && isReceiverUpdated) {
                        String insertTransactionQuery = "INSERT INTO [2019476593].[dbo].[Transaction] " +
                                "([OriginAccountId], [TargetAccountId], [TransactionAmount], [TransactionFeeCharged], [TransactionDate], [UserEmail]) " +
                                "VALUES (" +
                                "(SELECT [BankAccountId] FROM [2019476593].[dbo].[BankAccount] WHERE [UserEmail] = '" + userEmail.toLowerCase() + "'), " +
                                "(SELECT [BankAccountId] FROM [2019476593].[dbo].[BankAccount] WHERE [BankAccountNumber] = '" + receiverAccount + "'), " +
                                amount + ", 0, GETDATE(), '" + userEmail.toLowerCase() + "')";

                        createConnection.insertData(insertTransactionQuery);

                        String insertSenderNotification = "INSERT INTO [2019476593].[dbo].[Notification] ([NotificationMessage], [NotificationCreated], [IsRead], [Email]) " +
                                "VALUES ('Transaction of " + amount + " to " + receiverAccount + " was successful', GETDATE(), 0, '" + userEmail.toLowerCase(Locale.ROOT) + "')";
                        createConnection.insertData(insertSenderNotification);

                        String insertReceiverNotification = "INSERT INTO [2019476593].[dbo].[Notification] ([NotificationMessage], [NotificationCreated], [IsRead], [Email]) " +
                                "VALUES ('You received " + amount + " from " + senderAccountField.getText().toString() + "', GETDATE(), 0, '" + receiverAccount + "')";
                        createConnection.insertData(insertReceiverNotification);

                        Intent i = new Intent(getBaseContext(), SuccessActivity.class);
                        i.putExtra("data", data);
                        i.putExtra("receiverAccount", receiverAccount);
                        i.putExtra("reference", "Payment to beneficiary");
                        i.putExtra("amount", amount);
                        startActivity(i);

                        Toast.makeText(this, "Transaction successful", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Transaction failed", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .show();
    }
}
