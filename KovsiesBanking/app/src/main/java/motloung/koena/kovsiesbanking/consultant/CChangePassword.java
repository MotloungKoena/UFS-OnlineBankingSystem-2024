package motloung.koena.kovsiesbanking.consultant;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import motloung.koena.kovsiesbanking.R;
import motloung.koena.kovsiesbanking.adapters.CreateConnection;

public class CChangePassword extends AppCompatActivity {

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
            finish();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Please enter a valid email", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        boolean Success = false;
        try {
            ResultSet resultSet = createConnection.getData("select Email from AspNetUsers where Email = '" + email + "';");
            if (resultSet != null && resultSet.next()) {
                Success = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if(!Success){
            Toast.makeText(this, "Could not find user", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        try {
           Success = createConnection.insertData("UPDATE AspNetUsers SET mobilePassword = '" + password + "' WHERE Email = '" + email + "';");
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(!Success){
            Toast.makeText(this, "Couldn't update password , please contact system admin!", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        Toast.makeText(this, "Password updated successfully", Toast.LENGTH_LONG).show();
        finish();
    }
}

