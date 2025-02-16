package motloung.koena.kovsiesbanking.roleActivities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import motloung.koena.kovsiesbanking.user.LoginActivity;
import motloung.koena.kovsiesbanking.R;
import motloung.koena.kovsiesbanking.adapters.UserData;
import motloung.koena.kovsiesbanking.admin.LoginSessions;
import motloung.koena.kovsiesbanking.admin.ViewAllUsers;
import motloung.koena.kovsiesbanking.financialAdvisor.AdvisorProfile;

public class AdvisorHomeActivity extends AppCompatActivity
{
    private Button btnViewAllClients;
    private Button btnAdvisorDetails;
    UserData data;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finadvisor);

        Toolbar toolbar = findViewById(R.id.advisor_toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        data = (UserData) intent.getSerializableExtra("data");

        btnViewAllClients = findViewById(R.id.btnViewAllClients);
        btnAdvisorDetails = findViewById(R.id.btnAdvisorPersonalDetails);

        btnViewAllClients.setOnClickListener(v -> handleViewClients());
        btnAdvisorDetails.setOnClickListener(v -> handlePersonalDetails());
    }

    private void handlePersonalDetails()
    {
        Intent personalDetails = new Intent(AdvisorHomeActivity.this, AdvisorProfile.class);
        personalDetails.putExtra("data",data);
        startActivity(personalDetails);
    }

    private void handleViewClients()
    {
        Intent ViewAll = new Intent(AdvisorHomeActivity.this, ViewAllUsers.class);
        ViewAll.putExtra("role", "FinancialAdvisor");
        startActivity(ViewAll);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.advisor_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if (id == R.id.advisor_action_user_logins)
        {
            Intent intent3 = new Intent(AdvisorHomeActivity.this, LoginSessions.class);
            startActivity(intent3);
            Toast.makeText(this, "Users Login Sessions", Toast.LENGTH_SHORT).show();
            return true;
        }
        else if (id == R.id.advisor_action_logout)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Confirm Logout");
            builder.setMessage("Are you sure you want to log out?");

            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(AdvisorHomeActivity.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AdvisorHomeActivity.this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
            });

            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            AlertDialog alert = builder.create();
            alert.show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
