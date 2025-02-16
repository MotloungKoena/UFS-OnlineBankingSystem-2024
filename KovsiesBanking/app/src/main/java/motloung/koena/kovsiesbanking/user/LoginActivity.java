package motloung.koena.kovsiesbanking.user;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import motloung.koena.kovsiesbanking.R;
import motloung.koena.kovsiesbanking.adapters.CreateConnection;
import motloung.koena.kovsiesbanking.adapters.UserData;
import motloung.koena.kovsiesbanking.helpers.DatabaseHelperIdentity;
import motloung.koena.kovsiesbanking.helpers.Helpers;
import motloung.koena.kovsiesbanking.roleActivities.AdminHomeActivity;
import motloung.koena.kovsiesbanking.roleActivities.AdvisorHomeActivity;
import motloung.koena.kovsiesbanking.roleActivities.ConsultantHomeActivity;

public class LoginActivity extends Activity {
    private DatabaseHelperIdentity mIdentityDbHelper;
    private EditText studentNumberField;
    private EditText passwordField;
    private Button loginButton;
    private TextView registerLink;

    CreateConnection createConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        createConnection = new CreateConnection(this);

        studentNumberField = findViewById(R.id.student_number);
        passwordField = findViewById(R.id.password);
        loginButton = findViewById(R.id.login_button);
        registerLink = findViewById(R.id.register_link);
        loginButton.setOnClickListener(v -> attemptLogin(createConnection.getConnection()));

        studentNumberField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 10) {
                    studentNumberField.setBackgroundColor(Color.RED);
                } else {
                    studentNumberField.setBackgroundResource(android.R.drawable.editbox_background);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private Toast createMessage(Context context, CharSequence text) {
        Toast t = new Toast(context);
        t.setText(text);
        return t;
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

    private void attemptLogin(Connection connection) {
        String studentNumber = studentNumberField.getText().toString();
        String password = passwordField.getText().toString();
        if(TextUtils.isEmpty(studentNumber) || TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "Please fill in all fields!", Toast.LENGTH_SHORT).show();
        }
        if (!Helpers.anyEmpty(studentNumberField, passwordField)) {
            ResultSet resultSet = getData(connection,
                    "SELECT * FROM AspNetUsers");
            String loginPassword = "", loginEmail = "", userId = "";
            String normalizedEmail = "" ;
            String firstName = "" ;
            String accountNumber = "";
            String studNumber = "";
            String lastName = "";
            String userName = "";
            String phoneNumber = "";
            String role = "";
            try {
                while (resultSet != null && resultSet.next()) {
                    if (resultSet.getString(2).toUpperCase().equals(studentNumber.toUpperCase())
                            || resultSet.getString(16).equals(studentNumber.toUpperCase())) {

                        userId = resultSet.getString(1);
                        loginEmail = studentNumber;
                        loginPassword = resultSet.getString(7);
                        normalizedEmail = resultSet.getString(18);
                        firstName = resultSet.getString(3);
                        accountNumber = resultSet.getString(10);
                        studNumber = resultSet.getString(2);
                        lastName = resultSet.getString(4);
                        userName = resultSet.getString(16);
                        phoneNumber = resultSet.getString(12);

                        String studentEmployeeNumber = resultSet.getString("StudentEmployeeNumber");
                        switch (studentEmployeeNumber) {
                            case "Admin":
                                role = "Admin";
                                break;
                            case "Consultant":
                                role = "Consultant";
                                break;
                            case "Advisor":
                                role = "FinancialAdvisor";
                                break;
                            default:
                                role = "User";
                                break;
                        }
                        break;
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            if ("".equals(loginEmail)) {
                createMessage(this, "Couldn't find user").show();
            }
            if (loginEmail.equals(studentNumber) && loginPassword.equals(password)) {
                Toast.makeText(getBaseContext(), "Successfully logged in as " + firstName, Toast.LENGTH_SHORT).show();
                goToHomePage(new UserData(studentNumber, userId, normalizedEmail, firstName, accountNumber, studNumber, lastName, userName, phoneNumber), role);
            } else {
                createMessage(this, "Incorrect student/staff number or password").show();
            }
        }
    }

    private void goToHomePage(UserData data, String role)
    {
        Intent home;
        switch (role) {
            case "Admin":
                home = new Intent(LoginActivity.this, AdminHomeActivity.class);
                break;
            case "Consultant":
                home = new Intent(LoginActivity.this, ConsultantHomeActivity.class);
                break;
            case "FinancialAdvisor":
                home = new Intent(LoginActivity.this, AdvisorHomeActivity.class);
                break;
            default:
                home = new Intent(LoginActivity.this, HomeActivity.class);
                break;
        }
        home.putExtra("data", (Serializable) data);
        home.putExtra("role", (Serializable) role);

        startActivity(home);
        finish();
    }








































































    /*private void attemptLogin(Connection connection) {
        String username = studentNumberField.getText().toString();
        String password = passwordField.getText().toString();

        if (!Helpers.anyEmpty(studentNumberField, passwordField)) {
            ResultSet resultSet = getData(connection,
                    "SELECT * FROM AspNetUsers");
            String loginPassword = "", loginEmail = "", userId = "";
            String normalizedEmail ="" ;
            String firstName ="" ;
            String accountNumber="";
            String studNumber="";
            String lastName="";
            String userName="";
            try {
                while (resultSet != null && resultSet.next()) {
                    if (resultSet.getString(16).equals(username.toUpperCase())) {
                        userId = resultSet.getString(1);
                        loginEmail = username;
                        loginPassword = resultSet.getString(7);
                        normalizedEmail = resultSet.getString(18);
                        firstName = resultSet.getString((3));
                        accountNumber = resultSet.getString(10);
                        studNumber = resultSet.getString(2);
                        lastName = resultSet.getString(4);
                        userName = resultSet.getString(16);
                        break;
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            /*if (role.equals("Staff") || role.equalsIgnoreCase("student") || role.equals("User")) {
                intent = new Intent(MainActivity.this, UserActivity.class);
            } else if (role.equals("Consultant") || role.equals("Admin")) {
                intent = new Intent(MainActivity.this, CConsultants.class); // or ConsultantActivity, FinancialAdvisorActivity
            } else {
                intent = new Intent(MainActivity.this, Advisor.class);
            }*/
            /*if ("".equals(loginEmail)) createMessage(this, "Couldn't find user").show();
            if (loginEmail.equals(username) && loginPassword.equals(password))
            {
                Toast.makeText(getBaseContext(), "Successfully logged in as " + firstName, Toast.LENGTH_SHORT).show();
                goToHomePage(new UserData(username, userId, normalizedEmail,firstName , accountNumber, studNumber, lastName, userName));
            }
            else createMessage(this, "Incorrect email or password").show();
        }
    }*/
}
