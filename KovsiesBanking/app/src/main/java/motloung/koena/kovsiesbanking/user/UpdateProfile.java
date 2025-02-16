package motloung.koena.kovsiesbanking.user;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;
import java.util.Locale;

import motloung.koena.kovsiesbanking.R;
import motloung.koena.kovsiesbanking.adapters.CreateConnection;
import motloung.koena.kovsiesbanking.adapters.UserData;

public class UpdateProfile extends AppCompatActivity
{
    TextView txtAccountNumber, txtEmail, txtStudentNumber;
    EditText txtName, txtSurname, txtPhoneNumber;
    private Connection connection;
    private CreateConnection createConnection;
    UserData data;
    Button btnSaveChanges, btnGoBack;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updateprofile);

        txtName = findViewById(R.id.txtFirstNameUpdate);
        txtSurname = findViewById(R.id.txtLastNameUpdate);
        txtPhoneNumber = findViewById(R.id.txtPhoneNumberUpdate);
        txtAccountNumber = findViewById(R.id.txtAccountNumberUpdate);
        txtEmail = findViewById(R.id.txtEmailUpdate);
        txtStudentNumber = findViewById(R.id.txtStudentNumberUpdate);
        btnSaveChanges = findViewById(R.id.buttonSaveChangesUpdate);

        createConnection = new CreateConnection(this);
        connection = createConnection.getConnection();

        Intent intent = getIntent();
        data = (UserData) intent.getSerializableExtra("data");

        txtName.setText(data.getFirstName());
        txtSurname.setText(data.getLastName());
        txtPhoneNumber.setText(data.getPhoneNumebr());
        txtAccountNumber.setText(data.getAccountNumber());
        txtEmail.setText(data.getNormalizedEmail().toLowerCase(Locale.ROOT));
        txtStudentNumber.setText(data.getStudentEmployeeNumber());
        //btnGoBack = findViewById(R.id.btn_go_back);
        btnSaveChanges.setOnClickListener(view -> updateInfo());
    }

    private void updateInfo() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm Update");
        builder.setMessage("Are you sure you want to update your personal details?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String query = "UPDATE AspNetUsers SET FirstName = '" + txtName.getText() + "', LastName = '" + txtSurname.getText() + "', PhoneNumber = '" + txtPhoneNumber.getText() + "' WHERE AccountNo = '" + data.getAccountNumber() + "';";
                boolean success = createConnection.insertData(query);
                if(!success){
                    Toast.makeText(UpdateProfile.this, "Error saving changes", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(UpdateProfile.this, "Changes saved successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(UpdateProfile.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) 
            {
                Toast.makeText(UpdateProfile.this, "Update cancelled", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                finish();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

}