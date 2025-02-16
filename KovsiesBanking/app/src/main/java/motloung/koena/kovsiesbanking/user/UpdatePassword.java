package motloung.koena.kovsiesbanking.user;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Pattern;

import motloung.koena.kovsiesbanking.R;
import motloung.koena.kovsiesbanking.adapters.CreateConnection;
import motloung.koena.kovsiesbanking.adapters.UserData;

public class UpdatePassword extends AppCompatActivity
{
    private EditText emailInput, passwordInput, confirmPasswordInput;
    private Button submitButton;
    private Connection connection;
    private CreateConnection createConnection;
    UserData data;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);

        createConnection = new CreateConnection(this);
        connection = createConnection.getConnection();

        Intent intent = getIntent();
        data = (UserData) intent.getSerializableExtra("data");

        passwordInput = findViewById(R.id.password_input_user);
        confirmPasswordInput = findViewById(R.id.confirm_password_user);
        submitButton = findViewById(R.id.login_button);

        submitButton.setOnClickListener(v -> handleSubmit());
    }

    private void handleSubmit() {
        String password = passwordInput.getText().toString().trim();
        String confirmPassword = confirmPasswordInput.getText().toString().trim();

        if (TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_LONG).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_LONG).show();
            return;
        }

        if (!isValidPassword(confirmPassword)) {
            confirmPasswordInput.setError("Password must be more than 5 characters, contain a digit, and a special character.");
            return;
        }


        boolean success = false;
        try {
            ResultSet resultSet = createConnection.getData("select Email from AspNetUsers where Email = '" + data.getNormalizedEmail().toLowerCase() + "';");
            if (resultSet != null && resultSet.next()) {
                success = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (!success) {
            Toast.makeText(this, "Could not find user", Toast.LENGTH_LONG).show();
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm Password Update");
        builder.setMessage("Are you sure you want to update your password?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                boolean success = false;
                try {
                    success = createConnection.insertData("UPDATE AspNetUsers SET mobilePassword = '" + password + "' WHERE Email = '" + data.getNormalizedEmail().toLowerCase() + "';");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (!success) {
                    Toast.makeText(UpdatePassword.this, "Couldn't update password , please contact system admin!", Toast.LENGTH_LONG).show();
                    return;
                }
                Toast.makeText(UpdatePassword.this, "Password updated successfully", Toast.LENGTH_LONG).show();
                finish();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Toast.makeText(UpdatePassword.this, "Password update cancelled", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }
    private boolean isValidPassword(String password) {
        // Check if the password has more than 5 characters, contains at least one digit and one special character
        Pattern passwordPattern = Pattern.compile("^(?=.*[0-9])(?=.*[!@#$%^&*]).{6,}$");

        return passwordPattern.matcher(password).matches();
    }
}
