package motloung.koena.kovsiesbanking.consultant;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import motloung.koena.kovsiesbanking.R;
import motloung.koena.kovsiesbanking.adapters.CreateConnection;

public class CLoginSessions extends AppCompatActivity
{
    private Connection connection;
    private CreateConnection connectHelper;
    Button btnGoBack;
    ListView listViewUsers;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_sessions);

        connectHelper = new CreateConnection(this);
        connection = connectHelper.getConnection();
        btnGoBack = findViewById(R.id.buttonGoBack2);
        btnGoBack.setOnClickListener(v -> btnGoBack(v));

        listViewUsers = findViewById(R.id.listViewUsers2);
       List<String> loginSessions = new ArrayList<>();
        try {
            ResultSet resultSet = connectHelper.getData("select UserEmail, LoginDateTime from UserLogins");
            while (resultSet != null && resultSet.next()) {
                loginSessions.add(resultSet.getString(1) + "\t\t" + "\n" + resultSet.getString(2));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, loginSessions);
        listViewUsers.setAdapter(adapter);
    }

    private void btnGoBack(View v) {
        finish();
    }
}
