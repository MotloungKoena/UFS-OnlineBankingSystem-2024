package motloung.koena.kovsiesbanking.roleActivities;

import android.content.DialogInterface;
import android.os.Bundle;

import motloung.koena.kovsiesbanking.user.LoginActivity;
import motloung.koena.kovsiesbanking.R;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.Toolbar;

import motloung.koena.kovsiesbanking.user.RegisterActivity;
import motloung.koena.kovsiesbanking.adapters.UserData;
import motloung.koena.kovsiesbanking.admin.ChangePassword;
import motloung.koena.kovsiesbanking.admin.LoginSessions;
import motloung.koena.kovsiesbanking.admin.UpdateAccount;
import motloung.koena.kovsiesbanking.admin.ViewAllUsers;
import motloung.koena.kovsiesbanking.financialAdvisor.AdvisorProfile;

public class AdminHomeActivity extends AppCompatActivity
{
    TextView txtViewAllUsers, txtManageUserAccounts, txtChangeUserPassword
            ,txtViewLoginSessions, txtAddNewUser;
    UserData data;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        Toolbar toolbar = findViewById(R.id.admin_toolbar);
        setSupportActionBar(toolbar);

        txtViewAllUsers = findViewById(R.id.view_all_users);
        txtManageUserAccounts = findViewById(R.id.manage_user_accounts);
        txtChangeUserPassword = findViewById(R.id.change_user_password);
        txtAddNewUser = findViewById(R.id.add_new_user);

        Intent intent = getIntent();
        data = (UserData) intent.getSerializableExtra("data");
        txtViewAllUsers.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(AdminHomeActivity.this, ViewAllUsers.class);
                startActivity(intent);
            }
        });

        txtManageUserAccounts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(AdminHomeActivity.this, UpdateAccount.class);
                startActivity(intent1);
            }
        });

        txtChangeUserPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(AdminHomeActivity.this, ChangePassword.class);
                startActivity(intent2);
            }
        });

        txtAddNewUser.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Intent intent3 = new Intent(AdminHomeActivity.this, RegisterActivity.class);
                startActivity(intent3);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.admin_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if (id == R.id.admin_action_user_logins)
        {
            Intent intent3 = new Intent(AdminHomeActivity.this, LoginSessions.class);
            startActivity(intent3);
            Toast.makeText(this, "Users Login Sessions", Toast.LENGTH_SHORT).show();
            return true;
        }

        else if (id == R.id.admin_action_logout)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Confirm Logout");
            builder.setMessage("Are you sure you want to log out?");

            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(AdminHomeActivity.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AdminHomeActivity.this, LoginActivity.class);
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

        else if (id == R.id.admin_personal_details)
        {
            Intent intent = new Intent(AdminHomeActivity.this, AdvisorProfile.class);
            intent.putExtra("data",data);
            startActivity(intent);
            Toast.makeText(this, "Logged in user details>>", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

