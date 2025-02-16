package motloung.koena.kovsiesbanking.user;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Pattern;

import motloung.koena.kovsiesbanking.R;
import motloung.koena.kovsiesbanking.adapters.CreateConnection;
import motloung.koena.kovsiesbanking.adapters.UserData;
import motloung.koena.kovsiesbanking.user.LoginActivity;
import motloung.koena.kovsiesbanking.user.MainActivity;

public class RegisterActivity extends AppCompatActivity {
    private LinearLayout logo_layout;
    private ScrollView registerLayout;
    Handler handler = new Handler();
    Button btnRetry, btnGoToRegister, btnLogin, btnGoToLogin, btnRegister;
    EditText txtLoginEmail, txtLoginPassword, txtRegisterFName, txtRegisterLName, txtRegisterPass,
            txtRegisterCPass, txtRegisterIdNumber, txtRegisterStudentStaff, txtRegisterEmail;
    private TextView loginLink;
    RadioGroup rdoGroup;
    CreateConnection createConnection;
    Connection connection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        createConnection = new CreateConnection(this);
        connection = null;

        logo_layout = findViewById(R.id.logo_layout);
        logo_layout.setVerticalGravity(Gravity.CENTER_VERTICAL);
        registerLayout = findViewById(R.id.registerLayout);
        btnGoToLogin = findViewById(R.id.btnGoToLogin);
        btnRegister = findViewById(R.id.btnRegister);
        txtRegisterFName = findViewById(R.id.txtRegisterFirstName);
        txtRegisterLName = findViewById(R.id.txtRegisterLastName);
        txtRegisterIdNumber = findViewById(R.id.txtRegisterIdNumber);
        txtRegisterIdNumber.setVisibility(View.GONE);
        txtRegisterStudentStaff = findViewById(R.id.txtRegisterStudentStaffNumber);
        txtRegisterPass = findViewById(R.id.txtRegisterPassword);
        txtRegisterCPass = findViewById(R.id.txtConfirmPassword);
        txtRegisterEmail = findViewById(R.id.txtRegisterEmail);
        rdoGroup = findViewById(R.id.rdoGroup);

        rdoGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (("Visitor".equals(((RadioButton) findViewById(radioGroup.getCheckedRadioButtonId())).getText().toString())))
                    txtRegisterIdNumber.setVisibility(View.VISIBLE);
                else
                    txtRegisterIdNumber.setVisibility(View.GONE);

            }
        });
        txtRegisterIdNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 13) {
                    txtRegisterIdNumber.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
                } else {
                    txtRegisterIdNumber.getBackground().clearColorFilter();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        txtRegisterStudentStaff.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 10) {
                    txtRegisterStudentStaff.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
                } else {
                    txtRegisterStudentStaff.getBackground().clearColorFilter();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        btnGoToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }

    private String generateUsername(String firstName, String lastName, String studentNumber) {
        if (firstName.isEmpty() || lastName.isEmpty() || studentNumber.isEmpty()) {
            return "";
        }
        String firstInitial = String.valueOf(firstName.charAt(0)).toUpperCase();
        String lastInitial = String.valueOf(lastName.charAt(0)).toUpperCase();
        return lastInitial + firstInitial + studentNumber;
    }

    public boolean anyEmpty(EditText... controls) {
        return Arrays.stream(controls).anyMatch(c -> !(c.getText().toString().length() > 0));
    }

    public void displayErrorIfEmpty(EditText... controls) {
        for (EditText control : Arrays.stream(controls)
                .filter(c -> !(c.getText().toString().length() > 0)).toArray(EditText[]::new))
            control.setError("Please fill in value");
    }

    private void registerUser() {
        String firstName = txtRegisterFName.getText().toString().trim();
        String lastName = txtRegisterLName.getText().toString().trim();
        String email = txtRegisterEmail.getText().toString().trim();
        String studentStaffNumber = txtRegisterStudentStaff.getText().toString().trim();
        String idPassport = txtRegisterIdNumber.getText().toString().trim();
        String password = txtRegisterPass.getText().toString().trim();
        String confirmPassword = txtRegisterCPass.getText().toString().trim();

        if (TextUtils.isEmpty(firstName) || TextUtils.isEmpty(lastName) ||
                TextUtils.isEmpty(email) || TextUtils.isEmpty(studentStaffNumber) ||
                TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {
            Toast.makeText(getBaseContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(getBaseContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        if(studentStaffNumber.length() !=10 && studentStaffNumber.length()!=7)
        {
            Toast.makeText(getBaseContext(), "Invalid staff/student number", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isValidEmail(email))
        {
            txtRegisterEmail.setError("Only @ufs4life.ac.za && @ufs.ac.za domains are allowed");
            return;
        }
        if(!isValidPassword(confirmPassword))
        {
            txtRegisterCPass.setError("Password must be more than 5 characters, contain a digit, and a special character.");
            return;
        }
        try {
            String checkStudentQuery = "SELECT COUNT(*) FROM AspNetUsers WHERE StudentEmployeeNumber = '" + studentStaffNumber + "'";
            Statement checkStatement = createConnection.getConnection().createStatement();
            ResultSet resultSet = checkStatement.executeQuery(checkStudentQuery);

            if (resultSet != null && resultSet.next()) {
                int count = resultSet.getInt(1);
                if (count > 0) {
                    Toast.makeText(getBaseContext(), "Student number already exists.", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            String accountNumber = generateAccountNumber();

            UUID uuid = UUID.randomUUID();
            String Id = uuid.toString();

            String phoneNumber = "";
            String dateOfBirth = "GETDATE()";
            String profile = null;

            String sqlQuery = "INSERT INTO AspNetUsers " +
                    "(Id, StudentEmployeeNumber, FirstName, LastName, PassportIDNumber, EmailAdress, mobilePassword, " +
                    "DateOfBirth, Profile, AccountNo, Address, PhoneNumber, Loan, " +
                    "AdvisorName, UserName, NormalizedUserName, Email, NormalizedEmail, " +
                    "EmailConfirmed, PhoneNumberConfirmed, TwoFactorEnabled, LockoutEnd, LockoutEnabled, AccessFailedCount) " +
                    "VALUES ('" + Id + "', '" + studentStaffNumber + "', '" + firstName + "', '" + lastName + "', '" +
                    idPassport + "', '" + email + "', '" + password + "', " + dateOfBirth + ", " +
                    (profile == null ? "NULL" : "'" + profile + "'") + ", '" + accountNumber + "', NULL, '" +
                    phoneNumber + "', 0, NULL, '" + generateUsername(firstName, lastName, studentStaffNumber) + "', '" +
                    generateUsername(firstName, lastName, studentStaffNumber).toUpperCase() + "', '" + email + "', '" +
                    email.toUpperCase() + "', 0, 0, 0, NULL, 0, 0); " +
                    "INSERT INTO BankAccount " +
                    "(BankAccountNumber, BankAccountDateCreated, BankAccountBalance, IsActive, UserEmail) " +
                    "VALUES ('" + accountNumber + "', GETDATE(), 0, 0, '" + email + "');";

            Statement statement = createConnection.getConnection().createStatement();
            int result = statement.executeUpdate(sqlQuery);

            if (result > 0) {
                Toast.makeText(getBaseContext(), "Registration successful!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(getBaseContext(), "Registration failed", Toast.LENGTH_SHORT).show();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Toast.makeText(getBaseContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }


    private String generateAccountNumber() {
        Random random = new Random();
        StringBuilder accountNumber = new StringBuilder();

        for (int i = 0; i < 10; i++) {
            accountNumber.append(random.nextInt(10));
        }

        return accountNumber.toString();
    }
    private ResultSet getData(Connection connection, String query) {
        ResultSet resultSet = null;
        try {
            Statement statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultSet;
    }
    private boolean isValidEmail(String email) {
        return email.endsWith("@ufs4life.ac.za") || email.endsWith("@ufs.ac.za");
    }
    private boolean isValidPassword(String password) {
        // Check if the password has more than 5 characters, contains at least one digit and one special character
        Pattern passwordPattern = Pattern.compile("^(?=.*[0-9])(?=.*[!@#$%^&*]).{6,}$");
        return passwordPattern.matcher(password).matches();
    }

    private String generateUniqueAccountNumber(Connection connection) throws SQLException {
        String accountNumber;
        boolean isUnique;
        do {
            accountNumber = String.format("%010d", new Random().nextInt(1000000000));
            ResultSet resultSet = getData(connection, "SELECT AccountNo FROM AspNetUsers WHERE AccountNo = '" + accountNumber + "'");
            isUnique = !resultSet.next();
        } while (!isUnique);

        return accountNumber;
    }

    private void goToHomePage(UserData data) {
        Intent home = new Intent(getBaseContext(), MainActivity.class);
        home.putExtra("data", (Serializable) data);
        startActivity(home);
        finish();
    }
}
