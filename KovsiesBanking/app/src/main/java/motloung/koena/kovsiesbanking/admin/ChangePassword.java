package motloung.koena.kovsiesbanking.admin;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
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

public class ChangePassword extends AppCompatActivity {

    private EditText emailInput, passwordInput, confirmPasswordInput;
    private Button submitButton;
    private Connection connection;
    private CreateConnection createConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        createConnection = new CreateConnection(this);
        connection = createConnection.getConnection();

        emailInput = findViewById(R.id.email_input);
        passwordInput = findViewById(R.id.password_input);
        confirmPasswordInput = findViewById(R.id.password_input2);
        submitButton = findViewById(R.id.login_button);

        submitButton.setOnClickListener(v -> handleSubmit());
    }

    private void handleSubmit() {
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();
        String confirmPassword = confirmPasswordInput.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_LONG).show();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Please enter a valid email", Toast.LENGTH_LONG).show();
            finish();
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

        boolean userExists = false;
        try {
            ResultSet resultSet = createConnection.getData("select Email from AspNetUsers where Email = '" + email + "';");
            if (resultSet != null && resultSet.next()) {
                userExists = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (!userExists) {
            Toast.makeText(this, "Could not find user", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        new AlertDialog.Builder(this)
                .setTitle("Confirm Password Change")
                .setMessage("Are you sure you want to change the password for this account?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    try {
                        boolean success = createConnection.insertData("UPDATE AspNetUsers SET mobilePassword = '" + password + "' WHERE Email = '" + email + "';");
                        if (!success) {
                            Toast.makeText(this, "Couldn't update password, please contact system admin!", Toast.LENGTH_LONG).show();
                            finish();
                        } else {
                            Toast.makeText(this, "Password updated successfully", Toast.LENGTH_LONG).show();
                            finish();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                })
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .show();
    }


    private boolean isValidPassword(String password) {
        // regex to check if the password has more than 5 characters, contains at least one digit and one special character
        Pattern passwordPattern = Pattern.compile("^(?=.*[0-9])(?=.*[!@#$%^&*]).{6,}$");

        return passwordPattern.matcher(password).matches();
    }
}

