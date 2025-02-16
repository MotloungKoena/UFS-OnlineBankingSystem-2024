package motloung.koena.kovsiesbanking.user;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import motloung.koena.kovsiesbanking.R;
import motloung.koena.kovsiesbanking.adapters.CreateConnection;
import motloung.koena.kovsiesbanking.adapters.UserData;

public class Notifications extends AppCompatActivity
{
    private CreateConnection createConnection;
    private ListView listView;
    private ArrayList<String> notificationsList;
    private String userEmail;
    UserData data;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_notifications);

        findViewById(R.id.btnNotifGoBack2).setOnClickListener(v -> finish());

        createConnection = new CreateConnection(this);
        listView = findViewById(R.id.listView1);
        notificationsList = new ArrayList<>();

        Intent intent = getIntent();
        data = (UserData) intent.getSerializableExtra("data");

        userEmail = data.getNormalizedEmail().toLowerCase();

        if (userEmail != null && !userEmail.isEmpty()) {
            loadNotifications();
        } else {
            Toast.makeText(Notifications.this, "No email passed from previous activity", Toast.LENGTH_LONG).show();
        }
    }
    private void loadNotifications()
    {
        Connection connection = createConnection.getConnection();
        if (connection != null) {
            try {
                String query = "SELECT \n" +
                        "    NotificationId, \n" +
                        "    NotificationMessage, \n" +
                        "    CONVERT(VARCHAR, NotificationCreated, 120) AS NotificationCreated,  -- Format: yyyy-mm-dd hh:mi:ss\n" +
                        "    IsRead, \n" +
                        "    Email \n" +
                        "FROM Notification " +
                        "WHERE Email = '" + userEmail + "' " +
                        "OR Email = '" + data.getAccountNumber() + "'";

                PreparedStatement statement = connection.prepareStatement(query);
                ResultSet resultSet = statement.executeQuery();
                notificationsList.clear();

                while (resultSet != null && resultSet.next()) {
                    String id = resultSet.getString("NotificationId");
                    String message = resultSet.getString("NotificationMessage");
                    String notificationDate = resultSet.getString("NotificationCreated");
                    String isRead = resultSet.getString("IsRead");
                    String userEmail = resultSet.getString("Email");

                    String notification =
                            "Message: " + message + "\n"+
                                    "Date: " + notificationDate +"\n"+
                                    "Is Read: True"  ;/*+"\n"+
                                "Email: " + userEmail;*/

                    notificationsList.add(notification);
                }
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            Log.e("DB Connection", "Connection is null.");
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                Notifications.this,
                android.R.layout.simple_list_item_1,
                notificationsList
        );
        listView.setAdapter(adapter);
    }

    private class FetchNotificationsTask extends AsyncTask<Void, Void, Boolean> {

        private final String userEmail;

        public FetchNotificationsTask(String userEmail) {
            this.userEmail = userEmail;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            Connection connection = createConnection.getConnection();
            if (connection != null) {
                String query = "SELECT \n" +
                        "    NotificationId, \n" +
                        "    NotificationMessage, \n" +
                        "    CONVERT(VARCHAR, NotificationCreated, 120) AS NotificationCreated,  -- Format: yyyy-mm-dd hh:mi:ss\n" +
                        "    IsRead, \n" +
                        "    Email \n" +
                        "FROM Notification " +
                        "WHERE Email = '" + userEmail + "' " +
                        "OR Email = '" + data.getAccountNumber() + "'";
                ResultSet resultSet = createConnection.getData(query);

                try {
                    notificationsList.clear();
                    while (resultSet != null && resultSet.next()) {
                        String id = resultSet.getString("NotificationId");
                        String message = resultSet.getString("NotificationMessage");
                        String notificationDate = resultSet.getString("NotificationCreated");
                        String isRead = resultSet.getString("IsRead");
                        String userEmail = resultSet.getString("Email");

                        String notification =
                                "Message: " + message + "\n"+
                                "Date: " + notificationDate +"\n"+
                                "Is Read: True"  ;/*+"\n"+
                                "Email: " + userEmail;*/

                        notificationsList.add(notification);
                    }
                    resultSet.close();
                    connection.close();
                    return true;
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean success)
        {
            if (success) {
                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                        Notifications.this,
                        android.R.layout.simple_list_item_1,
                        notificationsList
                );
                listView.setAdapter(adapter);
            } else {
                Toast.makeText(Notifications.this, "Failed to retrieve data from database", Toast.LENGTH_LONG).show();
            }
        }
    }
}
