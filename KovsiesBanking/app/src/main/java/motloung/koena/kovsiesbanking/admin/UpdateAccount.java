package motloung.koena.kovsiesbanking.admin;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import motloung.koena.kovsiesbanking.R;
import motloung.koena.kovsiesbanking.adapters.CreateConnection;
import motloung.koena.kovsiesbanking.adapters.UserData;

public class UpdateAccount extends AppCompatActivity
{
    private EditText edtBankAccount;
    private EditText edtFirstName;
    private EditText edtLastName;
    private EditText edtPhoneNumber;
    private Button btnSearchBankAccount;
    private Button btnSaveChanges;
    private Connection connection;
    private CreateConnection connectHelper;
    UserData data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_account);

        connectHelper = new CreateConnection(this);
        connection = connectHelper.getConnection();
        edtBankAccount = findViewById(R.id.txtBankAcc);
        edtFirstName = findViewById(R.id.fname);
        edtLastName = findViewById(R.id.lname);
        edtPhoneNumber = findViewById(R.id.phoneno);
        btnSearchBankAccount = findViewById(R.id.findBankAcc);
        btnSaveChanges = findViewById(R.id.btnSave);
        btnSearchBankAccount.setOnClickListener(v -> searchBankAccount());

        Intent intent = getIntent();
        String role = intent.getStringExtra("role");
        data = (UserData) intent.getSerializableExtra("data");

        if(role != null)
        {
            edtBankAccount.setText(data.getAccountNumber());
            btnSearchBankAccount.performClick();
        }
        btnSaveChanges.setOnClickListener(v -> saveChanges());
    }

    private void searchBankAccount() {
        String bankacc = edtBankAccount.toString().trim();
        if (TextUtils.isEmpty(bankacc))
        {
            edtBankAccount.setError("Bank account is required!");
            Toast.makeText(this, "Bank account is required", Toast.LENGTH_SHORT).show();
        }
        String currentFname  = "";
        String currentLname  = "";
        String currentPhoneNo  = "";
        boolean dataFound = false;
        if(connection != null){

            try {
                ResultSet set = connectHelper.getData("select FirstName, LastName, PhoneNumber from AspNetUsers where AccountNo = '" + edtBankAccount.getText().toString() + "';");
                while (set.next()) {
                    dataFound = true;
                    currentFname = set.getString(1);
                    currentLname = set.getString(2);
                    currentPhoneNo = set.getString(3);
                    break;
                }
            } catch (SQLException e) {
               e.printStackTrace();
               finish();
            }
        }
        if(dataFound) {
            edtFirstName.setVisibility(View.VISIBLE);
            edtLastName.setVisibility(View.VISIBLE);
            edtPhoneNumber.setVisibility(View.VISIBLE);
            btnSaveChanges.setVisibility(View.VISIBLE);
            edtBankAccount.setEnabled(false);
            edtFirstName.setText(currentFname);
            edtLastName.setText(currentLname);
            edtPhoneNumber.setText(currentPhoneNo);
        }
    }

    private void saveChanges() {
        if (validateInputs()) {
            new AlertDialog.Builder(this)
                    .setTitle("Confirm Update")
                    .setMessage("Are you sure you want to update this user?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        // Proceed with saving changes if user confirms
                        String query = "UPDATE AspNetUsers SET FirstName = '" + edtFirstName.getText() +
                                "', LastName = '" + edtLastName.getText() +
                                "', PhoneNumber = '" + edtPhoneNumber.getText() +
                                "' WHERE AccountNo = '" + edtBankAccount.getText() + "';";
                        boolean success = connectHelper.insertData(query);
                        if (!success) {
                            Toast.makeText(this, "Error saving changes", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(this, "User updated successfully", Toast.LENGTH_SHORT).show();
                            edtFirstName.setVisibility(View.GONE);
                            edtLastName.setVisibility(View.GONE);
                            edtPhoneNumber.setVisibility(View.GONE);
                            finish();
                        }
                    })
                    .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                    .show();
        }
    }


    private boolean validateInputs() {
        if (TextUtils.isEmpty(edtBankAccount.getText())) {
            edtBankAccount.setError("Bank account is required");
            return false;
        }
        if (TextUtils.isEmpty(edtFirstName.getText())) {
            edtFirstName.setError("First name is required");
            return false;
        }
        if (TextUtils.isEmpty(edtLastName.getText())) {
            edtLastName.setError("Last name is required");
            return false;
        }
        if (TextUtils.isEmpty(edtPhoneNumber.getText())) {
            edtPhoneNumber.setError("Phone number is required");
            return false;
        }
        return true;
    }
}
