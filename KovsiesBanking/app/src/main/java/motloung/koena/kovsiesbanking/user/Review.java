package motloung.koena.kovsiesbanking.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

import motloung.koena.kovsiesbanking.R;
import motloung.koena.kovsiesbanking.adapters.CreateConnection;
import motloung.koena.kovsiesbanking.adapters.UserData;
import motloung.koena.kovsiesbanking.user.HomeActivity;

public class Review extends AppCompatActivity
{
    UserData data;
    private EditText etComment, etUserEmail, etFirstName, etLastName;
    private TextView etRate;
    private Button btnSubmit, btnGoBack;
    private CreateConnection connectHelper;
    private String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        etComment = findViewById(R.id.et_feedback);
        etRate = findViewById(R.id.tv_rate_label);
        etUserEmail = findViewById(R.id.et_email); // Hidden field
        etFirstName = findViewById(R.id.et_first_name);
        etLastName = findViewById(R.id.et_last_name);

        btnSubmit = findViewById(R.id.btn_submit);
        btnGoBack = findViewById(R.id.btn_go_back);

        connectHelper = new CreateConnection(this);
        Intent intent = getIntent();
        data = (UserData) intent.getSerializableExtra("data");

        if (data != null)
        {
            userEmail = data.getNormalizedEmail().toLowerCase();
            etUserEmail.setText(userEmail);
        } else {
            Toast.makeText(this, "User email not provided!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitFeedback();
            }
        });

        btnGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void submitFeedback()
    {
        String message = etComment.getText().toString().trim();
        String rating = etRate.getText().toString().trim();
        String fName = etFirstName.getText().toString().trim();
        String lName = etLastName.getText().toString().trim();

        if (message.isEmpty() || rating.isEmpty() || fName.isEmpty() || lName.isEmpty())
        {
            Toast.makeText(this, "All fields are required.", Toast.LENGTH_SHORT).show();
            return;
        }

        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());

        String insertQuery = "INSERT INTO Review (Message, dateTime, Rating, ClientEmail, ClientName, AppUserId) VALUES (?, ?, ?, ?, ?, ?)";

        boolean isInserted = false;
        try (Connection connection = connectHelper.getConnection()) {
            if (connection != null) {
                try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                    preparedStatement.setString(1, message);
                    preparedStatement.setTimestamp(2, currentTimestamp); // Use current system time
                    preparedStatement.setInt(3, 4);
                    preparedStatement.setString(4, userEmail); // Use email from intent
                    preparedStatement.setString(5, lName + " " + fName);
                    preparedStatement.setString(6, "");
                    isInserted = preparedStatement.executeUpdate() > 0;
                }
            } else {
                Toast.makeText(this, "Connection failed! Please try again.", Toast.LENGTH_SHORT).show();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error submitting feedback: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        if (isInserted)
        {
            Intent i = new Intent(getBaseContext(), HomeActivity.class);
            i.putExtra("data",data);
            startActivity(i);
            Toast.makeText(this, "Feedback submitted successfully!", Toast.LENGTH_SHORT).show();
            clearFields();
        } else {
            Toast.makeText(this, "Failed to submit feedback.", Toast.LENGTH_SHORT).show();
        }
    }

    private void clearFields() {
        etComment.setText("");
        etRate.setText("");
        etUserEmail.setText("");
    }
}
