package motloung.koena.kovsiesbanking.user;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import motloung.koena.kovsiesbanking.R;
import motloung.koena.kovsiesbanking.adapters.CreateConnection;
import motloung.koena.kovsiesbanking.adapters.UserData;

public class UserTransactions extends AppCompatActivity {

    private CreateConnection createConnection;
    private ListView listView;
    private ArrayList<String> transactionsList;
    private String userEmail;
    Button btnGoBack;
    UserData data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_transactions);
        Intent intent = getIntent();
        data = (UserData) intent.getSerializableExtra("data");
        createConnection = new CreateConnection(this);
        listView = findViewById(R.id.listView);
        transactionsList = new ArrayList<>();
        btnGoBack = findViewById(R.id.btnNotifGoBack2);

        userEmail = data.getNormalizedEmail().toLowerCase();

        if (userEmail != null && !userEmail.isEmpty()) {
            loadTransactions();
        } else {
            Toast.makeText(getBaseContext(), "No email information passed", Toast.LENGTH_LONG).show();
        }

        btnGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void loadTransactions()
    {
        Connection connection = createConnection.getConnection();
        if (connection != null) {
            try {
                String query = "SELECT OriginAccountId, TargetAccountId, TransactionAmount,  CONVERT(VARCHAR, TransactionDate, 120) AS TransactionDate, Description, UserEmail " +
                        "FROM [2019476593].[dbo].[Transaction] " +
                        "WHERE OriginAccountId = '" + data.getAccountNumber() + "' " +
                        "OR UserEmail = '" + userEmail + "';";

                PreparedStatement statement = connection.prepareStatement(query);
                ResultSet resultSet = statement.executeQuery();
                transactionsList.clear();

                while (resultSet != null && resultSet.next()) {
                    String bankAccountIdSender = resultSet.getString("OriginAccountId");
                    String bankAccountIdReceiver = resultSet.getString("TargetAccountId");
                    String amount = resultSet.getString("TransactionAmount");
                    String transactionDate = resultSet.getString("TransactionDate");

                    String transaction = "Sender: " + bankAccountIdSender +"\n"+
                            "Receiver: " + bankAccountIdReceiver + "\n"+
                            "Amount: " + amount + "\n"+
                            "Date: " + transactionDate;

                    transactionsList.add(transaction);
                }
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            Log.e("DB Connection", "Connection is null.");
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                UserTransactions.this,
                android.R.layout.simple_list_item_1,
                transactionsList
        );
        listView.setAdapter(adapter);
    }
}
