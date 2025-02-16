package motloung.koena.kovsiesbanking.user;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.Toolbar;

import com.google.android.material.card.MaterialCardView;

import java.util.Locale;

import motloung.koena.kovsiesbanking.Fragments.CardsFragment;
import motloung.koena.kovsiesbanking.Fragments.HomeFragment;
import motloung.koena.kovsiesbanking.Fragments.UserProfileFragment;
import motloung.koena.kovsiesbanking.R;
import motloung.koena.kovsiesbanking.adapters.UserData;

public class HomeActivity extends AppCompatActivity
{
    UserData data;
    TextView txtProfileName;
    MaterialCardView cardView;
    TextView txtEmail, txtViewBalance, txtSendMoney, txtNotif, txtTransactions, txtDeposit, txtCashSend, txtViewCards;
    ImageView imgViewBalance, imgSendMoney, imgViewNotification, imgViewTransactions,
    imgDepositMoney, imgToolbarIcon;
    private TextView notificationCountView;
    Button btnViewBalance, btnTransferMoney, btnTransactions, btnNotifications, btnEditProfile, btnDeposit;
    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        data = (UserData) intent.getSerializableExtra("data");

        cardView = findViewById(R.id.buttonEditProfile);

        txtDeposit = findViewById(R.id.textViewDepositMoney);
        txtSendMoney = findViewById(R.id.textViewTransferMoney);
        txtTransactions = findViewById(R.id.textViewTransactions);
        txtViewBalance = findViewById(R.id.textViewBalance);
        txtProfileName = findViewById(R.id.textProfileName);
        txtCashSend = findViewById(R.id.textViewCashSend);
        txtViewCards = findViewById(R.id.textViewViewCards);

        imgToolbarIcon = findViewById(R.id.toolbar_icon);


        txtProfileName.setText("Hello, " + data.getFirstName());

        txtEmail = findViewById(R.id.textViewEmail);
        txtEmail.setText(data.getNormalizedEmail().toLowerCase(Locale.ROOT));


        txtViewBalance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(HomeActivity.this, ViewBalance.class);
                intent1.putExtra("data",data);
                startActivity(intent1);
            }
        });
        txtSendMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(getBaseContext(), SendMoneyActivity.class);
                intent2.putExtra("data",data);
                startActivity(intent2);
            }
        });

        txtTransactions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent3 = new Intent(HomeActivity.this, UserTransactions.class);
                intent3.putExtra("data",data);
                startActivity(intent3);
            }
        });

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent4 = new Intent(HomeActivity.this, UpdateProfile.class); //it should be UpdateProfile here
                intent4.putExtra("data",data);
                startActivity(intent4);
            }
        });

        imgToolbarIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent5 = new Intent(HomeActivity.this, Notifications.class);
                intent5.putExtra("data",data);
                startActivity(intent5);
            }
        });

        txtDeposit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent6 = new Intent(HomeActivity.this, DepositMoneyActivity.class);
                intent6.putExtra("data",data);
                startActivity(intent6);
            }
        });

        txtCashSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent7 = new Intent(HomeActivity.this, CashSendActivity.class);
                intent7.putExtra("data",data);
                startActivity(intent7);
            }
        });

        txtViewCards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent8 = new Intent(HomeActivity.this, CardActivity.class);
                intent8.putExtra("data",data);
                startActivity(intent8);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_submit_review)
        {
            Toast.makeText(this, "Rate Kovsies Banking!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(HomeActivity.this, Review.class);
            intent.putExtra("data",data);
            startActivity(intent);
            return true;
        }
        else if (id == R.id.action_logout) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Confirm Logout");
            builder.setMessage("Are you sure you want to log out?");

            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(HomeActivity.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
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
        else if(id == R.id.update_password)
        {
            Intent intent = new Intent(HomeActivity.this, UpdatePassword.class);
            intent.putExtra("data",data);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
