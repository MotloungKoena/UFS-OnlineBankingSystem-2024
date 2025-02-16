package motloung.koena.kovsiesbanking.consultant;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import motloung.koena.kovsiesbanking.R;
import motloung.koena.kovsiesbanking.adapters.CreateConnection;
import motloung.koena.kovsiesbanking.adapters.UserData;
import motloung.koena.kovsiesbanking.financialAdvisor.Advice;

public class CViewAllUsers extends AppCompatActivity {

    private Connection connection;
    private CreateConnection createConnection;
    Button btnGoBack;
    TextView textViewViewUsers;
    ListView listViewUsers;
    UserData data;
    private boolean proceedDelete = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_users);

        createConnection = new CreateConnection(this);
        connection = createConnection.getConnection();

        Intent intent1 = getIntent();
        data = (UserData) intent1.getSerializableExtra("data");
        btnGoBack = findViewById(R.id.buttonGoBack);
        textViewViewUsers = findViewById(R.id.textViewViewUsers2);
        btnGoBack.setOnClickListener(v -> btnGoBack(v));

        listViewUsers = findViewById(R.id.listViewUsers);
        Intent intent = getIntent();
        String role = intent.getStringExtra("role");
        if (role != null && role.equals("FinancialAdvisor"))
        {
            textViewViewUsers.setText("Select a user to give your advice!");
            listViewUsers.setOnItemClickListener((adapterView, view, i, l) -> handleItemClick(adapterView, view, i, l));
        }

        listViewUsers.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                return longClickDelete(parent, view, position, id);
            }
        });
        List<String> users = new ArrayList<>();
        try {
            ResultSet resultSet = createConnection.getData("select FirstName, LastName, AccountNo from AspNetUsers");
            while (resultSet != null && resultSet.next()) {
                users.add(resultSet.getString(1) + " " + resultSet.getString(2) + "\n" + resultSet.getString(3));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,
                users.stream().filter(u -> !u.contains("Nick")).toArray(String[]::new));  //NB - KovsiesBanking consultant is not hardcoded
        listViewUsers.setAdapter(adapter);
    }

    private void handleItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        String AccountNumber = listViewUsers.getItemAtPosition(i).toString().split("\n")[1];
        Intent intent = new Intent(CViewAllUsers.this, Advice.class);
        intent.putExtra("accountnumber", AccountNumber);
        intent.putExtra("data",data);
        startActivity(intent);
    }


     private boolean longClickDelete(AdapterView<?> adapterView, View view, int i, long l) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete user")
                .setMessage("Are you sure you want to delete this user?")
                .setCancelable(false)
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String id = listViewUsers.getItemAtPosition(i).toString().split("\n")[1];
                        boolean deleted = false;
                        try{
                            deleted = createConnection.insertData("DELETE FROM AspNetUsers " +
                                    "WHERE AccountNo = "+ "" + id + ";");
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        if(deleted){
                            Toast.makeText(CViewAllUsers.this, "User deleted Successfully", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(CViewAllUsers.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(CViewAllUsers.this, "Deletion cancelled", Toast.LENGTH_SHORT).show();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();

        return true;
    }

    private void btnGoBack(View v) {
        finish();
    }
}
